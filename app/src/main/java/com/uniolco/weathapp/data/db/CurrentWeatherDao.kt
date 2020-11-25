package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uniolco.weathapp.data.db.converter.WeatherConverter
import com.uniolco.weathapp.data.db.unitlocalized.ImperialCurrentWeather
import com.uniolco.weathapp.data.db.unitlocalized.MetricCurrentWeather
import com.uniolco.weathapp.data.network.response.CURRENT_WEATHER_ID
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse


@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE because when updating we will always have a conflict
    // because of the same id
    fun insertOrUpdate(currentWeather: CurrentWeatherResponse)

    @Query("SELECT * FROM current_weather")
    fun getWeatherMetric(): LiveData<CurrentWeatherResponse> //LiveData is like observer who notifies subscribers that data has been changed
//
//
//    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
//    fun getWeatherMImperial(): LiveData<ImperialCurrentWeather>

}