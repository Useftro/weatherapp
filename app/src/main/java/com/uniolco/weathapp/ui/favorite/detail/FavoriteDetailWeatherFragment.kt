package com.uniolco.weathapp.ui.favorite.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uniolco.weathapp.R
import com.uniolco.weathapp.internal.background
import com.uniolco.weathapp.internal.glide.GlideApp
import com.uniolco.weathapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.favorite_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_coluds
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_cityName
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_humitidy
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_maxWind
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_min_max_temperature
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunrise
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunset
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_visibility
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class FavoriteDetailWeatherFragment : ScopeFragment(), KodeinAware, OnMapReadyCallback {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactoryInstanceFactory:
            ((String) -> FavoriteDetailWeatherViewModelFactory) by factory()

    companion object {
        fun newInstance() = FavoriteDetailWeatherFragment()
    }

    private lateinit var viewModel: FavoriteDetailWeatherViewModel

    private var mMap: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.favorite_detail_weather_fragment, container, false)
        mMap = view?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)
        return view
        //inflater.inflate(R.layout.favorite_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { FavoriteDetailWeatherFragmentArgs.fromBundle(it) }

        Log.d("ARGS", safeArgs.toString())
        val loc = safeArgs?.locationName.toString()
        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(loc)).get(FavoriteDetailWeatherViewModel::class.java)
        bindUI()
        var clicked: Boolean = false
        val mapViewHeight = mapView.layoutParams.height
        val mapViewWidth = mapView.layoutParams.width
        val layoutWidth = const_layout.maxWidth
        val layoutHeight = const_layout.maxHeight
        mapFullScreen_btn.setOnClickListener {
            if(!clicked){
                clicked = true
                val relPar: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(layoutHeight, layoutWidth)
                mapView.rootView.layoutParams = relPar
//                mapView.layoutParams.height = 2
//                Log.d("HEHEHE", mapView.layoutParams.height.toString() + "; $layoutHeight")
//                mapView.layoutParams.width = 2
            }
            else{
                val relPar: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(mapViewHeight, mapViewWidth)
                mapView.rootView.layoutParams = relPar
//                mapView.layoutParams.height = mapViewHeight
//                mapView.layoutParams.width = mapViewWidth
                clicked = false
            }
        }
    }

    private fun bindUI() = launch {
        val weather = viewModel.exactWeather.await()
        Log.d("WEWEWEWe", weather.toString())
        updateLocation(weather.weather.location.name.toString())
        updateTemperatures(weather.weather.current.tempC, weather.weather.current.feelslikeC)
        updateVisibility(weather.weather.current.visKm)
        updateWindSpeed(weather.weather.current.windKph)
        updateHumidity(weather.weather.current.humidity)
        updateClouds(weather.weather.current.cloud)
        GlideApp.with(imageView_cond).load("https://" + weather.weather.current.condition.icon).into(imageView_cond)
        GlideApp.with(this@FavoriteDetailWeatherFragment).
            load(background(weather.weather.current.condition.code)).
            into(favorite_background)
        favorite_background.imageAlpha = 90
        Log.d("ARGUILS", weather.toString())
    }

    private suspend fun getCurrentLocation(): Pair<Double, Double>{
        val weather = viewModel.exactWeather.await()
        return Pair(weather.weather.location.lat, weather.weather.location.lon)
    }

    private fun updateTitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Favorite"
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateTemperatures(temp: Double, temp_feelsLike: Double){
        textView_min_max_temperature.text = "Feels like: ${temp_feelsLike}°C"
        textView_cityName.text = "$temp°C"
    }

    private fun updateWindSpeed(windSpeed: Double){
        textView_maxWind.text = "Wind speed: \n $windSpeed km/h"
    }

    private fun updateClouds(clouds: Int){
        textView_coluds.text = "Clouds: $clouds"
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

    override fun onMapReady(p0: GoogleMap) {
        launch {
            val pair = getCurrentLocation()
            p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(pair.first, pair.second), 12f))
            p0.addMarker(MarkerOptions().position(LatLng(pair.first, pair.second)))
            p0.uiSettings.isZoomControlsEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

}