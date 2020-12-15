package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.CurrentWeatherDao
import com.uniolco.weathapp.data.db.FavoriteWeatherDao
import com.uniolco.weathapp.data.db.FutureWeatherDao
import com.uniolco.weathapp.data.db.WeatherLocationDao
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.db.entity.favorite.FavoriteEntry
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.db.unitlocalized.future.detailed.UnitSpecificDetailedFutureWeatherEntry
import com.uniolco.weathapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.uniolco.weathapp.data.network.NUMBER_OF_DAYS
import com.uniolco.weathapp.data.network.WeatherNetworkDataSource
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse
import com.uniolco.weathapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate


// just doing it easier to change something in future
class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val favoriteWeatherDao: FavoriteWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather) // on data changes we are updating our data
        }
        weatherNetworkDataSource.downloadedFutureWeather.observeForever { newFutureWeather ->
            persistFetchedFutureWeather(newFutureWeather)
        }
        weatherNetworkDataSource.downloadedFavoriteWeather.observeForever { newFavoriteWeather ->
            persistFavoriteWeather(FavoriteEntry(weather = newFavoriteWeather))
        }
    }

    private fun persistFetchedFutureWeather(newFutureWeather: FutureWeatherResponse) {
        fun deleteOldForecastData(){
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = newFutureWeather.futureWeatherEntries.forecastdays
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.insertOrUpdate(newFutureWeather.location)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeather> { // need to work under metric and imperial
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext currentWeatherDao.getWeatherMetric()
            //return@withContext if (metric) currentWeatherDao.getWeatherMetric()
        }
    }

    private fun persistFavoriteWeather(favoriteWeather: FavoriteEntry){
        GlobalScope.launch(Dispatchers.IO) {
            favoriteWeatherDao.insertWeather(favoriteWeather)
        }
    }

    override suspend fun getExactFavorite(location: String): FavoriteEntry{
        return withContext(Dispatchers.IO){
            fetchFavoriteWeather(location)
            return@withContext favoriteWeatherDao.getExactFavorite(location)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.insertOrUpdate(fetchedWeather.current)
            weatherLocationDao.insertOrUpdate(fetchedWeather.location)
        }
    }



    private suspend fun initWeatherData(){
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if(isFetchFutureNeeded())
            fetchFutureWeather()
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < NUMBER_OF_DAYS
    }

    private suspend fun fetchFavoriteWeather(locations: String){
        weatherNetworkDataSource.fetchFavoriteWeather(
            locations
        )
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString()
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: org.threeten.bp.ZonedDateTime): Boolean {
        val thirtyMinutesAgo = org.threeten.bp.ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext futureWeatherDao.getSimpleWeatherForecastMetric(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(date: LocalDate): LiveData<out UnitSpecificDetailedFutureWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext futureWeatherDao.getDetailedMetricWeatherByDate(date)
        }
    }

    override suspend fun getFavorites(locations: String): List<FavoriteEntry> {
        return withContext(Dispatchers.IO){
            fetchFavoriteWeather(locations)
            return@withContext favoriteWeatherDao.getAllFavorites()
        }
    }

    override suspend fun getAllLocations(): List<Locations> {
        return withContext(Dispatchers.IO){
            return@withContext favoriteWeatherDao.getAllLocations()
        }
    }

    override suspend fun insertLocations(
        weatherLocation: Locations
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            favoriteWeatherDao.insertLocations(weatherLocation)
        }
    }

    override suspend fun deleteLocation(locations: Locations) {
        GlobalScope.launch(Dispatchers.IO) {
            favoriteWeatherDao.deleteLocation(locations)
        }
    }

    override suspend fun insertFavoriteEntry(favoriteEntry: FavoriteEntry) {
        GlobalScope.launch(Dispatchers.IO) {
            favoriteWeatherDao.insertWeather(favoriteEntry)
        }
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString()
        )
    }
}