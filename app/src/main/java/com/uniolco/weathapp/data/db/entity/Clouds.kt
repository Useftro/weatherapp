package com.uniolco.weathapp.data.db.entity


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Clouds(
    @ColumnInfo(name = "all")
    val all: Int // percentage
) {
    constructor() : this(0)
}