package com.uniolco.weathapp.data.db.entity


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Coord(
    @ColumnInfo(name = "latitude")
    val lat: Double,
    @ColumnInfo(name = "longitude")
    val lon: Double
) {
    constructor() : this(0.0, 0.0)
}