package com.uniolco.weathapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.network.ApiWeatherService
import com.uniolco.weathapp.data.network.ConnectivityInterceptorImpl
import com.uniolco.weathapp.data.network.WeatherNetworkDataSource
import com.uniolco.weathapp.data.network.WeatherNetworkDataSourceImpl
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).
            get(CurrentWeatherViewModel::class.java)

        bindUI()
//        // TODO: Use the ViewModel
//        val apiService = ApiWeatherService(ConnectivityInterceptorImpl(this.requireContext()))
//        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
//
//        weatherNetworkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, Observer {
//            currentWeatherTextView.text = it.toString()
//        })
//
//        GlobalScope.launch(Dispatchers.Main) {
//            weatherNetworkDataSource.fetchCurrentWeather("London")
//        }
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            progressBar.visibility = View.GONE
            updateLocation("Minsk")
            updateDate(it.dt.toLong())
            updateTemperature(it.main.temp, it.main.feelsLike)
            updateCondition(it.wind.speed, it.weather[0].description, it.main.humidity, it.main.pressure)
        })
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(time: Long){

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
        val netDate = Date(time*1000)
        val date =sdf.format(netDate)
        (activity as? AppCompatActivity)?.supportActionBar?.title = date

        // java.time.format.DateTimeFormatter.ISO_INSTANT
        //                .format(java.time.Instant.ofEpochSecond(time))
    }

    private fun updateTemperature(temperature: Double, temperatureFeelsLike: Double){
        textView_temperature.text = "${temperature-273.15}°C"
        textView_feels_like_temperature.text = String.format("Feels like: %.2f°C", temperatureFeelsLike-273.15)
    }

    private fun updateCondition(windSpeed: Double, weatherDescription: String, humidity: Int,
    pressure: Int){
        textView_wind.text = "Wind speed: $windSpeed m/s"
        textView_humidity.text = "Humidity: $humidity%"
        textView_pressure.text = "Pressure: ${pressure*0.75} mmHg"
        textView_weatherDesc.text = "Few words about weather: $weatherDescription"
    }

}