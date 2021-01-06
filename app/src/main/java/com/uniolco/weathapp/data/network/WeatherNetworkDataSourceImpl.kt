package com.uniolco.weathapp.data.network

import android.content.Context
import android.util.AndroidRuntimeException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse
import com.uniolco.weathapp.internal.CityNotFound
import com.uniolco.weathapp.internal.NoConnectivityException
import retrofit2.HttpException
import kotlin.coroutines.coroutineContext

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

    private val _downloadedFavoriteWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedFavoriteWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedFavoriteWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            Log.d("FDSKFL:SDF", "FETCHING...........")
            val fetchedCurrentWeather = apiWeatherService
                .getCurrentWeather(location).await()
            Log.d("FDSKFL:SDF", "FETCHEDDD...........")
            Log.d("FETCHED", fetchedCurrentWeather.toString())
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
        catch (e: retrofit2.HttpException){
            Log.e("Cityyy", "Oh shit, I'm sorry... ${e.message()}")
            val fetchedCurrentWeather = apiWeatherService
                .getCurrentWeather("Minsk").await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
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
        catch (e: retrofit2.HttpException){
            val fetchedFutureWeather = apiWeatherService
                .getFutureWeather("Minsk", 7).await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
    }

    override suspend fun fetchFavoriteWeather(location: String) {
        try{
            val fetchedFavoriteWeather = apiWeatherService
                .getCurrentWeather(location).await()
            _downloadedFavoriteWeather.postValue(fetchedFavoriteWeather)
        }
        catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection fetchCurrentWeather", e)
        }
    }

}