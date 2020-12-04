package com.uniolco.weathapp.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniolco.weathapp.data.db.entity.onecall.Hourly

class HourlyConverter {
    @TypeConverter
    fun fromWeather(value: List<Hourly>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hourly>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeather(value: String): List<Hourly> {
        val gson = Gson()
        val type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(value, type)
    }
}