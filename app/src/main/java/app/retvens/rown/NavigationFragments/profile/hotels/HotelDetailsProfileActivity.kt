package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.NavigationFragments.profile.vendorsReview.ReviewsActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelDetailsProfileBinding

class HotelDetailsProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelDetailsProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

                binding.openEditReview.setOnClickListener {
                    startActivity(Intent(this, EditHotelDetailsActivity::class.java))
                }

                binding.checkReviews.setOnClickListener {
                    startActivity(Intent(this, ReviewsActivity::class.java))
                }
            }

}