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
import androidx.core.widget.addTextChangedListener
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEditHotelProfileBinding
import app.retvens.rown.utils.cropProfileImage
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditHotelProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditHotelProfileBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropProfileImage(cameraImageUri, this)
        }
    }
    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog

    var user_id = ""

    var name = ""
    var bio = ""
    var website = ""
    var bookingText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Designation.setOnClickListener {
//            openHotelTypeBottom()
        }

        binding.profileBackBtn.setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id", "").toString()

        binding.refreshLayout.setOnRefreshListener {
            fetchUser(user_id)
            binding.refreshLayout.isRefreshing = false
        }
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

        binding.save.isClickable = false

        binding.etNameEdit.addTextChangedListener {
            if (binding.etNameEdit.text.toString() != name) {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green_own
                    )
                )
                binding.save.isClickable = true
            } else {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_40
                    )
                )
                binding.save.isClickable = false
            }
        }
        binding.bioEt.addTextChangedListener {
            if (binding.bioEt.text.toString() != bio) {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green_own
                    )
                )
                binding.save.isClickable = true
            } else {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_40
                    )
                )
                binding.save.isClickable = false
            }
        }
        binding.website.addTextChangedListener {
            if (binding.website.text.toString() != website) {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green_own
                    )
                )
                binding.save.isClickable = true
            } else {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_40
                    )
                )
                binding.save.isClickable = false
            }
        }
        binding.bookingText.addTextChangedListener {
            if (binding.bookingText.text.toString() != bookingText) {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green_own
                    )
                )
                binding.save.isClickable = true
            } else {
                binding.save.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_40
                    )
                )
                binding.save.isClickable = false
            }
        }

        binding.save.setOnClickListener {

            if (binding.etNameEdit.length() < 3) {
                Toast.makeText(applicationContext, "Please enter your hotel name", Toast.LENGTH_SHORT)
                    .show()
            }
//            else if (binding.hotelType.text.toString() == "Hotel Type") {
//                Toast.makeText(applicationContext, "Please select Hotel Type", Toast.LENGTH_SHORT)
//                    .show()
//            }
            else {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.setCancelable(false)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent)
                    .into(image)
                progressDialog.show()

                if (croppedImageUri == null) {
                    updateHotel(user_id)
                } else {
                    updateHotel(user_id)
                    updateLogo()
                }
            }
        }


    }

    private fun updateLogo() {
        val logo  = prepareFilePart(croppedImageUri!!, "hotelLogo", applicationContext)
        val uL = RetrofitBuilder.ProfileApis.updateHotelLogo("", logo!!)
        uL.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {

            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
            }
        })
    }

    private fun updateHotel(userId: String) {
        val hotel = RetrofitBuilder.ProfileApis.updateHotel(
            userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
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

    private fun fetchUser(userId: String) {
        val fetch = RetrofitBuilder.retrofitBuilder.fetchUser(userId)
        fetch.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                Log.d("fetch",response.body().toString())

                if (response.isSuccessful) {
                    val image = response.body()!!
                    name = response.body()?.hotelOwnerInfo?.hotelownerName.toString()
                    bio = (response.body()!!.hotelOwnerInfo.hotelDescription)
                    website = (response.body()!!.hotelOwnerInfo.websiteLink)
                    bookingText = (response.body()!!.hotelOwnerInfo.bookingEngineLink)

                    Glide.with(applicationContext).load(image.Profile_pic.toString()).into(binding.profileEdit)
                    binding.etNameEdit.setText(name)
                    binding.bioEt.setText(response.body()!!.hotelOwnerInfo.hotelDescription)
                    if(response.body()!!.hotelOwnerInfo.hotelType.isNotEmpty()) {
                        binding.hotelType.text = (response.body()!!.hotelOwnerInfo.hotelType)
                    }
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
                cropProfileImage(imageUri, this)

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
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
        }
        dialogRole.findViewById<RelativeLayout>(R.id.hotelChainRL).setOnClickListener {
            binding.hotelType.text = "Hotel Chain"
            dialogRole.dismiss()
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
        }
    }

}