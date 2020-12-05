package com.uniolco.weathapp.data.db.entity.onecall


import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Daily(
    @ColumnInfo(name = "clouds")
    val clouds: Double,
    @SerializedName("dew_point")
    @ColumnInfo(name = "dew_point")
    val dewPoint: Double,
    @ColumnInfo(name = "dt")
    val dt: Int,
    @SerializedName("feels_like")
    @Embedded(prefix = "feelslike_")
    val feelsLike: FeelsLike,
    @ColumnInfo(name = "humidity")
    val humidity: Double,
    @ColumnInfo(name = "pop")
    val pop: Double,
    @ColumnInfo(name = "pressure")
    val pressure: Int,
    @ColumnInfo(name = "rain")
    val rain: Double,
    @ColumnInfo(name = "sunrise")
    val sunrise: Int,
    @ColumnInfo(name = "sunset")
    val sunset: Int,
    @Embedded(prefix = "temp_")
    val temp: Temp,
    @ColumnInfo(name = "uvi")
    val uvi: Double,
    @ColumnInfo(name = "weather")
    val weather: List<Weather>,
    @ColumnInfo(name = "wind_deg")
    @SerializedName("wind_deg")
    val windDeg: Int,
    @ColumnInfo(name = "wind_speed")
    @SerializedName("wind_speed")
    val windSpeed: Double
)