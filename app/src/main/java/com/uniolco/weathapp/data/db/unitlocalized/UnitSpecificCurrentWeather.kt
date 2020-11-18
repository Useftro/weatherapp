package com.uniolco.weathapp.data.db.unitlocalized

interface UnitSpecificCurrentWeather {

    val temperature: Double
    val temperatureMax: Double
    val temperatureMin: Double
    val temperatureFeelsLike: Double
    val sunrise: Double
    val sunset: Double
    val pressure: Int
    val windSpeed: Double

}