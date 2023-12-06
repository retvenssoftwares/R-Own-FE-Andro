package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.databinding.ActivityCommunityEditBinding
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mesibo.api.Mesibo
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.decodeSampledBitmapFromFile
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

class CommunityEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityEditBinding

    private  var communityUri: Uri? = null
    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private  var cameraImageUri: Uri? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropProfileImage(cameraImageUri!!)
    }

    private lateinit var profile: ShapeableImageView
    private lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")
        val groupId = intent.getStringExtra("groupId")
        val location = intent.getStringExtra("location")

        binding.communityNameEdit.setText(title)

        Glide.with(applicationContext).load(image).into(binding.communityEditProfile)

        binding.communityDescriptionEdit.setText(description)

        profile = findViewById(R.id.community_edit_profile)

        binding.communityEditProfile.setOnClickListener {
            openBottomCameraSheet()
        }

        binding.nextEditCo.setOnClickListener {

            if (communityUri != null){
                updateProfile(groupId,title,image,description,location)
                onBackPressed()
            }else{
                updateProfiles(groupId,title,image,description,location)
                onBackPressed()
            }
        }



    }

    private fun updateProfiles(
        groupId: String?,
        title: String?,
        image: String?,
        description: String?,
        location: String?
    ) {

        var name = binding.communityNameEdit.text.toString()
        if (name == ""){
            name = title!!
        }

        var desc = binding.communityDescriptionEdit.text.toString()
        if (desc == ""){
            desc = description!!
        }

        val updateProfile = RetrofitBuilder.feedsApi.updateGroupDetails(groupId!!,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),desc),
        )

        updateProfile.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,

                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()

                    val profile = Mesibo.getProfile(groupId.toLong())
                    profile.name = name
                    Log.e("name",profile.name.toString())
                    profile.save()

                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun updateProfile(groupId: String?,name:String?,image:String?,description:String?,location:String?) {


        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            communityUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "${app.retvens.rown.utils.getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        var names = binding.communityNameEdit.text.toString()
        if (names == ""){
            names = name!!
        }

        var desc = binding.communityDescriptionEdit.text.toString()
        if (desc == ""){
            desc = description!!
        }

        val updateProfile = RetrofitBuilder.feedsApi.updateGroup(groupId!!,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),names),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),desc),
            MultipartBody.Part.createFormData("Profile_pic", file.name, body)
        )

        updateProfile.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    val profile = Mesibo.getProfile(groupId.toLong())
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
               Log.e("error",t.message.toString())
            }
        })


        val sendProfile = RetrofitBuilder.feedsApi.updateGroupProfile(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),groupId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),names),
            MultipartBody.Part.createFormData("image", file.name, body)
        )

        sendProfile.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("res",response.op.toString())
                }else{
                    Log.e("code",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun openBottomCameraSheet() {

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_camera)

        dialog.findViewById<ImageView>(R.id.delete_img).setOnClickListener {
            deleteImage()
        }

        dialog.findViewById<LinearLayout>(R.id.pick_from_gallery).setOnClickListener {
            openGallery()
        }
        dialog.findViewById<LinearLayout>(R.id.pick_from_camera).setOnClickListener {
            openCamera()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun openCamera() {
        contract.launch(cameraImageUri)
        dialog.dismiss()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    private fun deleteImage() {
        communityUri = null
        profile.setImageURI(communityUri)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                cropProfileImage(imageUri)
                communityUri = imageUri
            }
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun cropProfileImage(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(filesDir, "croppedImage.jpg").toUri()

        val options : UCrop.Options = UCrop.Options()
        options.setCircleDimmedLayer(true)
        UCrop.of(inputUri, outputUri)
            .withAspectRatio(1F, 1F)
            .withOptions(options)
            .start(this)
    }
    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }

    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
            val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg",System.currentTimeMillis())
            val finalFile = File(path,fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            communityUri = compressed
            profile.setImageURI(communityUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }
}