package app.retvens.rown.NavigationFragments.profile.vendorsReview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.NavigationFragments.profile.viewConnections.ConnectionsAdapter
import app.retvens.rown.NavigationFragments.profile.viewConnections.ConnectionsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityReviewsBinding

class ReviewsActivity : AppCompatActivity() {
    lateinit var  binding : ActivityReviewsBinding

    lateinit var vendorsReviewAdapter: VendorsReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reviewsBackBtn.setOnClickListener { onBackPressed() }

        setUpReviewAdapter()

    }

    private fun setUpReviewAdapter() {
        binding.whatPeopleRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //binding.whatPeopleRecycler. //recyclerView.setHasFixedSize(true)


//        vendorsReviewAdapter = VendorsReviewAdapter(blogs, applicationContext)
//        binding.whatPeopleRecycler.adapter = vendorsReviewAdapter
//        vendorsReviewAdapter.notifyDataSetChanged()

    }
}