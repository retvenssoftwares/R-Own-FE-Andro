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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.MesiboBackgroundService
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.home.postDetails.CheckInDetailsActivity
import app.retvens.rown.NavigationFragments.home.postDetails.PostDetailsActivityNotification
import app.retvens.rown.NavigationFragments.home.postDetails.StatusDetailsActivity
import app.retvens.rown.NavigationFragments.profile.viewConnections.ViewConnectionsActivity
import app.retvens.rown.NavigationFragments.profile.viewRequests.ViewRequestsActivity
import app.retvens.rown.api.MesiboCall
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

const val channelId = "Notification_Channel"
const val channelName = "app.retvens.rown"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var title: String
    private lateinit var body: String
    private lateinit var type:String
    private lateinit var address:String
    private lateinit var postId:String
    private lateinit var postType:String

    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {

        Log.e("message",message.toString())


//            MesiboCall.getInstance().init(applicationContext)
            title = message.data["title"].toString()
            body = message.data["body"].toString()
            address = message.data["address"].toString()
            type = message.data["type"].toString()
            postId = message.data["post_id"].toString()
            postType = message.data["postType"].toString()

            Log.e("type",type.toString())
             Log.e("post",postType.toString())
        Log.e("podtod",postId.toString())

        Log.e("received", "success")

        createNotification()


        if (type == "call"){
            try {
                MesiboApi.init(applicationContext)
                MesiboApi.startMesibo(true)
                MesiboCall.getInstance().init(applicationContext)
            } catch (e: Exception) {
                Log.e(TAG, "MesiboCall initialization failed: ${e.message}")
            }
        }

        super.onMessageReceived(message);
    }

    private fun createNotification() {
        val notificationId = 1 // Assign a unique notification id

        // Create the notification builder
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.r_own_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority for heads-up notification
            .setDefaults(Notification.DEFAULT_ALL) // Add default notification behaviors (sound, vibration, etc.)

        // Create the full-screen intent

        if (type == "message"){
            MesiboApi.init(applicationContext)
            MesiboApi.startMesibo(true)
            val fullScreenIntent = Intent(this, MesiboMessagingActivity::class.java)
            fullScreenIntent.putExtra(MesiboUI.PEER,address)
            fullScreenIntent.putExtra("page","2")
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            notificationBuilder.setContentIntent(fullScreenPendingIntent)

        }else{

            if (postType == "share some media"){
                val fullScreenIntent = Intent(this, PostDetailsActivityNotification::class.java)
                fullScreenIntent.putExtra("postId",postId)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                notificationBuilder.setContentIntent(fullScreenPendingIntent)
            }else if (postType == "Check-in"){
                val fullScreenIntent = Intent(this, CheckInDetailsActivity::class.java)
                fullScreenIntent.putExtra("postId",postId)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                notificationBuilder.setContentIntent(fullScreenPendingIntent)
            }else if (postType == "normal status"){
                val fullScreenIntent = Intent(this, StatusDetailsActivity::class.java)
                fullScreenIntent.putExtra("postId",postId)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                notificationBuilder.setContentIntent(fullScreenPendingIntent)
            }else if (type == "request"){
                val fullScreenIntent = Intent(this, ViewRequestsActivity::class.java)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                notificationBuilder.setContentIntent(fullScreenPendingIntent)
            }else if (type == "connection"){
                val fullScreenIntent = Intent(this, ViewConnectionsActivity::class.java)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                notificationBuilder.setContentIntent(fullScreenPendingIntent)
            }

        }


//            val fullScreenIntent = Intent(this, DashBoardActivity::class.java)
//            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//            notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true)
        // Set the full-screen intent


        // Build the notification
        val notification = notificationBuilder.build()

        // Get the system notification manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel only if the device is running Android Oreo (API 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "My Channel Description"

            // Add additional configuration to the channel if needed, such as sound or vibration

            // Create the notification channel
            notificationManager.createNotificationChannel(channel)
        }

        // Show the notification as a popup (heads-up notification)
        notificationManager.notify(notificationId, notification)
    }
}