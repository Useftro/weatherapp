package com.uniolco.weathapp.data.db

import android.content.Context
import androidx.room.*
import com.uniolco.weathapp.data.db.converter.CurrentWeatherConverter
import com.uniolco.weathapp.data.db.converter.DailyConverter
import com.uniolco.weathapp.data.db.converter.HourlyConverter
import com.uniolco.weathapp.data.db.converter.OneCallWeatherConverter
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.OneCallServerResponse

@Database(
    entities = [CurrentWeatherResponse::class, OneCallServerResponse::class],
    version = 4,
//    exportSchema = false // check some info about this
)
@TypeConverters(CurrentWeatherConverter::class, DailyConverter::class,
                HourlyConverter::class, OneCallWeatherConverter::class)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun oneCallWeatherDao(): OneCallWeatherDao

    // database should be a singleton
    companion object{
        @Volatile private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){ // checking if instance is initialized
            instance ?: buildDatabase(context).also { instance = it } // whatever returned, instance is equal to it
        }


        // room has no lifecycle
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, // context of all application not a fragment or anything else
                ForecastDatabase::class.java, "forecast.db").fallbackToDestructiveMigration().build()

    }
}