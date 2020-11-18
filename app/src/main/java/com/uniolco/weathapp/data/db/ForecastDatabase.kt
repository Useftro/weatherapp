package com.uniolco.weathapp.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import com.uniolco.weathapp.data.db.converter.WeatherConverter
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse

@Database(
    entities = [CurrentWeatherResponse::class],
    version = 1,
//    exportSchema = false // check some info about this
)
@TypeConverters(WeatherConverter::class)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    // database should be a singleton
    companion object{
        @Volatile private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){ // checking if instance is initialized
            instance ?: buildDatabase(context).also { instance = it } // whatever returned, instance is equal to it
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, // context of all application not a fragment or anything else
                ForecastDatabase::class.java, "forecast.db").addMigrations().build()

    }
}