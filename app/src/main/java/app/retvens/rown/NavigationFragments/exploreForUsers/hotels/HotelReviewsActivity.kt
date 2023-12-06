package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.QuickReviewAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorsReviewAdapter
import app.retvens.rown.bottomsheet.BottomSheetAddReview
import app.retvens.rown.databinding.ActivityHotelReviewsBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelReviewsActivity : AppCompatActivity(){

    lateinit var binding : ActivityHotelReviewsBinding
    lateinit var allReviewsAdapter : AllReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hotelId = intent.getStringExtra("hotelId").toString()
        val hotelName = intent.getStringExtra("hotelName").toString()
        val hotelCoverpicUrl = intent.getStringExtra("hotelCoverpicUrl").toString()
        Glide.with(applicationContext).load(hotelCoverpicUrl).into(binding.reviewProfile)
        val Hoteldescription = intent.getStringExtra("Hoteldescription").toString()
        binding.descriptionHotel.text = Hoteldescription

        binding.addReview.setOnClickListener {
            val title = "How was your stay experience"
            val bottomSheet = BottomSheetAddReview(title, hotelName,hotelId)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetAddReview.Review_TAG)}
        }

        topReview(hotelId)
        allReview(hotelId)
    }

    private fun topReview(user_id: String) {
        binding.topReviewRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
       // binding.topReviewRecycler. //recyclerView.setHasFixedSize(true)

        val allR = RetrofitBuilder.exploreApis.topHotelReviews(user_id)
        allR.enqueue(object : Callback<List<VendorReviewsData>?> {
            override fun onResponse(
                call: Call<List<VendorReviewsData>?>,
                response: Response<List<VendorReviewsData>?>
            ) {
                if (response.isSuccessful){
                    val quickReviewAdapter = QuickReviewAdapter(response.body()!! ,applicationContext)
                    binding.topReviewRecycler.adapter = quickReviewAdapter
                    quickReviewAdapter.notifyDataSetChanged()
                }
                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<VendorReviewsData>?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun allReview(user_id: String) {
//        binding.whatPeopleRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.whatPeopleRecycler. //recyclerView.setHasFixedSize(true)

        binding.allReviewRecycler.layoutManager = LinearLayoutManager(this)
        //binding.allReviewRecycler. //recyclerView.setHasFixedSize(true)

        val allR = RetrofitBuilder.exploreApis.allHotelReviews(user_id)
        allR.enqueue(object : Callback<List<AllReviewsData>?> {
            override fun onResponse(
                call: Call<List<AllReviewsData>?>,
                response: Response<List<AllReviewsData>?>
            ) {
                Log.d("VDActivity", response.body()!!.toString())
                if (response.isSuccessful) {
                    try {
//                        vendorsReviewAdapter =
//                            VendorsReviewAdapter(response.body()!!.get(0).userReviews, applicationContext)
//                        binding.whatPeopleRecycler.adapter = vendorsReviewAdapter
//                        vendorsReviewAdapter.notifyDataSetChanged()

                        allReviewsAdapter =
                            AllReviewAdapter(response.body()!!.get(0).reviews_types, applicationContext)
                        binding.allReviewRecycler.adapter = allReviewsAdapter
                        allReviewsAdapter.notifyDataSetChanged()

                    }catch (e:NullPointerException){
                        binding.error.visibility = View.GONE
                        Log.d("VDActivity", e.toString())
                    }
                } else{
                    binding.error.visibility = View.GONE
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AllReviewsData>?>, t: Throwable) {
                binding.error.visibility = View.GONE
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}