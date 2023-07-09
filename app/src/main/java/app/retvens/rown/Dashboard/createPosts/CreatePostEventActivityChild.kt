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
import app.retvens.rown.databinding.ActivityCreatePostEventChildBinding
import app.retvens.rown.databinding.ActivityCreateTextPostBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePostEventActivityChild : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener {

    lateinit var binding : ActivityCreatePostEventChildBinding
    var canSee : Int ?= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostEventChildBinding.inflate(layoutInflater)
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



        val name = intent.getStringExtra("name")
        val pic = intent.getStringExtra("image")
        val date = intent.getStringExtra("date")

        Glide.with(applicationContext).load(pic).into(binding.coverEvent)
        binding.titleEvent.text = name
        binding.eventDate.text  = date

        binding.nextCumm.setOnClickListener {
            val name = intent.getStringExtra("name")
            val pic = intent.getStringExtra("image")
            val date = intent.getStringExtra("date")
            val location = intent.getStringExtra("location")

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.canSeeText.text == "Can See"){
                Toast.makeText(applicationContext,"Select Post Seen Status", Toast.LENGTH_SHORT).show()
            }else if (binding.canCommentText.text == "Can comment"){
                Toast.makeText(applicationContext,"Select Comment Status", Toast.LENGTH_SHORT).show()
            }else{
                shareEvent(user_id,name,pic,date,location!!)
            }
        }
    }

    private fun shareEvent(userId: String, name: String?, pic: String?, date: String?,location:String) {
        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()


        val sendPost  = RetrofitBuilder.feedsApi.createPostEvent(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"Update about an event"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),pic!!),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name!!),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),location)
        )

        sendPost.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, DashBoardActivity::class.java))
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