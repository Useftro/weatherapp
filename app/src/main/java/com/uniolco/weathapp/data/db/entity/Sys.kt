package com.uniolco.weathapp.data.db.entity

import androidx.room.ColumnInfo

data class Sys(
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "country_id")
    val id: Int,
    @ColumnInfo(name = "message")
    val message: Double,
    @ColumnInfo(name = "sunrise")
    val sunrise: Int,
    @ColumnInfo(name = "sunset")
    val sunset: Int,
    @ColumnInfo(name = "type")
    val type: Int
) {
    constructor() : this("", 0, 0.0, 0, 0, 0)
}