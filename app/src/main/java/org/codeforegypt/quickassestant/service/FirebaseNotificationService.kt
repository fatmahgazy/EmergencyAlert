package org.codeforegypt.quickassestant.service

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("EmergencyNotification", "Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val header = "Emergency"
        Log.d("EmergencyNotification", "Body: init")
        val body = message.data["body"] ?: "Help me please!"
        Log.d("EmergencyNotification", "Body: $body")
        EmergencyNotification.show(header, body, notificationManager, this)
    }
}