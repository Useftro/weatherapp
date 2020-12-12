package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniolco.weathapp.data.db.entity.favorite.FavoriteEntry

@Dao
interface FavoriteWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntry)

    @Query("SELECT * FROM favorite_weather")
    fun getAllFavorites(): LiveData<FavoriteEntry>
}