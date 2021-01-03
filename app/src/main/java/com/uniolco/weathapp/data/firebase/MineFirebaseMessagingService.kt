package com.uniolco.weathapp.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MineFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        sendRegistrationToServer(p0)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("RESISSS", "sendRegistrationTokenToServer($token)")
    }
}