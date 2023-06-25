package app.retvens.rown.viewAll.viewAllCommunities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOpenCommunityDetailsBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class OpenCommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityOpenCommunityDetailsBinding

    var isBusinessVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.usersText.setOnClickListener {
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            binding.llu.visibility = View.VISIBLE
        }
        binding.mediaText.setOnClickListener {
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            binding.llu.visibility = View.GONE
        }

        binding.business.setOnClickListener {
            if (isBusinessVisible){
                binding.staticCard.visibility = View.VISIBLE
                isBusinessVisible = false
            } else {
                binding.staticCard.visibility = View.GONE
                isBusinessVisible = true
            }
        }

        val groupId = intent.getStringExtra("groupId")

        getCommunityDetails(groupId)
    }

    private fun getCommunityDetails(groupId: String?) {

        val getCommunities = RetrofitBuilder.feedsApi.getGroup(groupId!!)

        getCommunities.enqueue(object : Callback<GetCommunitiesData?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetCommunitiesData?>,
                response: Response<GetCommunitiesData?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    Log.e("response", response.toString())
                    Glide.with(applicationContext).load(response.Profile_pic)
                        .into(binding.communityProfile)
                    binding.communityDetailName.text = response.group_name
                    binding.communityDetailMembers.text =
                        "${response.Totalmember.toString()} members"
                    binding.communityDescription.text = response.description

                    val date = convertTimestampToFormattedDate(response.date_added)

                    binding.communityCreatedBy.text = "Created by ${response.creator_name} | $date"


                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {

            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToFormattedDate(timestamp: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

        try {
            val instant = Instant.parse(timestamp)
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))
            return zonedDateTime.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
        }

        return ""
    }
}