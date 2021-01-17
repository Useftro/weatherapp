package com.uniolco.weathapp.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniolco.weathapp.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        with(sharedPreferences.edit()){
            putBoolean("Passed", true)
            apply()
        }

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        loginButton.setOnClickListener {
            login()
            with(sharedPreferences.edit()){
                putBoolean("UserNotNull", true)
                apply()
            }
        }


        contWoLogButton.setOnClickListener {
            if(!sharedPreferences.getBoolean("askedPermissions", false)){
                startActivity(Intent(this, WaitingActivity::class.java))
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                with(sharedPreferences.edit()){
                    putBoolean("Logged", false)
                    putBoolean("registered", false)
                    apply()
                }
                startActivity(intent)
                finish()
            }
        }

    }

    private fun login() {
        if (emailloginEditText.text.isEmpty() || passwordEditText.text.isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailloginEditText.text.toString()).matches()){
            Toast.makeText(this, "Please enter valid email!", Toast.LENGTH_SHORT).show()
        }
        try {
            auth.signInWithEmailAndPassword(
                emailloginEditText.text.toString(),
                passwordEditText.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }
        catch(e: java.lang.IllegalArgumentException){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if(currentUser != null){
            lateinit var inten: Intent
            if(!sharedPreferences.getBoolean("askedPermissions", false)) {
                inten = Intent(this, WaitingActivity::class.java)
            }
            else{
                inten = Intent(this, MainActivity::class.java)
            }
            with(sharedPreferences.edit()){
                putBoolean("Logged", true)
                putBoolean("registered", intent.getBooleanExtra("registered", false))
                putString("Email", currentUser.email)
                apply()
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

/*            inten.putExtra("login", intent.getStringExtra("login"))
            inten.putExtra("email", intent.getStringExtra("email"))
            inten.putExtra("phone", intent.getStringExtra("phone"))
            inten.putExtra("name", intent.getStringExtra("name"))
            inten.putExtra("surname", intent.getStringExtra("surname"))
            inten.putExtra("address", intent.getStringExtra("address"))



            inten.putExtra("registered", intent.getBooleanExtra("registered", false))*/
            startActivity(inten)
            finish()
        }
    }
}