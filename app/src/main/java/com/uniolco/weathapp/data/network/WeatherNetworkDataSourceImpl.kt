package com.uniolco.weathapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse
import com.uniolco.weathapp.internal.NoConnectivityException

const val NUMBER_OF_DAYS = 7

class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: ApiWeatherService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            val fetchedCurrentWeather = apiWeatherService
                .getCurrentWeather(location).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
    }

    override suspend fun fetchFutureWeather(location: String) {
        try{
            val fetchedFutureWeather = apiWeatherService
                .getFutureWeather(location, NUMBER_OF_DAYS).await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
            Log.d("VALUES", fetchedFutureWeather.futureWeatherEntries.forecastdays.size.toString())
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
    }

}