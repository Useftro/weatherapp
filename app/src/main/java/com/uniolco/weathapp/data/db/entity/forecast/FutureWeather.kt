package com.uniolco.weathapp.data.db.entity.forecast


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "future_weather", indices = [Index(value = ["date"], unique = true)])
// When we want to have only one future weather, not many of them
data class FutureWeather(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @Embedded
    val astro: Astro,
    val date: String,
    @Embedded
    val day: Day,
    val hour: List<Hour>
)