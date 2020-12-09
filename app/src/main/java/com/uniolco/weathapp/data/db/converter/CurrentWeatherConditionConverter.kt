package com.uniolco.weathapp.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uniolco.weathapp.data.db.entity.current.Condition

class CurrentWeatherConditionConverter {

    @TypeConverter
    fun fromCondition(value: Condition): String {
        val gson = Gson()
        val type = object : TypeToken<List<Condition>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCondition(value: String): Condition {
        val gson = Gson()
        val type = object : TypeToken<List<Condition>>() {}.type
        return gson.fromJson(value, type)
    }
}