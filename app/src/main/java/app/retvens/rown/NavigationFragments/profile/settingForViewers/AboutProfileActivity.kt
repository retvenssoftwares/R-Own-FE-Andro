package app.retvens.rown.NavigationFragments.profile.settingForViewers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAboutProfileBinding

class AboutProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityAboutProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.created.setText(intent.getStringExtra("created").toString())
        binding.location.setText(intent.getStringExtra("location").toString())
        binding.verification.setText(intent.getStringExtra("verification").toString())

    }
}