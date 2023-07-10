package app.retvens.rown.NavigationFragments.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelOwnerDetailsBinding

class HotelOwnerDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelOwnerDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelOwnerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }

    }
}