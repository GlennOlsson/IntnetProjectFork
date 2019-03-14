package com.example.intproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color

class NotificationSingleton constructor(context: Context) {

    var notificationManager: NotificationManager? = null
    val channelId = "com.example.intproject.notification"
    var context: Context? = null
    var notificationId: Int = 0

    init {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        this.context = context
        createNotificationChannel(
            channelId,
            "Profile Events",
            "Changes on your profile")
    }

    companion object {
        @Volatile
        private var INSTANCE: NotificationSingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationSingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }

    fun sendNotification(title: String, content: String) {
        val notification = Notification.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelId)
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    /*
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToQueue(req: Request<T>) {
        requestQueue.add(req)
    }
    */
}