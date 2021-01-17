package com.uniolco.weathapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.uniolco.weathapp.R

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        requestLocationPermission()
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
            if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                with(sharedPreferences.edit()){
                    putBoolean("askedPermissions", true)
                    apply()
                }
            }
            else
                Toast.makeText(this, "Set weatherLocation manually in settings", Toast.LENGTH_LONG).show()
            val uid = intent.getStringExtra("uid")
            startActivity(Intent(this, MainActivity::class.java).putExtra("uid", uid))
            finish()
        }
    }
}