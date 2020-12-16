package com.uniolco.weathapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.uniolco.weathapp.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptorImpl(context: Context): ConnectivityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline()){
            throw NoConnectivityException() //
        }
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean { // Checking if our device has Internet connection due to wifi, cellular, vpn
        var result: Boolean = false
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        connectivityManager.run {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                connectivityManager.run {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                        if(hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                            result = true
                        }
                        else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                            result = true
                        }
                        else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                            result = true
                        }
                    }
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI){
                            result = true
                        }
                        else if(type == ConnectivityManager.TYPE_MOBILE){
                            result = true
                        }
                        else if(type == ConnectivityManager.TYPE_VPN){
                            result = true
                        }
                    }
                }
            }
        }
        return result
    }
}