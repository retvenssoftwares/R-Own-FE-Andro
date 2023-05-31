package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetGoingBack
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.bottomsheet.BottomSheetWhatToPost
import app.retvens.rown.databinding.ActivityCreateClickAndSharePostBinding
import app.retvens.rown.databinding.ActivityCreatePostBinding
import app.retvens.rown.databinding.ActivityCreateTextPostBinding
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class CreateTextPost : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener,
    BottomSheetGoingBack.OnBottomGoingBackClickListener,
    BottomSheetWhatToPost.OnBottomWhatToPostClickListener {
    lateinit var binding : ActivityCreateTextPostBinding

    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog

    var canSee : Int ?= 0

    var selectedImg : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(applicationContext).load(profilePic).into(binding.userCompleteProfile)
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


        binding.createP.setOnClickListener {
            selectedImg = 1
            val bottomSheet = BottomSheetWhatToPost()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetWhatToPost.WTP_TAG)}
            bottomSheet.setOnWhatToPostClickListener(this)
        }

        binding.ShareStatus.setOnClickListener {

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.canSeeText.text == "Can See"){
                Toast.makeText(applicationContext,"Select Post Seen Status",Toast.LENGTH_SHORT).show()
            }else if (binding.canCommentText.text == "Can comment"){
                Toast.makeText(applicationContext,"Select Comment Status",Toast.LENGTH_SHORT).show()
            }else{
                shareStatus(user_id)
            }
        }
    }

    private fun shareStatus(userId:String) {

        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()


        val sendPost  = RetrofitBuilder.feedsApi.createStatus(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"normal status"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption)
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
    override fun bottomWhatToPostClick(WhatToPostFrBo: String) {
        when (WhatToPostFrBo) {
            "Click" -> {
                startActivity(Intent(applicationContext, CreateClickAndSharePostActivity::class.java))
            }
            "Share" -> {
                startActivity(Intent(applicationContext, CreatePostActivity::class.java))
            }
            "Update" -> {
                startActivity(Intent(applicationContext, CreateEventPostActivity::class.java))
            }
            "Check" -> {
                startActivity(Intent(applicationContext, CreatCheackInPostActivity::class.java))
            }
            "Poll" -> {
                startActivity(Intent(applicationContext, CreatePollActivity::class.java))
            }
        }
    }
    override fun onBackPressed() {
//        super.onBackPressed()
        val bottomSheet = BottomSheetGoingBack()
        val fragManager = supportFragmentManager
        fragManager.let{bottomSheet.show(it, BottomSheetGoingBack.GoingBack_TAG)}
        bottomSheet.setOnGoingBackClickListener(this)
    }
    override fun bottomGoingBackClick(GoingBackFrBo: String) {
        if (GoingBackFrBo == "Discard"){
            super.onBackPressed()
        }
    }
}