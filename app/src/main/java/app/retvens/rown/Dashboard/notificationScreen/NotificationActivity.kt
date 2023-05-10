package app.retvens.rown.Dashboard.notificationScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationsBackBtn.setOnClickListener{ onBackPressed() }

        binding.personalNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
        binding.communityNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
        binding.connectionNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
        binding.suggetionNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
        }

    }
}