package app.retvens.rown.Dashboard.createPosts

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.databinding.ActivityCreateClickAndSharePostBinding
import com.bumptech.glide.Glide
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
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

class CreateClickAndSharePostActivity : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener, BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding:ActivityCreateClickAndSharePostBinding

    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var task: ImageView
    var canSee : Int ?= 0

    var selectedImg : Int = 0

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropImage(cameraImageUri)
        }
    }

    private val uCropContract = object : ActivityResultContract<List<Uri>, Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(3F, 4F)
                .withMaxResultSize(800, 800)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, set the phone number to the EditText
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    contract.launch(cameraImageUri)

                }else {
                    // Permission has been denied, handle it accordingly
                    // For example, show a message or disable functionality that requires the permission
                    Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this,"grant permission", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Permission has been denied, handle it accordingly
            // For example, show a message or disable functionality that requires the permission
            Toast.makeText(this,"Something bad", Toast.LENGTH_SHORT).toString()
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClickAndSharePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            contract.launch(cameraImageUri)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        //AutoFetch
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

        binding.imgPreview.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                contract.launch(cameraImageUri)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

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

        binding.editImage.setOnClickListener {
            if (croppedImageUri != null){

                val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)

                dsPhotoEditorIntent.data = croppedImageUri

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Edited Image")

                startActivityForResult(dsPhotoEditorIntent, 100)

            }
        }

        binding.deletePost.setOnClickListener {
            croppedImageUri = null
            binding.imgPreview.setImageURI(croppedImageUri)

            binding.deletePost.visibility = View.GONE
            binding.editImage.visibility = View.GONE
        }

        binding.etLocationPostEvent.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.sharePost.setOnClickListener {

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.canSeeText.text == "Can See"){
                Toast.makeText(applicationContext,"Select Post Seen Status",Toast.LENGTH_SHORT).show()
            }else if (binding.canCommentText.text == "Can comment"){
                Toast.makeText(applicationContext,"Select Comment Status",Toast.LENGTH_SHORT).show()
            }else if (croppedImageUri == null){
                Toast.makeText(applicationContext,"Select Image",Toast.LENGTH_SHORT).show()
//            }else if (binding.etLocationPostEvent.text.toString().isEmpty()){
//                binding.etLocationPostEvent.error = "select location first!!"
            }else{
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                createPost(user_id)
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
            val geocoder = Geocoder(this@CreateClickAndSharePostActivity)
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.locality
                    val state = address.adminArea
                    val country = address.countryName

                    binding.etLocationPostEvent.setText("$city,$state,$country")
                }
            } catch (e: Exception) {

            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                // Location services enabled, proceed to fetch location
                fetchLocation()
            } else {
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val croppedImage = UCrop.getOutput(data!!)
            binding.deletePost.visibility = View.VISIBLE
//            binding.editImage.visibility = View.VISIBLE

            croppedImageUri = app.retvens.rown.utils.compressImage(croppedImage!!, this)
            binding.imgPreview.setImageURI(croppedImageUri)
        } else if (resultCode == UCrop.RESULT_ERROR) {
//                final Throwable cropError = UCrop.getError(data);
        } else if (requestCode == 100) {
                val outputUri: Uri? = data!!.data
                if (outputUri != null) {
                    croppedImageUri = outputUri
                    binding.imgPreview.setImageURI(croppedImageUri)
                }
            }
    }
    fun cropImage(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(filesDir, "croppedImage.jpg").toUri()

        val options : UCrop.Options = UCrop.Options()
        UCrop.of(inputUri, outputUri)
            .withAspectRatio(3F, 4F)
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

    private fun createPost(userId: String) {

        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            croppedImageUri!!,"r",null
        )?:return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"" +
                "")

//        Toast.makeText(applicationContext,userId,Toast.LENGTH_SHORT).show()

        val sendPost  = RetrofitBuilder.feedsApi.createPost(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"share some media"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etLocationPostEvent.text.toString()),
            MultipartBody.Part.createFormData("media", file.name, body)
        )

        sendPost.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,"Post created",Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Something went wrong with Post",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })



    }

    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            if (audienceFrBo == "Connections") {
                binding.canSeeText.text = audienceFrBo
                binding.canCommentText.text = audienceFrBo
            } else {
                binding.canSeeText.text = audienceFrBo
            }
        }else{
            if (audienceFrBo == "Anyone") {
                binding.canCommentText.text = audienceFrBo
                binding.canSeeText.text = audienceFrBo
            } else {
                binding.canCommentText.text = audienceFrBo
            }
        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocationPostEvent.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }
}