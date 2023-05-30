package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        Glide.with(this).load(hotelLogo).into(binding.vendorImage)

        val hotelId = intent.getStringExtra("hotelId")

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
                    Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)

                    Hoteldescription = data.Hoteldescription
                    if(data.gallery.size >= 3) {
                        img1 = data.gallery.get(0).Images
                        img2 = data.gallery.get(1).Images
                        img3 = data.gallery.get(2).Images
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(1).Images).into(binding.img2)
                        Glide.with(applicationContext).load(data.gallery.get(2).Images).into(binding.img3)
                    } else if (data.gallery.size >= 2) {
                        img1 = data.gallery.get(0).Images
                        img2 = data.gallery.get(1).Images
                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(1).Images).into(binding.img2)
                    } else if (data.gallery.size > 0) {
                        img1 = data.gallery.get(0).Images
                            Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.img1)
                    }

                    binding.descriptionHotel.text = data.Hoteldescription
                    binding.location.text = data.location
                    hotelLogo = data.hotelLogoUrl
                    if (data.location!= null){
                        location = data.location
                    }
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