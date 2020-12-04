package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniolco.weathapp.data.network.response.OneCallServerResponse


@Dao
interface OneCallWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(oneCallCurrentWeather: OneCallServerResponse)

    @Query("SELECT * FROM all_weather")
    fun getData(): LiveData<OneCallServerResponse>
}