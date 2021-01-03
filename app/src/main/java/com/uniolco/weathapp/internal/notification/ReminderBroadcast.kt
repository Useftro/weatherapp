package com.uniolco.weathapp.internal.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.uniolco.weathapp.R

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, "notify")
            .setSmallIcon(R.drawable.ic_weather_sunny)
            .setContentTitle("Bruh")
            .setContentText("Mhm...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }
}