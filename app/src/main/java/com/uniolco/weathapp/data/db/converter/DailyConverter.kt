package com.uniolco.weathapp.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniolco.weathapp.data.db.entity.onecall.Daily

class DailyConverter {
    @TypeConverter
    fun fromDaily(value: List<Daily>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Daily>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toDaily(value: String): List<Daily> {
        val gson = Gson()
        val type = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(value, type)
    }
}