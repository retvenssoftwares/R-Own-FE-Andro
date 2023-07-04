package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityApplyForVerificationBinding
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.verificationStatus
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class ApplyForVerificationActivity : AppCompatActivity() {
    lateinit var binding : ActivityApplyForVerificationBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null
    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyForVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reBackBtn.setOnClickListener { onBackPressed() }

        if (verificationStatus != "false"){
            binding.layout.visibility = View.GONE
            binding.alreadyApplied.visibility = View.VISIBLE
        }

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val sharedPreferencess = getSharedPreferences("SaveUserName", AppCompatActivity.MODE_PRIVATE)
        val userName = sharedPreferencess.getString("user_name", "").toString()
        binding.userName.text = userName

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        binding.fullName.text = profileName

        binding.uploadDoc.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_select_doc)

            dialog.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
                binding.select.text = "Driver's License"
                openGallery()
                dialog.dismiss()
            }
            dialog.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
                binding.select.text = "Passport"
                openGallery()
                dialog.dismiss()
            }
            dialog.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
                binding.select.text = "Aadhar Card"
                openGallery()
                dialog.dismiss()
            }
            dialog.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
                binding.select.text = "Tax Filing"
                openGallery()
                dialog.dismiss()
            }
            dialog.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
                binding.select.text = "Recent Utility Bill"
                openGallery()
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }

        binding.save.setOnClickListener {
            if (croppedImageUri != null) {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                upload(user_id)
            } else {
                Toast.makeText(applicationContext, "Please Select an document", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun upload(user_id: String) {

        val file = prepareFilePart( croppedImageUri!!,"Documents", applicationContext)

        val upload = RetrofitBuilder.profileCompletion.uploadDocs(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"Location"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.select.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.userName.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.fullName.text.toString()),
            file!!
        )
        upload.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
                if (response.isSuccessful){
                    onBackPressed()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            if (imageUri != null) {
                try {
                    app.retvens.rown.utils.cropProfileImage(imageUri, this)
                }catch(e: RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!
                croppedImageUri = app.retvens.rown.utils.compressImage(croppedImage, this)
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}