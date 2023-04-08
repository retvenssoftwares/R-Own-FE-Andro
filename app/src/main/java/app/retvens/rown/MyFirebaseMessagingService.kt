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
import androidx.media.app.NotificationCompat
import app.retvens.rown.ChatSection.ChatScreen
import app.retvens.rown.ChatSection.MesiboUsers
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")

const val channelId = "Notification_Channel"
const val channelName = "app.retvens.rown"

class MyFirebaseMessagingService:FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Save the token to your backend server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null){
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, Message:String):RemoteViews{
        val remoteViews = RemoteViews("app.retvens.rown",R.layout.notification)
        remoteViews.setTextViewText(R.id.titlenotify,title)
        remoteViews.setTextViewText(R.id.description,Message)
        remoteViews.setImageViewResource(R.id.img,R.drawable.png_r_logo)

        return remoteViews
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title:String, message:String){
            val intent = Intent(this,ChatScreen::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder:androidx.core.app.NotificationCompat.Builder =  androidx.core.app.NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.png_r_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

}