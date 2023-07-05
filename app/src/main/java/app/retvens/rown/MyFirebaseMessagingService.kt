package app.retvens.rown

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import app.retvens.rown.ApiRequest.MesiboBackgroundService
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.api.MesiboCall
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")

const val channelId = "Notification_Channel"
const val channelName = "app.retvens.rown"
class MyFirebaseMessagingServices : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")

        // Save the token to your backend server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            MesiboCall.getInstance().init(applicationContext)
        }
//        val serviceIntent = Intent(this, MesiboBackgroundService::class.java)
//        startService(serviceIntent)
        MesiboCall.getInstance().init(applicationContext)
        Log.e("received","success")

        createNotificationChannel()
    }

    private fun createNotificationChannel() {

            val channelId = "my_channel_id"

// Create the notification channel only if the device is running Android Oreo (API 26) or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "My Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT

                val channel = NotificationChannel(channelId, channelName, importance)
                channel.description = "My Channel Description"

                // Add additional configuration to the channel if needed, such as sound or vibration

                // Get the system notification manager
                val notificationManager = getSystemService(NotificationManager::class.java)

                // Create the notification channel
                notificationManager.createNotificationChannel(channel)
            }

    }

//    @SuppressLint("RemoteViewLayout")
//    fun getRemoteView(title: String, message: String): RemoteViews {
//        val remoteViews = RemoteViews("app.retvens.rown", R.layout.notification)
//        remoteViews.setTextViewText(R.id.titlenotify, title)
//        remoteViews.setTextViewText(R.id.description, message)
//        remoteViews.setImageViewResource(R.id.img, R.drawable.png_r_logo)
//
//        return remoteViews
//    }


}
