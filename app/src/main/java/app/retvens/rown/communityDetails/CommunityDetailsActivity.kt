package app.retvens.rown.communityDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCommunityDetailsBinding

class CommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailEditBtn.setOnClickListener {
            startActivity(Intent(this, CommunityEditActivity::class.java))
        }

    }
}