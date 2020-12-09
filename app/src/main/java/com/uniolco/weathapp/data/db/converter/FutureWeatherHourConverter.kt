package com.uniolco.weathapp.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniolco.weathapp.data.db.entity.forecast.Hour

class FutureWeatherHourConverter {
    @TypeConverter
    fun fromHour(value: List<Hour>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hour>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toHour(value: String): List<Hour> {
        val gson = Gson()
        val type = object : TypeToken<String>() {}.type
        return gson.fromJson(value, type)
    }
}