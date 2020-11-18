package com.uniolco.weathapp.data.db.converter

import android.util.Log
import androidx.room.TypeConverter
import com.uniolco.weathapp.data.db.entity.Weather

class WeatherConverter {
    @TypeConverter
    fun toWeather(data: String): List<Weather>{
        val a = data.split(" ")
        val listWeather: MutableList<Weather> = mutableListOf()
        for(i in a.indices step 4){
            listWeather.add(Weather(a[i], a[i+1], a[i+2].toInt(), a[i+3]))
        }
        Log.d("ConvertToWeather", listWeather.toString())
        return listWeather.toList()
    }

    @TypeConverter
    fun fromWeather(weatherList: List<Weather>): String{
        val data: String = ""
        for(i in weatherList){
            data + i.description + i.icon + i.id.toString() + i.main
        }
        Log.d("ConvertToString", data)
        return data
    }
}