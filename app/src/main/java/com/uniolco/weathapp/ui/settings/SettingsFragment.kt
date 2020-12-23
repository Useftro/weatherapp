package com.uniolco.weathapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.uniolco.weathapp.R
import com.uniolco.weathapp.ui.LoginActivity
import com.uniolco.weathapp.ui.base.SharedViewModel
import com.uniolco.weathapp.ui.weather.current.CurrentWeatherFragment

const val LOG_BUTTON = "LOG_BUTTON"

class SettingsFragment: PreferenceFragmentCompat() {

    private val model: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = " "
        val preferenceButton = findPreference<Preference>(LOG_BUTTON)!!
        Log.d("OPOPOP", arguments.toString())
        val args: CurrentWeatherFragment by navArgs()
        model.selected.observe(viewLifecycleOwner, Observer { item ->
            Log.d("FGFGFG", item.toString())

            preferenceButton.setOnPreferenceClickListener(object: Preference.OnPreferenceClickListener{
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    if(item == false){
                        val intent = Intent(preferenceButton.context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        try {
                            FirebaseAuth.getInstance().signOut()
                            model.selected.postValue(false)
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
    }
}