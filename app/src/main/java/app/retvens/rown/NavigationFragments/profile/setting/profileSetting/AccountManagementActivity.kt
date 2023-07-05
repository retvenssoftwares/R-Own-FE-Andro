package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAccountManagementBinding

class AccountManagementActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.deleteAccount.setOnClickListener {  }
    }
}