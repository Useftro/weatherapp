package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    @ColumnInfo(name = "h")
    val h: Double
)