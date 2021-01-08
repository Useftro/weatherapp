package com.uniolco.weathapp.ui

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.format.Time
import androidx.core.os.HandlerCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.preference.PreferenceManager
import com.uniolco.weathapp.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val passed = sharedPreferences.getBoolean("Passed", false)

        val time = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar.HOUR_OF_DAY
        } else {
            Time.HOUR
        }


        Handler().postDelayed({
            if(passed){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}