package com.uniolco.weathapp.data.network.response


import androidx.room.*
import com.uniolco.weathapp.data.db.converter.WeatherConverter
import com.uniolco.weathapp.data.db.entity.*

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
    @ColumnInfo(name = "base")
    val base: String,
    @Embedded(prefix = "clouds_") // prefix means: clouds_all
    val clouds: Clouds,
    @ColumnInfo(name = "cod")
    val cod: Int,
    @Embedded(prefix = "coord_") // prefix means coord_lat, coord_lon
    val coord: Coord,
    @ColumnInfo(name = "dt")
    val dt: Int,
    @ColumnInfo(name = "identification_key")
    val id: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    @ColumnInfo(name = "name")
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    @ColumnInfo(name = "visibility")
    val visibility: Int,
    @ColumnInfo(name = "weather")
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind
){
    @PrimaryKey(autoGenerate = false) // const because only one current weather can be
    var idKey: Int = CURRENT_WEATHER_ID

//    constructor(): this(
//        "",
//        Clouds(0),
//        0,
//        Coord(0.0, 0.0),
//        0,
//        0,
//        Main(0, 0, 0.0, 0.0, 0.0, 0.0),
//        "",
//        Sys("", 0, 0.0, 0, 0, 0),
//        0,
//        listOf(Weather("", "", 0, "")),
//        Wind(0, 0.0))
}