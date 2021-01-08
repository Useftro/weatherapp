package com.uniolco.weathapp.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.converter.LocalDateConverter
import com.uniolco.weathapp.internal.DateNotFoundException
import com.uniolco.weathapp.internal.background
import com.uniolco.weathapp.internal.glide.GlideApp
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.favorite_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_cityName
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_coluds
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_humitidy
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_maxWind
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_min_max_temperature
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_visibility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureDetailWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactoryInstanceFactory:
            ((LocalDate) -> FutureDetailWeatherViewModelFactory) by factory()
    private lateinit var viewModel: FutureDetailWeatherViewModel

    companion object {
        fun newInstance() = FutureDetailWeatherFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date = LocalDateConverter.stringToDate(safeArgs?.dateString!!) ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date)).get(FutureDetailWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.weather.await()
        val weatherLoc = viewModel.weatherLocation.await()

        weatherLoc.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeather.observe(viewLifecycleOwner, Observer { weather ->
            if(weather == null) return@Observer

            updateDate(weather.date)
            updateChanceOfRain(weather.dailyChanceOfRain)
            updateSunsetAndSunrise(weather.sunset, weather.sunrise)
            updateTemperatures(weather.avgtempC, weather.mintempC, weather.maxtempC)
            updateVisibility(weather.avgvisKm)
            updateWindSpeed(weather.maxWindSpeed)
            updateHumidity(weather.avghumidity)

            GlideApp.with(this@FutureDetailWeatherFragment)
                .load("https:${weather.conditionIcon}")
                .into(imageView_condition_icon)

            GlideApp.with(this@FutureDetailWeatherFragment).
            load(background(weather.conditionCode)).
            into(futureFavorite_background)
            futureFavorite_background.imageAlpha = 90
        })

    }

    private fun updateDate(date: LocalDate){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            location
    }

    private fun updateTemperatures(temp: Double, temp_max: Double, temp_min: Double){
        textView_min_max_temperature.text = "Min: ${temp_min}°C, Max: ${temp_max}°C"
        textView_cityName.text = "$temp°C"
    }

    private fun updateWindSpeed(windSpeed: Double){
        textView_maxWind.text = "Max wind speed: \n $windSpeed m/s"
    }

    private fun updateChanceOfRain(chanceOfRain: String){
        textView_coluds.text = "Chance of rain: $chanceOfRain"
    }

    private fun updateSunsetAndSunrise(sunset: String, sunrise: String){
        textView_sunset.text = "Sunset: $sunset"
        textView_sunrise.text = "Sunrise: $sunrise"
    }

    private fun updateVisibility(visibility: Double){
        textView_visibility.text = "Visibility: $visibility"
    }

    private fun updateHumidity(humidity: Double){
        textView_humitidy.text = "Humidity: $humidity"
    }

}