package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.CurrentWeatherDao
import com.uniolco.weathapp.data.db.OneCallWeatherDao
import com.uniolco.weathapp.data.db.entity.current.Coord
import com.uniolco.weathapp.data.network.WeatherNetworkDataSource
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.OneCallServerResponse
import com.uniolco.weathapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


// just doing it easier to change something in future
class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
//    private val oneCallWeatherDao: OneCallWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather) // on data changes we are updating our data
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse> { // need to work under metric and imperial
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext currentWeatherDao.getWeatherMetric()
            //return@withContext if (metric) currentWeatherDao.getWeatherMetric()
        }
    }

    override suspend fun getOneCallWeather(): LiveData<OneCallServerResponse> {
        TODO("FDFDFD")
//        return withContext(Dispatchers.IO){
//            return@withContext oneCallWeatherDao.getData()
//        }
    }

    override suspend fun getCurrentLocation(): LiveData<Coord> {
        return withContext(Dispatchers.IO){
            return@withContext currentWeatherDao.getLocation()
        }
    }

    override suspend fun getCurrentTime(): Long {
        return withContext(Dispatchers.IO){
            return@withContext getCurrentTime()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.insertOrUpdate(fetchedWeather)
        } // inserting or updating our forecast im DB
    }

    private suspend fun initWeatherData(){
        val lastWeatherLocation = currentWeatherDao.getLocation().value
        val lastWeatherLocationName = currentWeatherDao.getWeatherMetric().value

        if ((lastWeatherLocation == null || lastWeatherLocationName == null) || locationProvider.hasLocationChanged(lastWeatherLocation, lastWeatherLocationName)) {
            fetchCurrentWeather()
            return
        }

        if(isFetchCurrentNeeded(ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(
                    getCurrentTime()
                ), ZoneId.systemDefault()))) // dummy value
            fetchCurrentWeather() // from here we init our weather forecast
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString()
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thiftyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thiftyMinutesAgo)

    }
}