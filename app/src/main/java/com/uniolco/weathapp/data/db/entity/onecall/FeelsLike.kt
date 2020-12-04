package com.uniolco.weathapp.data.db.entity.onecall


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    val day: Int,
    val eve: Double,
    val morn: Double,
    val night: Double
)