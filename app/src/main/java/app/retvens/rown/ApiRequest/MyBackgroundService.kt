package app.retvens.rown.ApiRequest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.retvens.rown.R
import app.retvens.rown.api.MesiboCall

class MyBackgroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Perform any necessary setup here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()

        val thread = Thread {
            // Perform the desired task, such as initializing MesiboCall
            MesiboCall.getInstance().init(this@MyBackgroundService)
        }

        thread.start()

        // Start the service in the foreground to ensure it keeps running
        val notification = createNotification() // Create a notification for the foreground service
        startForeground(NOTIFICATION_ID, notification)

        // Return START_STICKY to indicate that the service should be restarted if it's killed
        return START_STICKY
    }



    override fun onDestroy() {
        // Clean up any resources or connections here
        super.onDestroy()
    }

    private fun createNotification(): android.app.Notification {
        // Create and return a notification for the foreground service
        // Customize the notification based on your requirements
        // ...

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.png_r_logo)

        return notificationBuilder.build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "ForegroundServiceChannel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}