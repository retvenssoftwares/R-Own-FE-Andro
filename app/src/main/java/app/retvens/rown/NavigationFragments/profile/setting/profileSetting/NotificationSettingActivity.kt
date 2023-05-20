package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityNotificationSettingBinding

class NotificationSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityNotificationSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.jobNotification.setOnClickListener {
            startActivity(Intent(this, JobNotificationActivity::class.java))
        }
        binding.postNotification.setOnClickListener {
            startActivity(Intent(this, JobNotificationActivity::class.java))
        }
        binding.chatNotification.setOnClickListener {
            startActivity(Intent(this, JobNotificationActivity::class.java))
        }
        binding.communityNotification.setOnClickListener {
            startActivity(Intent(this, JobNotificationActivity::class.java))
        }

    }
}