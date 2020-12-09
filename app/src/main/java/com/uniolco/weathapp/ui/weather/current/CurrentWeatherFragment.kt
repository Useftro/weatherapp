package com.uniolco.weathapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.uniolco.weathapp.R
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.ZonedDateTime
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
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val currentLocation = viewModel.weatherLocation.await()

        currentLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
            updateDate(location.zonedDateTime)
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            progressBar.visibility = View.GONE
            updateTemperature(it.tempC, it.feelslikeC)
            updateCondition(it.windKph, it.visKm.toString(), it.humidity, it.pressureMb.toInt())
            
        })
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(time: ZonedDateTime){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = time.toString()
    }

    private fun updateTemperature(temperature: Double, temperatureFeelsLike: Double){
        textView_temperature.text = "${temperature}°C"
        textView_feels_like_temperature.text = String.format("Feels like: ${temperatureFeelsLike}°C")
    }

    private fun updateCondition(windSpeed: Double, weatherDescription: String, humidity: Int,
    pressure: Int){
        textView_wind.text = "Wind speed: $windSpeed m/s"
        textView_humidity.text = "Humidity: $humidity%"
        textView_pressure.text = "Pressure: ${pressure*0.75} mmHg"
        textView_weatherDesc.text = "Visibility: $weatherDescription"
    }

}