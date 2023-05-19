package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAccountSettingBinding

class AccountSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.lastSeen.setOnClickListener {
            startActivity(Intent(this, LastSeenActivity::class.java))
        }

        binding.sync.setOnClickListener {
            startActivity(Intent(this, SyncContactsActivity::class.java))
        }

        binding.management.setOnClickListener {
            startActivity(Intent(this, AccountManagementActivity::class.java))
        }

    }
}