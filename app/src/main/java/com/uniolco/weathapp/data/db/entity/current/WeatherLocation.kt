package com.uniolco.weathapp.data.db.entity.current


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val WEATHER_LOCATION_ID = 0

@Entity(tableName = "weather_location")
data class WeatherLocation(
    val country: String = "",
    val lat: Double = 0.0,
    val localtime: String = "",
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long = 0,
    val lon: Double = 0.0,
    val name: String = "",
    val region: String = "",
    @SerializedName("tz_id")
    val tzId: String = "",
){
    @PrimaryKey(autoGenerate = false)
    var id_w = WEATHER_LOCATION_ID

    val zonedDateTime: ZonedDateTime
        get(){
            val instant = Instant.ofEpochSecond(localtimeEpoch)
            val zoneId = ZoneId.of(tzId)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}