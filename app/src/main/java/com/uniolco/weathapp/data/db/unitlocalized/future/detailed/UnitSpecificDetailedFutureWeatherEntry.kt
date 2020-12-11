package com.uniolco.weathapp.data.db.unitlocalized.future.detailed

import org.threeten.bp.LocalDate

interface UnitSpecificDetailedFutureWeatherEntry {
    val date: LocalDate
    val avghumidity: Double
    val avgtempC: Double
    val avgvisKm: Double
    val dailyChanceOfRain: String
    val maxtempC: Double
    val mintempC: Double
    val sunrise: String
    val sunset: String
    val conditionIcon: String
    val maxWindSpeed: Double
}