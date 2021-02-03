package com.uniolco.weathapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.firebase.User
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse

class SharedViewModel : ViewModel() {
    val loggedIn = MutableLiveData<Boolean>()
    val personInfo = MutableLiveData<User>()
    val registered = MutableLiveData<Boolean>()
    val email = MutableLiveData<String>()
    val user = MutableLiveData<User>()
    val firebaseUser = MutableLiveData<User>()
    val uid = MutableLiveData<String>()
    val locationName = MutableLiveData<String>()

    val forJson = MutableLiveData<String>("")
    val forJsonWeather = MutableLiveData<CurrentWeather>()
    val forJsonLocation = MutableLiveData<WeatherLocation>()

    val writtenLoc = MutableLiveData<Boolean>(false)
    val writtenWea = MutableLiveData<Boolean>(false)

    val ifFromSettings = MutableLiveData<Boolean>()

    fun select(item: Boolean) {
        loggedIn.value = item
    }

    fun selectPersonInfo(item: User){
        personInfo.value = item
    }
}