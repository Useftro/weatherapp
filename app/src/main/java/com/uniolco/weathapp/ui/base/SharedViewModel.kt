package com.uniolco.weathapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.firebase.User

class SharedViewModel : ViewModel() {
    val loggedIn = MutableLiveData<Boolean>() // to know if person logged as existing user or as unauthorized person
    val personInfo = MutableLiveData<User>() // user to save it to room
    val registered = MutableLiveData<Boolean>() // to know if user has been registered now
    val email = MutableLiveData<String>() // email from logging screen
    val firebaseUser = MutableLiveData<User>()
    val uid = MutableLiveData<String>() // user uid

    val ifFromSettings = MutableLiveData<Boolean>() // to know if we are logging or registering from settings fragment

    fun select(item: Boolean) {
        loggedIn.value = item
    }
}