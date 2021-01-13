package com.uniolco.weathapp.ui

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.firebase.User
import com.uniolco.weathapp.internal.notification.ReminderBroadcast
import com.uniolco.weathapp.ui.base.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1
private const val CHANNEL_ID = "BibaBoba"

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            Log.d("LOCATI,", p0.toString())
            super.onLocationResult(p0)
        }
    }

    private lateinit var navController: NavController

    val model: SharedViewModel by viewModels()


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


        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)


        // getting info from login acitivity if logged, if registered and email
        val logged = intent.getBooleanExtra("Logged", false)
        val registered = intent.getBooleanExtra("registered", false)
        val email = intent.getStringExtra("Email")


        // notifications
        createNotificationChannel()
        val inte: Intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, inte, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val time = System.currentTimeMillis()

        val time10sec: Long = 1000 * 10

        if(sharedPreferences.getBoolean("USE_NOTIFICATION", true)) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, time10sec, pendingIntent)
        }
        else{
            alarmManager.cancel(pendingIntent)
        }
        // end of notifications


        // creating user
        val user = User(
            intent.getStringExtra("login").toString(),
            intent.getStringExtra("email").toString(),
            intent.getStringExtra("phone").toString(),
            intent.getStringExtra("name").toString(),
            intent.getStringExtra("surname").toString(),
            intent.getStringExtra("address").toString())

        Log.d("DATA", logged.toString())
        Log.d("MAINUSER", user.toString())


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.setGraph(R.navigation.mobile_navigation, bundleOf(Pair("Logged", logged)))
        // setting up navigation controller
        Log.d("RTYUI", navController.currentDestination.toString())
        bottom_nav.setupWithNavController(navController) // setting up bottom nav bar

        model.loggedIn.postValue(logged)
        model.registered.postValue(registered)
        model.personInfo.postValue(user)
        model.email.postValue(email)
        Log.d("EMAAAAAIl", email.toString())
        Log.d("REGISTEREEEEEEEED", registered.toString())


//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("OLOLO", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            Log.d("boyoyoyoyoy", token)
//            Toast.makeText(baseContext, "Token: $token", Toast.LENGTH_SHORT).show()
//        })



        observeModel(model)
        observeIfFromSettings(model)
        NavigationUI.setupActionBarWithNavController(this, navController)
        requestLocationPermission()
        if (hasLocationPermission()){
            bindLocationManager()
        }
        else
            requestLocationPermission()
    }





    private fun observeModel(model: SharedViewModel){
        model.loggedIn.observe(this, Observer {
            bottom_nav.menu.getItem(0).isVisible = it != false
        })
    }

    private fun observeIfFromSettings(model: SharedViewModel){
        model.ifFromSettings.observe(this, Observer {
            if(it == null)
                return@Observer
            if(it == true){
                finish()
                model.ifFromSettings.postValue(false)
            }
        })
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
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