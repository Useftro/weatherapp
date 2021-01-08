package com.uniolco.weathapp.internal

import android.util.Log
import com.uniolco.weathapp.R

val SNOWSTORM_CODES = listOf<Int>(1117, 1222, 1224)
val SUNNY_CODES = listOf<Int>(1000)
val CLOUDY_CODES = listOf<Int>(1003, 1006)
val OVERCAST_CODES = listOf<Int>(1009)
val THUNDER_CODES = listOf<Int>(1087,1273,1276,1279,1282)
val SNOW_CODES = listOf<Int>(1066,1069,1114,1210,1213,1216,1219, 1225,1237,1255,1258)
val MIST_CODES = listOf<Int>(1030,1135,1147)
val RAIN_CODES = listOf<Int>(1063, 1072, 1153, 1168,1178,1180,1183,1186,1189,1192,1195,1198,1201,1204,1207,1240,1243,1246,1249,1252,1261, 1264)


fun background(code: Int): Int{

    var drawable: Int = 0
    when(code){
        in SNOWSTORM_CODES -> drawable = R.drawable.snowstorm
        in SUNNY_CODES -> drawable = R.drawable.sunny
        in CLOUDY_CODES -> drawable = R.drawable.cloudy
        in OVERCAST_CODES -> drawable = R.drawable.overcast
        in THUNDER_CODES -> drawable = R.drawable.thunder
        in SNOW_CODES -> drawable = R.drawable.snow
        in MIST_CODES -> drawable = R.drawable.mist
        in RAIN_CODES -> drawable = R.drawable.rain
        else -> Log.e("ABCDERFEF", "Wrong code...")
    }
    return drawable
}

