package com.uniolco.weathapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.uniolco.weathapp.R
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        userName.text = sharedPreferences.getString("UserName", "")
        userSurname.text = sharedPreferences.getString("UserSurname", "")
        userEmail.text = sharedPreferences.getString("UserEmail", "")
        userLogin.text = sharedPreferences.getString("UserLogin", "")
        userPhone.text = sharedPreferences.getString("UserPhoneNumber", "")
        userAddress.text = sharedPreferences.getString("UserAddress", "")

    }
}