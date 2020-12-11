package com.uniolco.weathapp.data.db.unitlocalized.future.list

import org.threeten.bp.LocalDate

interface UnitSpecificSimpleFutureWeatherEntry {
    val date: LocalDate
    val averageTemp: Double
    val avgHumidity: Double
    val dailyChanceOfRain: String
    val conditionIcon: String
    val maxWindSpeed: Double
}