package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Minutely(
    @ColumnInfo(name = "dt")
    val dt: Double,
    @ColumnInfo(name = "precipitation")
    val precipitation: Double
)