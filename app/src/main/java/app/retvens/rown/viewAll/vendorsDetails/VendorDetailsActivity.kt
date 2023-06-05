package app.retvens.rown.viewAll.vendorsDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorsReviewAdapter
import app.retvens.rown.bottomsheet.BottomSheetAddReview
import app.retvens.rown.databinding.ActivityVendorDetailsBinding
import com.bumptech.glide.Glide
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityVendorDetailsBinding

    lateinit var vendorsReviewAdapter: VendorsReviewAdapter
    lateinit var allReviewsAdapter: AllReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }

        val user_id = intent.getStringExtra("user_id").toString()
        fetchUser(user_id)
        val vendorImage = intent.getStringExtra("vendorImage").toString()
        val vendorName = intent.getStringExtra("vendorName").toString()

        Glide.with(this).load(vendorImage).into(binding.vendorProfile)

        topReview(user_id)

        binding.img1.setOnClickListener { startActivity(Intent(this, VendorsImageViewActivity::class.java)) }
        binding.img2.setOnClickListener { startActivity(Intent(this, VendorsImageViewActivity::class.java)) }

        binding.addReview.setOnClickListener {
            val title = "How was your experience with $vendorName?"
            val bottomSheet = BottomSheetAddReview(title, user_id)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetAddReview.Review_TAG)}
        }
    }

    private fun topReview(user_id: String) {
        binding.whatPeopleRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.whatPeopleRecycler.setHasFixedSize(true)

        binding.allReviewRecycler.layoutManager = LinearLayoutManager(this)
        binding.allReviewRecycler.setHasFixedSize(true)

        val allR = RetrofitBuilder.viewAllApi.allReviews(user_id)
        allR.enqueue(object : Callback<List<AllReviewsData>?> {
            override fun onResponse(
                call: Call<List<AllReviewsData>?>,
                response: Response<List<AllReviewsData>?>
            ) {
                if (response.isSuccessful) {
                    try {
                        vendorsReviewAdapter =
                            VendorsReviewAdapter(response.body()!!.get(0).userReviews, applicationContext)
                        binding.whatPeopleRecycler.adapter = vendorsReviewAdapter
                        vendorsReviewAdapter.notifyDataSetChanged()

                        allReviewsAdapter =
                            AllReviewAdapter(response.body()!!.get(0).userReviews, applicationContext)
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
    private fun fetchUser(userId: String) {
        val fetch = RetrofitBuilder.retrofitBuilder.fetchUser(userId)
        fetch.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                Log.d("fetch",response.body().toString())

                if (response.isSuccessful) {
                    val image = response.body()?.vendorInfo?.vendorImage
                    val name = response.body()?.Full_name
//                    val name = response.body()?.vendorInfo?.vendorDescription

                    Glide.with(applicationContext).load(image).into(binding.vendorProfile)
                    binding.vendorName.text = (name).toString()
                    try {
                        binding.vendorDescription.text =
                            (response.body()!!.vendorInfo.vendorDescription).toString()
                    } catch (e:NullPointerException){

                    }
                    try {
                        if (response.body()!!.vendorInfo.portfolioLink.size >= 3) {
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(0).images)
                                .into(binding.img1)
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(1).images)
                                .into(binding.img2)
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(2).images)
                                .into(binding.img3)
                        } else if (response.body()!!.vendorInfo.portfolioLink.size >= 2) {
                            binding.img3.visibility = View.GONE
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(0).images)
                                .into(binding.img1)
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(1).images)
                                .into(binding.img2)
                        } else if (response.body()!!.vendorInfo.portfolioLink.size > 0) {
                            binding.img3.visibility = View.GONE
                            binding.img2.visibility = View.GONE
                            Glide.with(applicationContext)
                                .load(response.body()!!.vendorInfo.portfolioLink.get(0).images)
                                .into(binding.img1)
                        }
                    } catch (e:NullPointerException){

                    }
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}