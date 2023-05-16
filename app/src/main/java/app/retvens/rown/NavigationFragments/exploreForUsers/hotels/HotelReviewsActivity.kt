package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetAddReview
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.databinding.ActivityHotelReviewsBinding

class HotelReviewsActivity : AppCompatActivity(), BottomSheetAddReview.OnBottomSheetAddReviewClickListener{

    lateinit var binding : ActivityHotelReviewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addReview.setOnClickListener {

            val bottomSheet = BottomSheetAddReview()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetAddReview.Review_TAG)}
            bottomSheet.setOnReviewClickListener(this)
        }

    }

    override fun bottomReviewClick(reviewFrBo: String) {

    }
}