package com.uniolco.weathapp.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.uniolco.weathapp.data.db.entity.current.Coord
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.internal.LocationPermissionNotGrantedException
import com.uniolco.weathapp.internal.asDeferred
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
): PreferenceProvider(context), LocationProvider {

    private val appContext: Context = context.applicationContext

    override suspend fun hasLocationChanged(
        lastWeatherLocation: Coord,
        currentWeatherResponse: CurrentWeatherResponse
    ): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException){
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(currentWeatherResponse)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: Coord): Boolean {
        if(!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.latitude) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.longitude) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: CurrentWeatherResponse): Boolean{
        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, "London")
    }


    private fun isUsingDeviceLocation(): Boolean{
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()){
            try{
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude} ${deviceLocation.longitude}"
            } catch(e: LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }
        else{
            return "${getCustomLocationName()}"
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?>{
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}