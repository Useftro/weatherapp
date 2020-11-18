package com.uniolco.weathapp.data.db.entity


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName


data class Weather(
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "identification")
    val id: Int,
    @ColumnInfo(name = "mai")
    val main: String
) {
    constructor() : this("", "", 0, "")
}