package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather


@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE because when updating we will always have a conflict
    // because of the same id
    fun insertOrUpdate(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather")
    fun getWeatherMetric(): LiveData<CurrentWeather> //LiveData is like observer who notifies subscribers that data has been changed

//
//
//    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
//    fun getWeatherMImperial(): LiveData<ImperialCurrentWeather>

}