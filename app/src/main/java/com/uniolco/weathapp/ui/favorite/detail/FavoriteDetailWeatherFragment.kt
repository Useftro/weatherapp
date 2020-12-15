package com.uniolco.weathapp.ui.favorite.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.uniolco.weathapp.R
import com.uniolco.weathapp.internal.glide.GlideApp
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.favorite_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.imageView_condition_icon
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_chanceOfRain
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_cityName
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_humitidy
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_maxWind
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_min_max_temperature
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunrise
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunset
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_visibility
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class FavoriteDetailWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory:
            ((String) -> FavoriteDetailWeatherViewModelFactory) by factory()

    companion object {
        fun newInstance() = FavoriteDetailWeatherFragment()
    }

    private lateinit var viewModel: FavoriteDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_detail_weather_fragment, container, false)
    }

    private fun bindUI() = launch {
        val weather = viewModel.exactWeather.await()
        updateLocation(weather.weather.location.name)

//        updateChanceOfRain(weather.weather.current)
//        updateSunsetAndSunrise(weather.weather.current., weather.sunrise)
        updateTemperatures(weather.weather.current.tempC, weather.weather.current.feelslikeC)
        updateVisibility(weather.weather.current.visKm)
        updateWindSpeed(weather.weather.current.windKph)
        updateHumidity(weather.weather.current.humidity)
        GlideApp.with(imageView_cond).load("https://" + weather.weather.current.condition.icon).into(imageView_cond)
        Log.d("ARGUILS", weather.toString())
    }

    private fun updateTitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Favorite"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { FavoriteDetailWeatherFragmentArgs.fromBundle(it) }

        Log.d("ARGS", safeArgs.toString())
        val loc = safeArgs?.locationName.toString()
        viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(loc)).get(FavoriteDetailWeatherViewModel::class.java)
        bindUI()
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateTemperatures(temp: Double, temp_feelsLike: Double){
        textView_min_max_temperature.text = "Feels like: ${temp_feelsLike}°C"
        textView_cityName.text = "$temp°C"
    }

    private fun updateWindSpeed(windSpeed: Double){
        textView_maxWind.text = "Wind speed: \n ${windSpeed/3.6} m/s"
    }

    private fun updateChanceOfRain(chanceOfRain: String){
        textView_chanceOfRain.text = "Chance of rain: $chanceOfRain"
    }

    private fun updateSunsetAndSunrise(sunset: String, sunrise: String){
        textView_sunset.text = "Sunset: $sunset"
        textView_sunrise.text = "Sunrise: $sunrise"
    }

    private fun updateVisibility(visibility: Double){
        textView_visibility.text = "Visibility: $visibility km"
    }

    private fun updateHumidity(humidity: Int){
        textView_humitidy.text = "Humidity: $humidity"
    }

}