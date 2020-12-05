package com.uniolco.weathapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.OneCallServerResponse
import com.uniolco.weathapp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: ApiWeatherService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedOneCallWeather = MutableLiveData<OneCallServerResponse>()
    override val downloadedOneCallWeather: LiveData<OneCallServerResponse>
        get() = _downloadedOneCallWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            Log.d("LOCATION", location)
            val fetchedCurrentWeather = apiWeatherService
                .getCurrentWeather(location).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
    }

    override suspend fun fetchOneCallWeather(latitude: String, longitude: String) {
        try{
            val fetchedOneCallWeather = apiWeatherService.
                getOneCallWeather(latitude, longitude)
                .await()
            _downloadedOneCallWeather.postValue(fetchedOneCallWeather)
            Log.d("MIHAILCOORD", "${latitude}, ${longitude}")
            Log.d("MIHAIL", _downloadedOneCallWeather.value.toString())
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchOneCall", e)
        }
    }
}