package com.uniolco.weathapp.internal

import retrofit2.http.HTTP
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

class NoConnectivityException: IOException()

class LocationPermissionNotGrantedException: Exception()

class DateNotFoundException: Exception()

class CityNotFound: RuntimeException()