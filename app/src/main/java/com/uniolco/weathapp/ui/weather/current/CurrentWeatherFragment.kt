package com.uniolco.weathapp.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.uniolco.weathapp.internal.background
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import com.google.firebase.firestore.FirebaseFirestore

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
        getUser(model.email.value.toString())
        bindUI()

    }

    private fun bindUI() = launch {
        val currentLocation = viewModel.weatherLocation.await()
        val currentWeather = viewModel.weather.await()
        currentLocation.observe(viewLifecycleOwner, Observer { location ->
            Log.d("TGGG", currentLocation.value?.name.toString())
            current_group.visibility = View.VISIBLE
            progressBar0.visibility = View.GONE
            updateLocation(location.name)
            updateDate(location.zonedDateTime)
            addToFavorite(Locations(0, location, model.email.value.toString()))
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
        model.loggedIn.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            if (it == false){
                favorite_button.isClickable = false
                buttonDisappearOrAppear(disappear = true, clickable = false)
            }
            else{
                favorite_button.isClickable = true
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

    private fun getUser(userEmail: String){ // getting info from firebase database about user
        // getting reference of FirebaseDatabase
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.keepSynced(true)
        // going to Users where searching for email of user if it is then return data about user
        val ordersRef = rootRef.child("Users").orderByChild("email").equalTo(userEmail)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    Log.d("SNAPSHOOOOOT", ds.value.toString())
                    val name = ds.child("name").getValue(String::class.java)
                    val login = ds.child("login").getValue(String::class.java)
                    val surname = ds.child("surname").getValue(String::class.java)
                    val phoneNumber = ds.child("phoneNumber").getValue(String::class.java)
                    val address = ds.child("address").getValue(String::class.java)
                    val email = ds.child("email").getValue(String::class.java)
                    if (login != null) {
                        model.user.postValue(User(login, email!!, phoneNumber!!, name!!, surname!!, address!!))
                    }
                    Log.d("LALALALLLAL", name.toString() + login.toString() + surname.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ERRORINDATABASEFIREBASE", databaseError.message)
            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)
    }
}