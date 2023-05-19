package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLanguageSettingBinding

class LanguageSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityLanguageSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

    }
}