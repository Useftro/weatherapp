package com.uniolco.weathapp.data.db.entity.favorite

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation


@Entity(tableName = "favorite_weather")
data class FavoriteEntry(
    @PrimaryKey(autoGenerate = true)
    val id_: Int? = null,
    @Embedded
    val location: WeatherLocation,
    @Embedded
    val weather: CurrentWeather
)
