package app.retvens.rown.NavigationFragments.exploreForUsers.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveEvent
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEventDetailsBinding
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsAdapter
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityEventDetailsBinding

    var liked = true
    var operatioin = "push"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reviewBackBtn.setOnClickListener { onBackPressed() }

        val cover = intent.getStringExtra("cover")
        val title = intent.getStringExtra("title")
        val about = intent.getStringExtra("about")
        val price = intent.getStringExtra("price")
        val Profile_pic = intent.getStringExtra("Profile_pic")
        val User_name = intent.getStringExtra("User_name")
        val location = intent.getStringExtra("location")

        val saved = intent.getStringExtra("saved")


        if (saved == "saved"){
            operatioin = "pop"
            liked = false
            binding.save.setImageResource(R.drawable.svg_heart_liked)
        } else {
            operatioin = "push"
            liked = true
            binding.save.setImageResource(R.drawable.svg_heart)
        }

        val userId = intent.getStringExtra("userId")
        val eventId = intent.getStringExtra("eventId")

        binding.save.setOnClickListener {
            saveEvent(eventId)
        }

        binding.concertProfileName.text = User_name
        Glide.with(applicationContext).load(Profile_pic).into(binding.concertProfile)

        binding.concert.text = title
        binding.about.text = about
        Glide.with(this).load(cover).into(binding.concertCover)
        binding.concertPrice.text = price

        binding.openMap.setOnClickListener {
            Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
        }
        binding.getCab.setOnClickListener {
            Toast.makeText(this, "Getting", Toast.LENGTH_SHORT).show()
        }
        binding.bookNow.setOnClickListener {
            Toast.makeText(this, "Booking", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveEvent(eventId: String?) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

//        Toast.makeText(applicationContext, "$eventId   $user_id", Toast.LENGTH_SHORT).show()

        val saveEvent = RetrofitBuilder.EventsApi.saveEvent(user_id, SaveEvent(operatioin,eventId!!))
       saveEvent.enqueue(object : Callback<UpdateResponse?> {
           override fun onResponse(
               call: Call<UpdateResponse?>,
               response: Response<UpdateResponse?>
           ) {
               if (response.isSuccessful){
                   if (liked) {
                       liked = !liked
                       operatioin="pop"
                       binding.save.setImageResource(R.drawable.svg_heart_liked)
                   }else {
                       liked = !liked
                       operatioin="push"
                       binding.save.setImageResource(R.drawable.svg_heart)
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

    private fun getUserInfo(userId: String) {

        val send = RetrofitBuilder.retrofitBuilder.fetchUser(userId)

        send.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){

                    if (response.body() != null) {
                        val response = response.body()!!

                    }
                }else{
                    Toast.makeText(applicationContext,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}