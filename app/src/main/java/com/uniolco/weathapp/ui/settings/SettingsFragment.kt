package com.uniolco.weathapp.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.uniolco.weathapp.R
import com.uniolco.weathapp.ui.LoginActivity
import com.uniolco.weathapp.ui.base.SharedViewModel

const val LOG_BUTTON = "LOG_BUTTON"

class SettingsFragment: PreferenceFragmentCompat() {

    private val model: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        val preferenceButton = findPreference<Preference>(LOG_BUTTON)!!
        var subtitle = " "
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        model.loggedIn.observe(viewLifecycleOwner, Observer { item ->
            if (item == true){
                preferenceButton.title = "Log out"
            }
            else{
                preferenceButton.title = "Log in"
            }
            Log.d("FGFGFG", item.toString())
            if(item == null)
                return@Observer
            preferenceButton.setOnPreferenceClickListener(object: Preference.OnPreferenceClickListener{
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    if(item == false){
                        val intent = Intent(preferenceButton.context, LoginActivity::class.java)
                        startActivity(intent)
                        model.ifFromSettings.postValue(true)
                    }
                    else{
                        try {
                            FirebaseAuth.getInstance().signOut()
                            model.loggedIn.postValue(false)
                            with(sharedPreferences.edit()){
                                putBoolean("UserNotNull", false)
                                apply()
                            }
                            Toast.makeText(preferenceButton.context,"Signed out!", Toast.LENGTH_SHORT).show()
                        }
                        catch (e: Exception){
                            Log.e("ERROR", "ERROR WITH LOGGING OUT: ${e}")
                        }
                    }
                    return true
                }
            })
        })
        if(model.loggedIn.value == true){
            subtitle = "Hello, ${model.email.value}!"
        }
        else{
            subtitle = "Not authorised."
        }
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = subtitle
    }
}