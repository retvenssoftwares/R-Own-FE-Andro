package app.retvens.rown.Dashboard.createPosts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.databinding.ActivityCheckInChildBinding
import app.retvens.rown.databinding.ActivityCreatePostEventChildBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInChildActivity : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener {

    lateinit var binding : ActivityCheckInChildBinding
    var canSee : Int ?= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        if (profilePic.isNotEmpty()) {
            Glide.with(applicationContext).load(profilePic).into(binding.userCompleteProfile)
        } else {
            binding.userCompleteProfile.setImageResource(R.drawable.svg_user)
        }

        binding.userCompleteName.setText(profileName)

        binding.canSee.setOnClickListener {
            canSee = 1
            val bottomSheet = BottomSheetSelectAudience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetSelectAudience.S_A_TAG)}
            bottomSheet.setOnSelectAudienceClickListener(this)
        }
        binding.canComment.setOnClickListener {
            canSee = 2
            val bottomSheet = BottomSheetSelectAudience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetSelectAudience.S_A_TAG)}
            bottomSheet.setOnSelectAudienceClickListener(this)
        }

        val hotelPic = intent.getStringExtra("hotelPic")
        val hotelName = intent.getStringExtra("hotelName")

        Glide.with(applicationContext).load(hotelPic).into(binding.coverEvent)
        binding.titleEvent.text = hotelName

        binding.nextCumm.setOnClickListener {
            val name = intent.getStringExtra("hotelName")
            val hotelId = intent.getStringExtra("hotelId")
            val date = intent.getStringExtra("date")
            val location = intent.getStringExtra("location")

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.canSeeText.text == "Can See"){
                Toast.makeText(applicationContext,"Select Post Seen Status", Toast.LENGTH_SHORT).show()
            }else if (binding.canCommentText.text == "Can comment"){
                Toast.makeText(applicationContext,"Select Comment Status", Toast.LENGTH_SHORT).show()
            }else{
                shareCheckIn(user_id,name,hotelId,location!!)
            }
        }

    }

    private fun shareCheckIn(
        userId: String,
        name: String?,
        hotelId: String?,
        location: String
    ) {
        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()


        val sendPost  = RetrofitBuilder.feedsApi.createCheckIn(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelId!!),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"Check-in"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),location)
        )

        sendPost.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            binding.canSeeText.text = audienceFrBo
        }else{
            binding.canCommentText.text = audienceFrBo
        }
    }
}