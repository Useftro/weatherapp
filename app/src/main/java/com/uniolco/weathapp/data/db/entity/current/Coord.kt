package com.uniolco.weathapp.data.db.entity.current


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Coord(
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this(0.0, 0.0)
}