package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelDetailsBinding

class HotelDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openReview.setOnClickListener {
            startActivity(Intent(applicationContext, HotelReviewsActivity::class.java))
        }

    }
}