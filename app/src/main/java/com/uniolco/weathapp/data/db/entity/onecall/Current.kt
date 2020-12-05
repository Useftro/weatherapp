package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Current(
    @ColumnInfo(name = "clouds")
    val clouds: Double,
    @SerializedName("dew_point")
    @ColumnInfo(name = "dew_point")
    val dewPoint: Double,
    @ColumnInfo(name = "dt")
    val dt: Double,
    @SerializedName("feels_like")
    @ColumnInfo(name = "feels_like")
    val feelsLike: Double,
    @ColumnInfo(name = "humidity")
    val humidity: Double,
    @ColumnInfo(name = "pressure")
    val pressure: Double,
    @ColumnInfo(name = "sunrise")
    val sunrise: Double,
    @ColumnInfo(name = "sunset")
    val sunset: Double,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "uvi")
    val uvi: Double,
    @ColumnInfo(name = "visibility")
    val visibility: Double,
    @ColumnInfo(name = "weather")
    val weather: List<Weather>,
    @ColumnInfo(name = "wind_deg")
    @SerializedName("wind_deg")
    val windDeg: Double,
    @ColumnInfo(name = "wind_speed")
    @SerializedName("wind_speed")
    val windSpeed: Double
)