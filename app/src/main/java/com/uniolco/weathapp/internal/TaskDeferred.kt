package com.uniolco.weathapp.internal

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

// creating it because of lastLocation in getLastDeviceLocation in LocationProvider that returns
// Task<Location> but we need to return Deferred<Coord>

fun <T> Task<T>.asDeferred(): Deferred<T>{
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }
    this.addOnFailureListener{ exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}