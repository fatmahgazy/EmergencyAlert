package org.codeforegypt.quickassestant.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.ui.MainActivity
import kotlin.random.Random

object EmergencyNotification {
    const val CHANNEL_ID = "Single_v1"

    fun show(title: String, body: String, notificationManager: NotificationManager, context: Context) {
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_emergency_24)
            .setContentIntent(getPendingIntent(context))
            .setSound(getNotificationSoundInstore())
            .setVibrate(getVibrationPatternInstore())
            .setLights(Color.BLUE, 3000, 3000)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body))
            .setAutoCancel(true)
            .setOngoing(false)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    private fun getVibrationPatternInstore() = longArrayOf(1000, 500, 100, 500, 1000)

    private fun getNotificationSoundInstore() = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    private fun getAudioAttributesInstore() = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()

    /*private fun getFullScreenPendingIntent(): PendingIntent {
        val fullScreenIntent = Intent(MetricsApp.getContext(), MainActivity::class.java)
        return PendingIntent.getActivity(
            MetricsApp.getContext(), 0,
            fullScreenIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )
    }*/

    private fun getPendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Single"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Get a specific notification that's send specially for you!"
            enableVibration(true)
            enableLights(true)
            lightColor = Color.BLUE
            setSound(getNotificationSoundInstore(), getAudioAttributesInstore())
            vibrationPattern = getVibrationPatternInstore()
        }
        notificationManager.createNotificationChannel(channel)
    }

}