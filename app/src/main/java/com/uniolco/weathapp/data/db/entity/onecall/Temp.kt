package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Temp(
    @ColumnInfo(name = "day")
    val day: Double,
    @ColumnInfo(name = "eve")
    val eve: Double,
    @ColumnInfo(name = "max")
    val max: Double,
    @ColumnInfo(name = "min")
    val min: Double,
    @ColumnInfo(name = "morn")
    val morn: Double,
    @ColumnInfo(name = "night")
    val night: Double
)