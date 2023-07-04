package app.retvens.rown.NavigationFragments.exploreForUsers.events

class MyFirebaseMessagingService : FirebaseMessagingService() {

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

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("app.retvens.rown", R.layout.notification)
        remoteViews.setTextViewText(R.id.titlenotify, title)
        remoteViews.setTextViewText(R.id.description, message)
        remoteViews.setImageViewResource(R.id.img, R.drawable.png_r_logo)

        return remoteViews
    }


}
