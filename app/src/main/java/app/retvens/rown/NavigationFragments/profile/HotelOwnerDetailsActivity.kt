package app.retvens.rown.NavigationFragments.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var hotelList:ArrayList<HotelsName> = ArrayList()
    private lateinit var adpater:ProfessionalHotelAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelOwnerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val isViewer = intent.getStringExtra("viewer")
        val userID = intent.getStringExtra("userID")
        if (isViewer == "viewer"){
            getProfile(userID!!)
            getHotel(userID)
        } else {
            getProfile(user_id)
            getHotel(user_id)
        }

        binding.propertyRecycler.layoutManager = LinearLayoutManager(this)
        //binding.propertyRecycler. //recyclerView.setHasFixedSize(true)

        adpater = ProfessionalHotelAdapter(applicationContext,hotelList)
        binding.propertyRecycler.adapter = adpater
        adpater.notifyDataSetChanged()
    }

    private fun getHotel(user_id : String) {

        val getHotel = RetrofitBuilder.ProfileApis.getProfileHotels(user_id,user_id)

        getHotel.enqueue(object : Callback<List<HotelsName>?> {
            override fun onResponse(
                call: Call<List<HotelsName>?>,
                response: Response<List<HotelsName>?>
            ) {
                if (response.isSuccessful){
                    try {
                        val response = response.body()!!
                        response.forEach {
                            if (it.display_status == "1"){
                                hotelList.add(it)
                                adpater.notifyDataSetChanged()
                            }

                        }
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

    private fun getProfile(user_id: String) {

        val fetchUser = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        fetchUser.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    try {
                        if (response.Profile_pic!!.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.Profile_pic).into(binding.userProfile)
                        } else {
                            binding.userProfile.setImageResource(R.drawable.svg_user)
                        }

                        if (response.Profile_pic!!.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.Profile_pic)
                                .into(binding.vendorProfile)
                        } else {
                            binding.vendorProfile.setImageResource(R.drawable.svg_user)
                        }

                        binding.name.text = response.Full_name
                        binding.bio.text = response.userBio
                        binding.vendorName.text = response.hotelOwnerInfo.hotelownerName
                        binding.username.text = response.User_name
                        binding.location.text = response.location
                        binding.oneusername.text = response.User_name

                        val location = response.location
                        val city = location.split(",")[0].trim()
                        Log.e("city",city.toString())
                        binding.city.text = city

                    }catch (e:NullPointerException){
                        Log.e("error",e.message.toString())
                    }






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