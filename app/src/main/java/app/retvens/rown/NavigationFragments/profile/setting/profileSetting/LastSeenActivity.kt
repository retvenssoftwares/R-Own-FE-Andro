package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLastSeenBinding

class LastSeenActivity : AppCompatActivity() {
    lateinit var binding : ActivityLastSeenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLastSeenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

    }
}