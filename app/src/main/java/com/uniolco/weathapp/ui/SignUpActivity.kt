package com.uniolco.weathapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.firebase.User
import kotlinx.android.synthetic.main.activity_sign_up.*

private lateinit var auth: FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        button.setOnClickListener {
            signUp()
        }
    }


    private fun signUp(){
        if (loginEditText.text.isEmpty() || emailEditText.text.isEmpty() || phoneEditText.text.isEmpty()
            || passwordEditText.text.isEmpty() || nameEditText.text.isEmpty() || surnameEditText.text.isEmpty()
            || addressEditText.text.isEmpty()){
            error("Please fill all fields!")
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()){
            error("Please enter valid email!")
        }
        var registered = false
        auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")

                    val user = User(
                    loginEditText.text.toString(),
                    emailEditText.text.toString(),
                    phoneEditText.text.toString(),
                    nameEditText.text.toString(),
                    surnameEditText.text.toString(),
                    addressEditText.text.toString())
                    registered = true

                    FirebaseDatabase.getInstance().getReference("Users").child(
                        FirebaseAuth.getInstance().currentUser?.uid.toString()
                    ).setValue(user).addOnCompleteListener {
                        if(task.isSuccessful){
                            Toast.makeText(this, "Registration succeed", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("login", user.login)
                    intent.putExtra("email", user.email)
                    intent.putExtra("phone", user.phoneNumber)
                    intent.putExtra("name", user.name)
                    intent.putExtra("surname", user.surname)
                    intent.putExtra("address", user.address)
                    intent.putExtra("registered", registered)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Sign up failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }

    }
}