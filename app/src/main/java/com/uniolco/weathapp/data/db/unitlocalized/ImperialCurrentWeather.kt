package com.uniolco.weathapp.data.db.unitlocalized

import androidx.room.ColumnInfo

data class ImperialCurrentWeather(
    @ColumnInfo(name = "main_temp")
    override val temperature: Double,
    @ColumnInfo(name = "main_tempMax")
    override val temperatureMax: Double,
    @ColumnInfo(name = "main_tempMin")
    override val temperatureMin: Double,
    @ColumnInfo(name = "main_feelsLike")
    override val temperatureFeelsLike: Double,
    @ColumnInfo(name = "sys_sunrise")
    override val sunrise: Double,
    @ColumnInfo(name = "sys_sunset")
    override val sunset: Double,
    @ColumnInfo(name = "main_pressure")
    override val pressure: Int,
    @ColumnInfo(name = "wind_speed")
    override val windSpeed: Double

): UnitSpecificCurrentWeather