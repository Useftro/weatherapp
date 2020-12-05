package com.uniolco.weathapp.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniolco.weathapp.data.db.entity.onecall.Minutely

class MinutelyConverter {
    @TypeConverter
    fun fromWeather(value: List<Minutely>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Minutely>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeather(value: String): List<Minutely> {
        val gson = Gson()
        val type = object : TypeToken<List<Minutely>>() {}.type
        return gson.fromJson(value, type)
    }
}