package app.retvens.rown.NavigationFragments.profile.settingForViewers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAboutProfileBinding
import app.retvens.rown.utils.dateFormat

class AboutProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityAboutProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val created = intent.getStringExtra("created").toString()
        binding.created.setText(dateFormat(created))

        binding.location.setText(intent.getStringExtra("location"))

        val verification = intent.getStringExtra("verification").toString()

        if (verification == "false"){
            binding.verification.setText("Not applied for verification")
        } else {
            binding.verification.setText("Verified")
        }


    }
}