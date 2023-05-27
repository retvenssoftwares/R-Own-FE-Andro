package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.profile.vendorsReview.ReviewsActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelDetailsProfileBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetailsProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelDetailsProfileBinding

    var liked = false

    lateinit var hotelName : String
    lateinit var hotelId : String
    lateinit var logo : String
    lateinit var location : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        hotelLogo = intent.getStringExtra("logo").toString()

        getHotel()

        binding.hotelName.text = hotelName

        binding.hotelCardLike.setOnClickListener {
            if (!liked) {
                liked = !liked
                binding.hotelCardLike.setImageResource(R.drawable.svg_heart_liked)
            }else {
                liked = !liked
                binding.hotelCardLike.setImageResource(R.drawable.svg_heart)
            }
        }
                binding.openEditReview.setOnClickListener {
                    val intent = Intent(this, EditHotelDetailsActivity::class.java)
                    intent.putExtra("name", hotelName)
                    intent.putExtra("logo", logo)
                    intent.putExtra("location", location)
                    startActivity(intent)
                }

                binding.checkReviews.setOnClickListener {
                    startActivity(Intent(this, ReviewsActivity::class.java))
                }
            }

    private fun getHotel() {
        hotelName = intent.getStringExtra("name").toString()
        hotelId = intent.getStringExtra("hotel_id").toString()

        val hotel = RetrofitBuilder.ProfileApis.getHotelInfo(hotelId)
        hotel.enqueue(object : Callback<HotelData?> {
            override fun onResponse(call: Call<HotelData?>, response: Response<HotelData?>) {
                if (response.isSuccessful){
                    val data = response.body()!!
                    binding.hotelName.text = data.hotelName
                    Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)
                    binding.descriptionHotel.text = data.Hoteldescription
                    binding.location.text = data.location
                    logo = data.hotelLogoUrl
                    location = data.location
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