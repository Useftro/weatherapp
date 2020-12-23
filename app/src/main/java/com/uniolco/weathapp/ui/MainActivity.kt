package com.uniolco.weathapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.component1
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.uniolco.weathapp.R
import com.uniolco.weathapp.ui.base.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val data = intent.getBooleanExtra("Logged", false)
        Log.d("DATA", data.toString())

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.setGraph(R.navigation.mobile_navigation, bundleOf(Pair("Logged", data)))
        // setting up navigation controller
        Log.d("RTYUI", navController.currentDestination.toString())
        bottom_nav.setupWithNavController(navController) // setting up bottom nav bar
        val model: SharedViewModel by viewModels()
        model.selected.postValue(data)
        model.selected.observe(this, Observer {
            if(it == false){
                bottom_nav.menu.getItem(0).isVisible = false
            }
            else{
                bottom_nav.menu.getItem(0).isVisible = true
            }
        })
        NavigationUI.setupActionBarWithNavController(this, navController)
        requestLocationPermission()
        if (hasLocationPermission()){
            bindLocationManager()
        }
        else
            requestLocationPermission()
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