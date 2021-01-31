package com.uniolco.weathapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniolco.weathapp.R
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }


    public override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener)
            .withData(if (intent != null) intent.data else null).init()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        Handler().postDelayed({
            updateUI(currentUser)
            finish()
        }, 2000)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit()
    }

    private val branchReferralInitListener =
        BranchReferralInitListener { linkProperties, error ->
            // do stuff with deep link data (nav to page, display content, etc)
            Log.d("DEEPLINK", "DEEP LINK IS ALIVE")
        }

    private fun updateUI(currentUser: FirebaseUser?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if(currentUser != null){
            val inten = Intent(this, MainActivity::class.java)
            inten.putExtra("Logged", true)
            inten.putExtra("Email", currentUser.email)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

/*            with(sharedPreferences.edit()){
                putString("Login", currentUser.)
            }*/

            inten.putExtra("login", intent.getStringExtra("login"))
            inten.putExtra("email", intent.getStringExtra("email"))
            inten.putExtra("phone", intent.getStringExtra("phone"))
            inten.putExtra("name", intent.getStringExtra("name"))
            inten.putExtra("surname", intent.getStringExtra("surname"))
            inten.putExtra("address", intent.getStringExtra("address"))



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