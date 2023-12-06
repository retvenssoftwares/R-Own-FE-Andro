package app.retvens.rown.ApiRequest

import android.app.*
import android.app.Notification
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.retvens.rown.MainActivity
import app.retvens.rown.R
import app.retvens.rown.api.MesiboCall

class MesiboBackgroundService : Service() {

    private  val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()

        MesiboCall.getInstance().init(this)

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

    }

    private fun createNotification(): Notification? {
        val channelId = "my_background_service_channel"
        val notificationId = 1

        // Create a notification channel if running on Android Oreo (API 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Background Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }

        // Create a pending intent to launch the app when the notification is tapped
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Background Service")
            .setContentText("Running")
            .setSmallIcon(R.drawable.png_r_logo)
            .setContentIntent(pendingIntent)
            .build()

        // Start the service in the foreground with the notification
        startForeground(notificationId, notification)

        return notification
    }

    // Implement other necessary methods and logic for Mesibo calls

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Return START_STICKY or START_NOT_STICKY based on your requirements
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up or release resources here
    }
}