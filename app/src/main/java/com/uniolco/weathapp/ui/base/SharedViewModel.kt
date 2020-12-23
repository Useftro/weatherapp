package com.uniolco.weathapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Boolean>()

    fun select(item: Boolean) {
        selected.value = item
    }
}