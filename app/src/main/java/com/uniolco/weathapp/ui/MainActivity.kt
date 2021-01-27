package com.uniolco.weathapp.ui

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.uniolco.weathapp.R
import com.uniolco.weathapp.internal.notification.ReminderBroadcast
import com.uniolco.weathapp.ui.base.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val model: SharedViewModel by viewModels()

    private val locationCallback = object : LocationCallback() {
    }

    private lateinit var navController: NavController


    //creating channel for notif (for API >= 22)
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "ReminderChannel"
            val desc = "Channel for reminding"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notify", name, importance)
            channel.description = desc

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )

        // getting info from login acitivity if logged, if registered and email
        val logged = sharedPreferences.getBoolean("Logged", false)
        val registered = sharedPreferences.getBoolean("registered", false)
        val email = sharedPreferences.getString("Email", "")
        val uid = sharedPreferences.getString("currentuseruid", "")
        model.uid.postValue(uid)
        model.uid.value = uid

        // notifications
        createNotificationChannel()
        val inte: Intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            inte,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // alarm manager for creating notofications every 60 seconds
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        val time10sec: Long = 1000 * 3600 * 12

        if(sharedPreferences.getBoolean("USE_NOTIFICATION", true)) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, time10sec, pendingIntent)
        }
        else{
            alarmManager.cancel(pendingIntent)
        }
        // end of notifications


        // creating user
/*        val user = User(
            intent.getStringExtra("login").toString(),
            intent.getStringExtra("email").toString(),
            intent.getStringExtra("phone").toString(),
            intent.getStringExtra("name").toString(),
            intent.getStringExtra("surname").toString(),
            intent.getStringExtra("address").toString()
        )*/
        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.mobile_navigation)
        navController = navHostFragment.navController
        val dest = R.id.currentWeatherFragment
        navGraph.startDestination = dest
        navController.setGraph(navGraph, bundleOf(Pair("Logged", logged)))
        // use destination fragment
/*        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        navController.setGraph(R.navigation.mobile_navigation, bundleOf(Pair("Logged", logged)))*/
        bottom_nav.setupWithNavController(navController) // setting up bottom nav bar
        NavigationUI.setupActionBarWithNavController(this, navController)

        // setting up navigation controller

        model.loggedIn.postValue(logged)
        model.registered.postValue(registered)
//        model.personInfo.postValue(user)
        model.email.postValue(email)
        with(sharedPreferences.edit()){
            putString("UID", model.uid.value)
            apply()
        }

        observeModel(model)
        observeIfFromSettings(model)
        if (hasLocationPermission()){
            bindLocationManager()
        }
        else
            requestLocationPermission()
    }


    private fun observeModel(model: SharedViewModel){
        model.loggedIn.observe(this, {
            bottom_nav.menu.getItem(0).isVisible = it != false
        })
    }

    private fun observeIfFromSettings(model: SharedViewModel){
        model.ifFromSettings.observe(this, Observer {
            if (it == null)
                return@Observer
            if (it == true) {
                finish()
                model.ifFromSettings.postValue(false)
            }
        })
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION){
            if (!grantResults.contains(PackageManager.PERMISSION_DENIED))
                bindLocationManager()
            else
                Toast.makeText(this, "Set weatherLocation manually in settings", Toast.LENGTH_LONG).show()
        }
    }

    private fun bindLocationManager() { // no DI because we need to use mainactivity as lifecycle owner
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )
    }
}