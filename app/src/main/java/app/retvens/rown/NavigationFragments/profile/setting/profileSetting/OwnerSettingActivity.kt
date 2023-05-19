package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOwnerSettingBinding

class OwnerSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityOwnerSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.account.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }
       binding.notification.setOnClickListener {
            startActivity(Intent(this, NotificationSettingActivity::class.java))
        }
       binding.language.setOnClickListener {
            startActivity(Intent(this, LanguageSettingActivity::class.java))
        }
       binding.verification.setOnClickListener {
            startActivity(Intent(this, ApplyForVerificationActivity::class.java))
        }

    }
}