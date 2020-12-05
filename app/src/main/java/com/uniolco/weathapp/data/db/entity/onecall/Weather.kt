package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Weather(
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "id")
    val id: Double,
    @ColumnInfo(name = "main")
    val main: String
)