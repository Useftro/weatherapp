package com.uniolco.weathapp.data.db.unitlocalized.future.detailed

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricSpecificDetailedFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avghumidity")
    override val avghumidity: Double,
    @ColumnInfo(name = "avgtempC")
    override val avgtempC: Double,
    @ColumnInfo(name = "avgvisKm")
    override val avgvisKm: Double,
    @ColumnInfo(name = "dailyChanceOfRain")
    override val dailyChanceOfRain: String,
    @ColumnInfo(name = "maxtempC")
    override val maxtempC: Double,
    @ColumnInfo(name = "mintempC")
    override val mintempC: Double,
    @ColumnInfo(name = "sunrise")
    override val sunrise: String,
    @ColumnInfo(name = "sunset")
    override val sunset: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIcon: String,
    @ColumnInfo(name = "maxwindKph")
    override val maxWindSpeed: Double,
    @ColumnInfo(name = "condition_code")
    override val conditionCode: Int
) : UnitSpecificDetailedFutureWeatherEntry {
}