package com.uniolco.weathapp.data.db.entity.favorite

import androidx.room.*
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation

@Entity(tableName = "favorite_locations", indices = [Index(value = ["name"], unique = true)])
data class Locations(
    @PrimaryKey(autoGenerate = true)
    val id_loc: Int = 0,
    @Embedded
    val location: WeatherLocation
)
