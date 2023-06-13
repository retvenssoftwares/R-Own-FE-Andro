package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelDetailsBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelDetailsBinding

    private var liked = true
    private var operatioin = "push"

    private lateinit var hotelName : String
    private lateinit var hotelId : String
    private lateinit var hotelLogo : String
    private lateinit var hotelCoverpicUrl : String
    private lateinit var location : String
    private lateinit var Hoteldescription : String

    private var img1 = ""
    private var img2 = ""
    private var img3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = intent.getStringExtra("hotelAddress").toString()
        hotelLogo = intent.getStringExtra("logo").toString()

        Glide.with(this).load(hotelLogo).into(binding.vendorImage)

        val hotelId = intent.getStringExtra("hotelId").toString()

        val saved = intent.getStringExtra("saved")
        getHotel()

        if (saved != "no"){
            operatioin = "pop"
            liked = false
            binding.hotelCardLike.setImageResource(R.drawable.svg_heart_liked)
        } else {
            operatioin = "push"
            liked = true
            binding.hotelCardLike.setImageResource(R.drawable.svg_heart)
        }

        binding.hotelName.text = hotelName

        binding.img1.setOnClickListener {
            Glide.with(this).load(img1).into(binding.vendorImage)
        }
        binding.img2.setOnClickListener {
            Glide.with(this).load(img2).into(binding.vendorImage)
        }
        binding.img3.setOnClickListener {
            Glide.with(this).load(img3).into(binding.vendorImage)
        }

        binding.hotelCardLike.setOnClickListener {
            saveHotel(hotelId)
        }
        binding.openReview.setOnClickListener {
            val intent = Intent(applicationContext, HotelReviewsActivity::class.java)
            intent.putExtra("hotelId",hotelId)
            intent.putExtra("hotelName",hotelName)
            intent.putExtra("hotelCoverpicUrl",hotelCoverpicUrl)
            intent.putExtra("Hoteldescription",Hoteldescription)
            startActivity(intent)
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
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
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
            override fun onResponse(call: Call<HotelData?>, response: Response<HotelData?>) {
                if (response.isSuccessful){
                    val data = response.body()!!
                    binding.hotelName.text = data.hotelName
                    hotelCoverpicUrl = data.hotelCoverpicUrl
//                    Glide.with(applicationContext).load().into(binding.vendorImage)
                    hotelLogo = data.hotelLogoUrl

                    Hoteldescription = data.Hoteldescription
                    binding.descriptionHotel.text = data.Hoteldescription
                    binding.location.text = data.location
                    if (data.location!= null || data.location!= ""){
                        location = data.location
                    } else {
                        location = "Not Found"
                    }

                    if(data.gallery.size >= 3) {
                        img1 = data.gallery.get(0).Images
                        img2 = data.gallery.get(1).Images
                        img3 = data.gallery.get(2).Images
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(1).Images).into(binding.img2)
                        Glide.with(applicationContext).load(data.gallery.get(2).Images).into(binding.img3)
                    } else if (data.gallery.size >= 2) {
                        img1 = data.gallery.get(0).Images
                        img2 = data.gallery.get(1).Images
                        binding.img3.visibility = View.GONE
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(1).Images).into(binding.img2)
                    } else if (data.gallery.size > 0) {
                        img1 = data.gallery.get(0).Images
                        binding.img2.visibility = View.GONE
                        binding.img3.visibility = View.GONE
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                    } else {
                        binding.img1.visibility = View.GONE
                        binding.img2.visibility = View.GONE
                        binding.img3.visibility = View.GONE
                        Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)
                    }
                    binding.descriptionHotel.text = data.Hoteldescription
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