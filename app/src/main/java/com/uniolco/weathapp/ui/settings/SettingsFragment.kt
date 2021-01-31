package com.uniolco.weathapp.ui.settings

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
import com.uniolco.weathapp.ui.UserActivity
import com.uniolco.weathapp.ui.base.SharedViewModel

const val LOG_BUTTON = "LOG_BUTTON"
const val INFO_BUTTON = "Info"

class SettingsFragment: PreferenceFragmentCompat() {

    private val model: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.settingsTitle)
        val logButton = findPreference<Preference>(LOG_BUTTON)!!
        val userButton = findPreference<Preference>(INFO_BUTTON)!!
        var subtitle = " "
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        model.loggedIn.observe(viewLifecycleOwner, Observer { item ->
            if (item == true){
                logButton.title = getString(R.string.settingsLogIn)
            }
            else{
                logButton.title = getString(R.string.settingsLogOut)
            }
            if(item == null)
                return@Observer
            logButton.onPreferenceClickListener = object: Preference.OnPreferenceClickListener{
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    if(item == false){
                        val intent = Intent(logButton.context, LoginActivity::class.java)
                        startActivity(intent)
                        model.ifFromSettings.postValue(true)
                    } else{
                        try {
                            FirebaseAuth.getInstance().signOut()
                            model.loggedIn.postValue(false)
                            with(sharedPreferences.edit()){
                                putBoolean("UserNotNull", false)
                                apply()
                            }
                            Toast.makeText(logButton.context,getString(R.string.settingsToastSignedOut), Toast.LENGTH_SHORT).show()
                        } catch (e: Exception){
                            Log.e("ERROR", "ERROR WITH LOGGING OUT: ${e}")
                        }
                    }
                    return true
                }
            }
        })
        if(model.loggedIn.value == true){
            subtitle = getString(R.string.settingsGreeting, model.email.value)
        }
        else{
            subtitle = getString(R.string.settingsNotAuthorised)
        }
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = subtitle

        val infoPref: Preference? = findPreference("Info")
/*        val setO = sharedPreferences.getStringSet("userSet", setOf())
        if (!setO.isNullOrEmpty()){
            infoPref?.summary = "${setO?.elementAt(1)}, ${setO?.elementAt(2)}, ${setO?.elementAt(3)}"
        }
        else{
            Log.d("FDDFDFDPDSFJKDGFS{GKR", "EMPTY")
        }*/

        userButton.onPreferenceClickListener = object: Preference.OnPreferenceClickListener{
            override fun onPreferenceClick(preference: Preference?): Boolean {
                val intent = Intent(context, UserActivity::class.java)
                startActivity(intent)
                return true
            }

        }

    }
}