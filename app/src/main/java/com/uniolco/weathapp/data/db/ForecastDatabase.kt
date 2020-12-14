package com.uniolco.weathapp.data.db

import android.content.Context
import androidx.room.*
import com.uniolco.weathapp.data.db.converter.*
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.db.entity.favorite.FavoriteEntry
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.db.entity.forecast.FutureWeather

@Database(
    entities = [CurrentWeather::class, WeatherLocation::class, FutureWeather::class,
               FavoriteEntry::class, Locations::class],
    version = 17,
//    exportSchema = false // check some info about this
)
//@TypeConverters(CurrentWeatherConditionConverter::class)
@TypeConverters(LocalDateConverter::class, FutureWeatherHourConverter::class)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun favoriteWeatherDao(): FavoriteWeatherDao

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
                ForecastDatabase::class.java, "forecastContainer.db").fallbackToDestructiveMigration().build()

    }
}