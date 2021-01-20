package com.uniolco.weathapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniolco.weathapp.R

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }


    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        Handler().postDelayed({
            updateUI(currentUser)
            finish()
        }, 3000)

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if(currentUser != null){
            val inten = Intent(this, MainActivity::class.java)
            inten.putExtra("Logged", true)
            inten.putExtra("Email", currentUser.email)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            inten.putExtra("registered", intent.getBooleanExtra("registered", false))
            startActivity(inten)
            finish()
        }
        else{
            val passed = sharedPreferences.getBoolean("Passed", false)
            if (!passed)
                startActivity(Intent(this, LoginActivity::class.java))
            else
                startActivity(Intent(this, MainActivity::class.java))
        }
    }
}