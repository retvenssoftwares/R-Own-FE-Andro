package app.retvens.rown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import app.retvens.rown.Dashboard.DashBoardActivity

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Handle the custom action triggered by the notification
        if (intent.action == "your_custom_action") {

            Log.e("count","1")

            // Perform the desired action here, such as opening an activity or executing a specific task
            val targetIntent = Intent(context, DashBoardActivity::class.java)
            targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(targetIntent)
        }
    }
}