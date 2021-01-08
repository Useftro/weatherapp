package com.uniolco.weathapp.data.db.entity.current


import com.google.gson.annotations.SerializedName

data class Condition(
    val code: Int = 0,
    val icon: String = "",
    val text: String = ""
)