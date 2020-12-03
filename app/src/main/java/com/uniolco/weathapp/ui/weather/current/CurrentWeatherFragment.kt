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
        Log.d("ViewModelCurrentLoc", viewModel.weatherLocation.toString())
        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val currentLocation = viewModel.weatherLocation.await()
        Log.d("CURRENTLOCATION", currentLocation.toString())

        currentLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            progressBar.visibility = View.GONE
            updateLocation(it.name)
            updateDate(it.dt)
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
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = date
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