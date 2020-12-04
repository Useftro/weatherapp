package com.uniolco.weathapp.data.db.entity.onecall


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)