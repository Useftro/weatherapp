package com.uniolco.weathapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.firebase.User

class SharedViewModel : ViewModel() {
    val authorized = MutableLiveData<Boolean>()
    val personInfo = MutableLiveData<User>()
    val registered = MutableLiveData<Boolean>()
    val email = MutableLiveData<String>()

    fun select(item: Boolean) {
        authorized.value = item
    }

    fun selectPersonInfo(item: User){
        personInfo.value = item
    }
}