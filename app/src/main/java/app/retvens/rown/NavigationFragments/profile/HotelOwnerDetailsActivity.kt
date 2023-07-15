package app.retvens.rown.NavigationFragments.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityHotelOwnerDetailsBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelOwnerDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityHotelOwnerDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelOwnerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }

        getProfile()
        getHotel()

        binding.propertyRecycler.layoutManager = LinearLayoutManager(this)
        binding.propertyRecycler.setHasFixedSize(true)
    }

    private fun getHotel() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.ProfileApis.getProfileHotels(user_id,user_id)

        getHotel.enqueue(object : Callback<List<HotelsName>?> {
            override fun onResponse(
                call: Call<List<HotelsName>?>,
                response: Response<List<HotelsName>?>
            ) {
                if (response.isSuccessful){
                    try {
                        val response = response.body()!!
                        val adpater = ProfessionalHotelAdapter(applicationContext,response)
                        binding.propertyRecycler.adapter = adpater
                        adpater.notifyDataSetChanged()
                    }catch (e:NullPointerException){
                        Log.e("error",e.message.toString())
                    }
                }else{
                    Log.e("error",response.code().toString())
                }
            }
            override fun onFailure(call: Call<List<HotelsName>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun getProfile() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val fetchUser = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        fetchUser.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Glide.with(applicationContext).load(response.Profile_pic).into(binding.userProfile)
                    Glide.with(applicationContext).load(response.Profile_pic).into(binding.vendorProfile)
                    binding.name.text = response.Full_name
                    binding.vendorName.text = response.hotelOwnerInfo.hotelownerName
                    binding.username.text = response.User_name
                    binding.location.text = response.location



                }else{
                    Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                    Log.e("error",t.message.toString())
            }
        })
    }
}