package com.uniolco.weathapp.data.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "3cf54167b82e44289c7144355200812"

// api.openweathermap.org/data/2.5/weather?q=London&appid=e6837893ce79229aee822e26a1556818

const val BASE_URL = "https://api.weatherapi.com/v1/"

interface ApiWeatherService {

    @GET(value = "current.json")
    fun getCurrentWeather(
        @Query("q") location: String
    ): Deferred<CurrentWeatherResponse>

    @GET(value = "forecast.json")
    fun getFutureWeather(
        @Query("q") location: String,
        @Query("days") days: Int
    ): Deferred<FutureWeatherResponse>


    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApiWeatherService { // we can use our service as if were a function
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                Log.d("URL", url.toString())
                val request = chain.request().newBuilder().url(url).build()

                Log.d("Request", request.toString())

                return@Interceptor chain.proceed(request)

            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                 // we could use just ConnectivityInterceptorImpl as argument
                    //but it's not good because of tight coupling, that's why we use interface to further do DI with Kodein
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiWeatherService::class.java)
        }
    }
}