package com.uniolco.weathapp.data.network


import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uniolco.weathapp.data.db.entity.current.Condition
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse
import com.uniolco.weathapp.internal.NoConnectivityException
import java.util.*

const val NUMBER_OF_DAYS = 7

val language = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
    Resources.getSystem().configuration.locales[0].language
} else {
    Resources.getSystem().configuration.locale.language
}

class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: ApiWeatherService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    private val _downloadedFavoriteWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedFavoriteWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedFavoriteWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            val fetchedCurrentWeather = apiWeatherService
                .getCurrentWeather(location, language).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
        catch (e: retrofit2.HttpException){
            val currentResp = CurrentWeatherResponse(CurrentWeather(condition =
            Condition(code = 1000, icon = "//cdn.weatherapi.com/weather/64x64/day/113.png")),
                WeatherLocation(name = "Wrong location.", tzId = "Europe/Madrid"))
            _downloadedCurrentWeather.postValue(currentResp)
        }
    }

    override suspend fun fetchFutureWeather(location: String) {
        try{
            val fetchedFutureWeather = apiWeatherService
                .getFutureWeather(location, NUMBER_OF_DAYS, language).await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
        catch (e: retrofit2.HttpException){
            val fetchedFutureWeather = apiWeatherService
                .getFutureWeather("Minsk", 7, language).await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
    }

    override suspend fun fetchFavoriteWeather(location: String) {
        try{
            val fetchedFavoriteWeather = apiWeatherService
                .getCurrentWeather(location, language).await()
            _downloadedFavoriteWeather.postValue(fetchedFavoriteWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
    }

}