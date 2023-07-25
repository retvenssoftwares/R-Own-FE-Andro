package app.retvens.rown.viewAll.vendorsDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.query
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.QuickReviewAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorReviewsData
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

    var image1 = ""
    var image2 = ""
    var image3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }

        val user_id = intent.getStringExtra("user_id").toString()
        fetchUser(user_id)
        val vendorImage = intent.getStringExtra("vendorImage").toString()
        val vendorName = intent.getStringExtra("vendorName").toString()

//        Glide.with(this).load(vendorImage).into(binding.vendorProfile)

        binding.servicesGrid.layoutManager = GridLayoutManager(this,3)
        binding.servicesGrid.setHasFixedSize(true)

//        topReview(user_id)
//        allReview(user_id)

        binding.img1.setOnClickListener {
            val intent = Intent(this, VendorsImageViewActivity::class.java)
            intent.putExtra("img1", image1)
            intent.putExtra("img2", image2)
            intent.putExtra("img3", image3)
            startActivity(intent)
        }
        binding.img2.setOnClickListener {
            val intent = Intent(this, VendorsImageViewActivity::class.java)
            intent.putExtra("img1", image1)
            intent.putExtra("img2", image2)
            intent.putExtra("img3", image3)
            startActivity(intent)
        }
        binding.img3.setOnClickListener {
            val intent = Intent(this, VendorsImageViewActivity::class.java)
            intent.putExtra("img1", image1)
            intent.putExtra("img2", image2)
            intent.putExtra("img3", image3)
            startActivity(intent)
        }

        binding.addReview.setOnClickListener {
            val title = "How was your experience with"
            val bottomSheet = BottomSheetAddReview(title, vendorName, user_id)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetAddReview.Review_TAG)}
        }

        binding.cardHire.setOnClickListener {
            val intent = Intent(this, VendorProfileActivity::class.java)
            intent.putExtra("userId", user_id)
            startActivity(intent)
        }
    }

    private fun topReview(user_id: String) {
        binding.topReviewRecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.topReviewRecycler.setHasFixedSize(true)

        val allR = RetrofitBuilder.viewAllApi.topReviews(user_id)
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
                Log.d("fetch", response.body().toString())

                if (response.isSuccessful) {
                    val image = response.body()?.vendorInfo?.vendorImage
                    val name = response.body()?.Full_name
//                    val name = response.body()?.vendorInfo?.vendorDescription
                    val vendorServicesAdapter = VendorServicesAdapter(applicationContext, response.body()!!.vendorInfo.vendorServices)
                    binding.servicesGrid.adapter = vendorServicesAdapter
                    vendorServicesAdapter.notifyDataSetChanged()

                    Glide.with(applicationContext).load(image).into(binding.vendorProfile)
                    binding.vendorName.text = (name).toString()
                    try {
                        binding.vendorDescription.text =
                            (response.body()!!.vendorInfo.vendorDescription).toString()
                    } catch (e: NullPointerException) {

                    }
                    if (response.body()!!.vendorInfo.portfolioLink.isNotEmpty()) {
                        val i1 = response.body()!!.vendorInfo.portfolioLink.get(0).Image1
                        val i2 = response.body()!!.vendorInfo.portfolioLink.get(0).Image2
                        val i3 = response.body()!!.vendorInfo.portfolioLink.get(0).Image3
                        try {
                            if (i1.isNotEmpty() && i2.isNotEmpty() && i3.isNotEmpty()) {
                                image1 = i1
                                image2 = i2
                                image3 = i3

                                Glide.with(applicationContext)
                                    .load(i1)
                                    .into(binding.img1)
                                Glide.with(applicationContext)
                                    .load(i2)
                                    .into(binding.img2)
                                Glide.with(applicationContext)
                                    .load(i3)
                                    .into(binding.img3)
                            } else if (i1.isNotEmpty() && i2.isNotEmpty()) {
                                binding.img3.visibility = View.GONE
                                image1 = i1
                                image2 = i2
                                image3 = ""

                                Glide.with(applicationContext)
                                    .load(i1)
                                    .into(binding.img1)
                                Glide.with(applicationContext)
                                    .load(i2)
                                    .into(binding.img2)
                            } else if (i1.isNotEmpty() && i1.isNotBlank()) {
                                binding.img3.visibility = View.GONE
                                binding.img2.visibility = View.GONE
                                image1 = i1
                                image2 = ""
                                image3 = ""

                                Glide.with(applicationContext)
                                    .load(i1)
                                    .into(binding.img1)
                            } else {
                                image1 = ""
                                image2 = ""
                                image3 = ""
                                binding.img1.visibility = View.GONE
                                binding.img2.visibility = View.GONE
                                binding.img3.visibility = View.GONE
                                binding.portfolioImageError.visibility = View.VISIBLE
                            }
                        } catch (e: NullPointerException) {
                            image1 = ""
                            image2 = ""
                            image3 = ""
                            binding.img1.visibility = View.GONE
                            binding.img2.visibility = View.GONE
                            binding.img3.visibility = View.GONE
                            binding.portfolioImageError.visibility = View.VISIBLE
                        }
                    } else {
                        image1 = ""
                        image2 = ""
                        image3 = ""
                        binding.img1.visibility = View.GONE
                        binding.img2.visibility = View.GONE
                        binding.img3.visibility = View.GONE
                        binding.portfolioImageError.visibility = View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}