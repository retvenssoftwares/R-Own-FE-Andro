package app.retvens.rown.NavigationFragments.profile.hotels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetRating
import app.retvens.rown.utils.getRandomString
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    lateinit var task:ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var chainCover : ShapeableImageView

    lateinit var next :CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener {
            onBackPressed()
        }

        task = findViewById(R.id.autofetch)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        task.setOnClickListener {
            checkLocationSettings()
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

    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location services enabled, proceed to fetch location
            fetchLocation()
            Log.e("click","1")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("error",sendEx.toString())
                }
            }
        }
    }

    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            Log.e("click","2")
        } else {
            Log.e("click","3")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.e("latitude",latitude.toString())
                    Log.e("longitude",longitude.toString())

                    reverseGeocode(latitude, longitude)
                    Log.e("click","4")
                }else{
                    Log.e("click","5")
                }
            }.addOnFailureListener { exception: Exception ->
                Log.e("error",exception.toString())
                Toast.makeText(applicationContext,"Permission Required",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reverseGeocode(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.Main) {
            val geocoder = Geocoder(this@AddHotelActivity)
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]

                    val city = address.locality
                    val state = address.adminArea
                    val country = address.countryName


                    location.setText("$city,$state,$country")
                }
            } catch (e: Exception) {
                    Log.e("error",e.message.toString())
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
                cropImageHorizontal(imageUri)
            }
        }   else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cropImageHorizontal(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(filesDir, "croppedImage.jpg").toUri()

        val options : UCrop.Options = UCrop.Options()
        UCrop.of(inputUri, outputUri)
            .withAspectRatio(16F, 9F)
            .withMaxResultSize(2000,2000)
            .withOptions(options)
            .start(this)

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

    override fun selectlocation(latitude: String, longitude: String) {

    }
}