package com.uniolco.weathapp.data.network.response


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.uniolco.weathapp.data.db.entity.onecall.Current
import com.uniolco.weathapp.data.db.entity.onecall.Daily
import com.uniolco.weathapp.data.db.entity.onecall.Hourly

const val ONECALL_WEATHER_ID = 0

@Entity(tableName = "all_weather")
data class OneCallServerResponse(
    @Embedded(prefix = "current_")
    val current: Current,
    @ColumnInfo(name = "daily")
    val daily: List<Daily>,
    @ColumnInfo(name = "hourly")
    val hourly: List<Hourly>,
    @ColumnInfo(name = "latit")
    val lat: Double,
    @ColumnInfo(name = "longi")
    val lon: Double,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    @ColumnInfo(name = "timezone_offset")
    val timezoneOffset: Int
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = ONECALL_WEATHER_ID
}