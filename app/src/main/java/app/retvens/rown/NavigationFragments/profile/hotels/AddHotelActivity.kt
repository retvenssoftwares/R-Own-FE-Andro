package app.retvens.rown.NavigationFragments.profile.hotels

import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
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
import android.provider.OpenableColumns
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetRating
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.saveProgress
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

class AddHotelActivity : AppCompatActivity(), BottomSheetRating.OnBottomRatingClickListener, BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainCoverImageUri: Uri?= null  // Final Uri for Hotel chain Cover

    var REQUEST_CAMERA_PERMISSION : Int = 0

    lateinit var nameET : TextInputEditText
    lateinit var location : TextInputEditText
    lateinit var rating : TextInputEditText

    lateinit var progressDialog : Dialog

    lateinit var nameTIL : TextInputLayout
    lateinit var locationTIL : TextInputLayout
    lateinit var ratingTIL : TextInputLayout

    lateinit var chainCover : ShapeableImageView

    lateinit var next :CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener {
            onBackPressed()
        }

        next = findViewById(R.id.next)

        nameET = findViewById<TextInputEditText>(R.id.owner_Hotel)
        location = findViewById<TextInputEditText>(R.id.et_location)
        rating = findViewById<TextInputEditText>(R.id.hotel_owner_star_et)

        nameTIL = findViewById<TextInputLayout>(R.id.owner_Hotel_layout)
        locationTIL = findViewById<TextInputLayout>(R.id.hotel_location_field)
        ratingTIL = findViewById<TextInputLayout>(R.id.hotel_owner_star)

        chainCover = findViewById<ShapeableImageView>(R.id.hotel_owner_cover)
        chainCover.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this.parent ?: this,
                    arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        }
        location.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        rating.setOnClickListener {
            val bottomSheet = BottomSheetRating()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetRating.RATING_TAG)}
            bottomSheet.setOnRatingClickListener(this)
        }

        next.setOnClickListener {
            if (croppedHotelChainCoverImageUri == null){
                Toast.makeText(applicationContext, "Please select an Cover", Toast.LENGTH_SHORT).show()
            } else if(nameET.length() < 2){
                nameTIL.error = "Please enter Chain name"
            } else {
                nameTIL.isErrorEnabled = false
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                uploadData()
            }
        }
    }

    private fun uploadData() {
        val hotelName = nameET.text.toString()
        val chainLocation = location.text.toString()
        val chainRating = rating.text.toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val parcelFileDescriptor = this.contentResolver.openFileDescriptor(
            croppedHotelChainCoverImageUri!!,"r",null
        )?:return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(applicationContext.cacheDir, "${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.profileCompletion.addHotelProfile(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelName),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),chainLocation),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),chainRating),
            MultipartBody.Part.createFormData("hotelCoverpic", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id)
        )

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                cropImage(imageUri)
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

                compressImage(croppedImage)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(applicationContext,"Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cropImage(imageUri: Uri) {
        val options = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.OFF).also {

                it.setAspectRatio(4, 3)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setOutputCompressQuality(20)
                    .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    .start(this)
            }
    }
    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(applicationContext?.contentResolver,imageUri)
            val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg",System.currentTimeMillis())
            val finalFile = File(path,fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            croppedHotelChainCoverImageUri = compressed
            chainCover.setImageURI(croppedHotelChainCoverImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            applicationContext?.sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    private fun ContentResolver.getFileName(coverPhotoPart: Uri): String {

        var name = ""
        val returnCursor = this.query(coverPhotoPart,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
    override fun bottomRatingClick(ratingFrBo: String) {
        rating.setText(ratingFrBo)
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        location.setText(CountryStateCityFrBo)
    }
}