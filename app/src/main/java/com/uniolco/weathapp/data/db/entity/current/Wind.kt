package com.uniolco.weathapp.data.db.entity.current


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Wind(
    @ColumnInfo(name = "deg")
    val deg: Int, // direction mertologic
    @ColumnInfo(name = "speed")
    val speed: Double
) {
    constructor() : this(0, 0.0)
}