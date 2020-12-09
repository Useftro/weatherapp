package com.uniolco.weathapp.data.db.unitlocalized.future

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricSimpleFutureWeatherEntry (
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val averageTemp: Double,
    @ColumnInfo(name = "avghumidity")
    override val avgHumidity: Double,
    @ColumnInfo(name = "dailyChanceOfRain")
    override val dailyChanceOfRain: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIcon: String,
    @ColumnInfo(name = "maxwindKph")
    override val maxWindSpeed: Double
) : UnitSpecificSimpleFutureWeatherEntry