package com.uniolco.weathapp.ui.weather.current

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.alpha
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.firebase.User
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.ZonedDateTime
import java.util.*
import com.uniolco.weathapp.internal.glide.GlideApp
import com.uniolco.weathapp.ui.base.SharedViewModel

val SNOWSTORM_CODES = listOf<Int>(1117, 1222, 1224)
val SUNNY_CODES = listOf<Int>(1000)
val CLOUDY_CODES = listOf<Int>(1003, 1006)
val OVERCAST_CODES = listOf<Int>(1009)
val THUNDER_CODES = listOf<Int>(1087,1273,1276,1279,1282)
val SNOW_CODES = listOf<Int>(1066,1069,1114,1210,1213,1216,1219,1237,1255,1258)
val MIST_CODES = listOf<Int>(1030,1135,1147)
val RAIN_CODES = listOf<Int>(1063, 1072, 1153, 1168,1178,1180,1183,1186,1189,1192,1195,1198,1201,1204,1207,1240,1243,1246,1249,1252,1261, 1264)

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val model: SharedViewModel by activityViewModels()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        current_group.visibility = View.INVISIBLE
        viewModel = ViewModelProviders.of(this, viewModelFactory).
            get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val currentLocation = viewModel.weatherLocation.await()
        val currentWeather = viewModel.weather.await()
        currentLocation.observe(viewLifecycleOwner, Observer { location ->
            Log.d("TGGG", currentLocation.value?.name.toString())
            progressBar0.visibility = View.GONE
            current_group.visibility = View.VISIBLE
            updateLocation(location.name)
            updateDate(location.zonedDateTime)
            addToFavorite(Locations(0, location))
            if (location == null) return@Observer
        })
        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            progressBar0.visibility = View.GONE
            updateTemperature(it.tempC, it.feelslikeC)
            updateCondition(it.windKph, it.visKm.toString(), it.humidity, it.pressureMb.toInt(), it.condition.text)
            GlideApp.with(this@CurrentWeatherFragment)
                .load("https:${it.condition.icon}")
                .into(imageView_Weather)
            GlideApp.with(this@CurrentWeatherFragment).load(background(it.condition.code)).into(imageView)
            Log.d("CODECODECODECODE", it.condition.code.toString())
            imageView.imageAlpha = 90
        })
        model.authorized.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            if (it == false){
                buttonDisappearOrAppear(disappear = true, clickable = false)
            }
            else{
                buttonDisappearOrAppear(disappear = false, clickable = true)
            }
        })
        model.registered.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            if(it == true){
                val user = model.personInfo.value
                Log.d("ININININ", user.toString())
                if (user != null) {
                    insertUser(user)
                    model.registered.postValue(false)
                }
            }
            else{
                model.personInfo.postValue(viewModel.getUser(model.email.value.toString()).value)
                Log.d("IPIPIPIP", model.personInfo.value.toString() + "; EMAIL: " + model.email.value.toString())
            }
        })
        Log.d("USSSSSSR", model.personInfo.value.toString())
    }

    private fun addToFavorite(favoriteLocation: Locations){
        favorite_button.setOnClickListener {
            launch {
                viewModel.insertIntoFavorite(favoriteLocation)
            }
            Toast.makeText(favorite_button.context, "ADDED", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(time: ZonedDateTime){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "${time.dayOfMonth}.${time.month}"
    }

    private fun updateTemperature(temperature: Double, temperatureFeelsLike: Double){
        textView_cityName.text = "${temperature}°C"
        textView_feels_like_temperature.text = String.format("Feels like: ${temperatureFeelsLike}°C")
    }

    private fun updateCondition(windSpeed: Double, weatherDescription: String, humidity: Int,
    pressure: Int, condition: String){
        textView_maxWind.text = "Wind speed: $windSpeed m/s"
        textView_humidity.text = "Humidity: $humidity%"
        textView_pressure.text = "Pressure: ${pressure*0.75} mmHg"
        textView_weatherDesc.text =
            "Visibility: $weatherDescription\n\nSeems to be like its ${condition.toLowerCase()} today"
    }

    // true = disappear
    private fun buttonDisappearOrAppear(disappear: Boolean, clickable: Boolean){
        lateinit var anim: Animation
        if (disappear){
            anim = AnimationUtils.loadAnimation(favorite_button.context, R.anim.disappearing_button)
        }
        else{
            anim = AnimationUtils.loadAnimation(favorite_button.context, R.anim.appearing_button)
        }
        animate(anim, clickable)
        favorite_button.startAnimation(anim)
//            favorite_button.animate().scaleX(0.0f).scaleY(0.0f).rotation(1080f).setDuration(5000).start()
    }

    private fun insertUser(user: User) = launch {
        viewModel.insertOrUpdateUser(user)
    }

    private fun animate(anim: Animation, clickable: Boolean){
        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                favorite_button.isClickable = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                favorite_button.isClickable = clickable
                if(clickable){
                    favorite_button.visibility = View.VISIBLE
                }
                else{
                    favorite_button.visibility = View.GONE
                }

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun background(code: Int): Int{
        var drawable: Int = 0
        when(code){
            in SNOWSTORM_CODES -> drawable = R.drawable.snowstorm
            in SUNNY_CODES -> drawable = R.drawable.sunny
            in CLOUDY_CODES -> drawable = R.drawable.cloudy
            in OVERCAST_CODES -> drawable = R.drawable.overcast
            in THUNDER_CODES -> drawable = R.drawable.thunder
            in SNOW_CODES -> drawable = R.drawable.snow
            in MIST_CODES -> drawable = R.drawable.mist
            in RAIN_CODES -> drawable = R.drawable.rain
            else -> Log.e("ABCDERFEF", "Oh shit, wrong code...")
        }
        return drawable
    }

}