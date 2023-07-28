package app.retvens.rown.NavigationFragments.profile.hotels

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.SaveHotel
import app.retvens.rown.NavigationFragments.profile.vendorsReview.ReviewsActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelDetailsProfileBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetailsProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelDetailsProfileBinding

    var liked = true
    var operatioin = "push"
    var saved = ""

    lateinit var hotelName : String
    lateinit var hotelId : String
    lateinit var hotelLogo : String
    lateinit var location : String
    lateinit var Hoteldescription : String

    var img1 = ""
    var img2 = ""
    var img3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = intent.getStringExtra("hotelAddress").toString()
        hotelLogo = intent.getStringExtra("logo").toString()

        binding.img1.setOnClickListener {
            Glide.with(this).load(img1).into(binding.vendorImage)
        }
        binding.img2.setOnClickListener {
            Glide.with(this).load(img2).into(binding.vendorImage)
        }
        binding.img3.setOnClickListener {
            Glide.with(this).load(img3).into(binding.vendorImage)
        }

        val hotelId = intent.getStringExtra("hotelId")

        binding.location.text = location

//        saved = intent.getStringExtra("saved")!!

        binding.refreshLayout.setOnRefreshListener {
            getHotel()
            binding.refreshLayout.isRefreshing = false
        }

        getHotel()

//        if (saved != "not saved"){
//            operatioin = "pop"
//            liked = false
//            saved = "saved"
//            binding.hotelCardLike.setImageResource(R.drawable.svg_heart_liked)
//        } else {
//            operatioin = "push"
//            liked = true
//            binding.hotelCardLike.setImageResource(R.drawable.svg_heart)
//        }

        binding.hotelName.text = hotelName

        binding.hotelCardLike.setOnClickListener {
            saveHotel(hotelId)
        }
                binding.openEditReview.setOnClickListener {
                    val intent = Intent(this, EditHotelDetailsActivity::class.java)
                    intent.putExtra("name", hotelName)
                    intent.putExtra("img1", img1)
                    intent.putExtra("img2", img2)
                    intent.putExtra("img3", img3)
                    intent.putExtra("location", location)
                    intent.putExtra("hotelDescription", Hoteldescription)
                    intent.putExtra("hotelId", hotelId)
                    startActivity(intent)
                    finish()
                }

                binding.checkReviews.setOnClickListener {
                    startActivity(Intent(this, ReviewsActivity::class.java))
                }
            }

    private fun saveHotel(hotelId: String?) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val saveEvent = RetrofitBuilder.exploreApis.saveHotel(user_id, SaveHotel(operatioin,hotelId!!))
        saveEvent.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    if (liked) {
                        liked = !liked
                        binding.hotelCardLike.setImageResource(R.drawable.svg_heart_liked)
                    }else {
                        liked = !liked
                        binding.hotelCardLike.setImageResource(R.drawable.svg_heart)
                    }
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("error",response.code().toString())
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getHotel() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        hotelName = intent.getStringExtra("name").toString()
        hotelId = intent.getStringExtra("hotelId").toString()

        val hotel = RetrofitBuilder.ProfileApis.getHotelInfo(hotelId)
        hotel.enqueue(object : Callback<HotelData?> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<HotelData?>, response: Response<HotelData?>) {
                if (response.isSuccessful){
                    val data = response.body()!!
                    binding.hotelName.text = data.hotelName
                    hotelName = data.hotelName
//                    Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)

                    binding.hotelRating.text = "${data.hotelRating} Hotel"
                    location = data.hotelAddress
                    binding.location.text = data.hotelAddress
                    try {
                        Hoteldescription = data.Hoteldescription

                        if (data.gallery.get(0).Image1.isNotEmpty()){
                            img1 = data.gallery.get(0).Image1
                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.vendorImage)
                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
                            binding.img1.visibility = View.VISIBLE
                        } else {
                            binding.img1.visibility = View.GONE
                        }
                        if (data.gallery.get(0).Image2.isNotEmpty()){
                            img2 = data.gallery.get(0).Image2
                            Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.vendorImage)
                            Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)

                            binding.img2.visibility = View.VISIBLE
                        } else {
                            binding.img2.visibility = View.GONE
                        }
                        if (data.gallery.get(0).Image3.isNotEmpty()){
                            img3 = data.gallery.get(0).Image3
                            Glide.with(applicationContext).load(data.gallery.get(0).Image3).into(binding.vendorImage)
                            Glide.with(applicationContext).load(data.gallery.get(0).Image3).into(binding.img3)

                            binding.img3.visibility = View.VISIBLE
                        } else {
                            binding.img3.visibility = View.GONE
                        }

                        if (data.gallery.get(0).Image1.isEmpty() && data.gallery.get(0).Image2.isEmpty() && data.gallery.get(0).Image3.isEmpty()) {
                            Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)

                            binding.img1.visibility = View.GONE
                            binding.img2.visibility = View.GONE
                            binding.img3.visibility = View.GONE
                        }

//                        if(data.gallery.get(0).Image1 != "" && data.gallery.get(0).Image2 != "" && data.gallery.get(0).Image3 != "" ) {
//                            img1 = data.gallery.get(0).Image1
//                            img2 = data.gallery.get(0).Image2
//                            img3 = data.gallery.get(0).Image3
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.vendorImage)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image3).into(binding.img3)
//                        } else if (data.gallery.get(0).Image1 != "" && data.gallery.get(0).Image2 != "" ) {
//                            img1 = data.gallery.get(0).Image1
//                            img2 = data.gallery.get(0).Image2
//                            binding.img3.visibility = View.GONE
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.vendorImage)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)
//                        } else if (data.gallery.get(0).Image1 != "") {
//                            binding.img2.visibility = View.GONE
//                            binding.img3.visibility = View.GONE
//                            img1 = data.gallery.get(0).Image1
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
//                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.vendorImage)
//                        } else {
//                            binding.img1.visibility = View.GONE
//                            binding.img2.visibility = View.GONE
//                            binding.img3.visibility = View.GONE
//                            Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)
//                        }
                    }catch (e:IndexOutOfBoundsException){
                        Log.e("error",e.message.toString())
                        binding.img1.visibility = View.GONE
                        binding.img2.visibility = View.GONE
                        binding.img3.visibility = View.GONE
                        Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)
                    }



                    binding.descriptionHotel.text = data.Hoteldescription
                    hotelLogo = data.hotelLogoUrl
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HotelData?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}