package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEvenDetailsBinding
import app.retvens.rown.databinding.ActivityJobNotificationBinding

class JobNotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityJobNotificationBinding

    var onSwitchInApp = true
    var onSwitchPost = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.relative1.setOnClickListener {
            if (onSwitchPost) {
                binding.postYes.setImageResource(R.drawable.switch_yes)
                onSwitchPost = false
            } else {
                binding.postYes.setImageResource(R.drawable.switch_no)
                onSwitchPost = true
            }
        }
        binding.inApp.setOnClickListener {
            if (onSwitchInApp) {
                binding.inAppYes.setImageResource(R.drawable.switch_yes)
                onSwitchInApp = false
            } else {
                binding.inAppYes.setImageResource(R.drawable.switch_no)
                onSwitchInApp = true
            }
        }
    }
}