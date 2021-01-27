package com.uniolco.weathapp.data.db.entity.favorite

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse


@Entity(tableName = "favorite_weather", indices = [Index(value = ["weather_location_name"], unique = true)])
data class FavoriteEntry(
    @PrimaryKey(autoGenerate = true)
    val id_: Int = 0,
    @Embedded(prefix = "weather_")
    val weather: CurrentWeatherResponse
)
