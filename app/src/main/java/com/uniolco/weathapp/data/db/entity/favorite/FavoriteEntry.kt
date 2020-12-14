package com.uniolco.weathapp.data.db.entity.favorite

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather


@Entity(tableName = "favorite_weather")
data class FavoriteEntry(
    @PrimaryKey(autoGenerate = true)
    val id_: Int = 0,
    @Embedded
    val weather: CurrentWeather
)
