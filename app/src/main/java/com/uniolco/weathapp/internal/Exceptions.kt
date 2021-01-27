package com.uniolco.weathapp.internal

import java.io.IOException

class NoConnectivityException: IOException()

class LocationPermissionNotGrantedException: Exception()

class DateNotFoundException: Exception()

class CityNotFound: RuntimeException()