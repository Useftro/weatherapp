package com.uniolco.weathapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.firebase.User

class SharedViewModel : ViewModel() {
    val loggedIn = MutableLiveData<Boolean>()
    val personInfo = MutableLiveData<User>()
    val registered = MutableLiveData<Boolean>()
    val email = MutableLiveData<String>()
    val user = MutableLiveData<User>()
    val dataReady = MutableLiveData<Boolean>()

    val ifFromSettings = MutableLiveData<Boolean>()

    fun select(item: Boolean) {
        loggedIn.value = item
    }

    fun selectPersonInfo(item: User){
        personInfo.value = item
    }
}