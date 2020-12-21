package com.uniolco.weathapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uniolco.weathapp.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordEditText
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            login()
        }

        contWoLogButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Logged", false)
            startActivity(intent)
            finish()
        }

    }

    private fun login() {
        if (emailloginEditText.text.isEmpty() || passwordEditText.text.isEmpty()){
            error("Please fill all fields!")
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailloginEditText.text.toString()).matches()){
            error("Please enter valid email!")
            return
        }

        auth.signInWithEmailAndPassword(emailloginEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("QWERTY", user?.uid.toString())
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Logged", true)
            intent.putExtra("Name", currentUser.displayName)
            intent.putExtra("Email", currentUser.email)
            intent.putExtra("Phone", currentUser.phoneNumber)
            startActivity(intent)
            finish()
        }
    }
}