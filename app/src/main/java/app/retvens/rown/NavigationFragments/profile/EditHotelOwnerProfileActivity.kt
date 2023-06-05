package app.retvens.rown.NavigationFragments.profile

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
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
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEditHotelOwnerProfileBinding
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditHotelOwnerProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditHotelOwnerProfileBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        compressImage(cameraImageUri)
        cropImage(cameraImageUri)
    }
    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog

    var user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelOwnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hotelType.setOnClickListener {
            openHotelTypeBottom()
        }

        binding.profileBackBtn.setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id", "").toString()

        fetchUser(user_id)
        binding.cameraEdit.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
            openBottomSheet()
        }

        binding.save.setOnClickListener {

            if (binding.etNameEdit.length() < 3) {
                Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.hotelType.text.toString() == "Hotel Type") {
                Toast.makeText(applicationContext, "Please select Hotel Type", Toast.LENGTH_SHORT)
                    .show()
            } else {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.setCancelable(false)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent)
                    .into(image)
                progressDialog.show()

                uploadData(user_id)
                updateHotel(user_id)
            }
        }
    }

    private fun updateHotel(userId: String) {
        val hotel = RetrofitBuilder.ProfileApis.updateHotel(
            userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.hotelType.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.website.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bookingText.text.toString())
        )
        hotel.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                onBackPressed()
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadData(userId: String) {

        if (croppedImageUri != null){

            val image = prepareFilePart(croppedImageUri!!, "Profile_pic", applicationContext)

            val respo  = RetrofitBuilder.ProfileApis.updateVendorProfile(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
                image!!
            )
            respo.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_SHORT).show()
                    Log.d("image", response.toString())
                    Log.d("image", response.body().toString())

                    if (response.isSuccessful){
                        saveFullName(applicationContext, binding.etNameEdit.text.toString())
//                        saveProfileImage(applicationContext, )
                    }

                }
                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        } else {

            val respo  = RetrofitBuilder.ProfileApis.updateVendorProfileWithoutImg(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString())
            )
            respo.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_SHORT).show()
                    Log.d("image", response.toString())
                    Log.d("image", response.body().toString())

                    if (response.isSuccessful){
                        saveFullName(applicationContext, binding.etNameEdit.text.toString())
//                        saveProfileImage(applicationContext, )
                        onBackPressed()
                    }

                }
                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun fetchUser(userId: String) {
        val fetch = RetrofitBuilder.retrofitBuilder.fetchUser(userId)
        fetch.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                Log.d("fetch",response.body().toString())

                if (response.isSuccessful) {
                    val image = response.body()?.Profile_pic
                    val name = response.body()?.Full_name
                    saveFullName(applicationContext, name.toString())
                    saveProfileImage(applicationContext, "$image")

                    Glide.with(applicationContext).load(image).into(binding.profileEdit)
                    binding.etNameEdit.setText(name)
                    binding.bioEt.setText(response.body()!!.userBio)
                    binding.hotelType.text = (response.body()!!.hotelOwnerInfo.hotelType)
                    binding.website.setText(response.body()!!.hotelOwnerInfo.websiteLink)
                    binding.bookingText.setText(response.body()!!.hotelOwnerInfo.bookingEngineLink)
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*------------------------------CAMERA FUNCTIONALITIES AND SET LOCALE LANGUAGE--------------*/
    private fun openBottomSheet() {
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

    private fun deleteImage() {
        croppedImageUri = null
        binding.profileEdit.setImageURI(croppedImageUri)
        dialog.dismiss()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            if (imageUri != null) {
//                compressImage(imageUri)
                cropImage(imageUri)

            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImage = resultingImage.uri

                compressImage(croppedImage)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }

    private fun cropImage(imageUri: Uri) {
        val options = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)

        options.setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setOutputCompressQuality(20)
            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            .start(this)
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

            croppedImageUri = compressed
            binding.profileEdit.setImageURI(croppedImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }
    private fun openHotelTypeBottom() {

        val dialogRole = Dialog(this)
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.singleHotelRL).setOnClickListener {
            binding.hotelType.text = "Single Hotel"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.hotelChainRL).setOnClickListener {
            binding.hotelType.text = "Hotel Chain"
            dialogRole.dismiss()
        }
    }
}