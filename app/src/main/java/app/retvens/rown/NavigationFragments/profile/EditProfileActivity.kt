package app.retvens.rown.NavigationFragments.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.authentication.UserInterest
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetJobDesignation
import app.retvens.rown.bottomsheet.BottomSheetJobTitle
import app.retvens.rown.databinding.ActivityEditProfileBinding
import app.retvens.rown.utils.cropProfileImage
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.mesibo.api.Mesibo
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException

class EditProfileActivity : AppCompatActivity(), BottomSheetJobTitle.OnBottomJobTitleClickListener,
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding:ActivityEditProfileBinding
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        compressImage(cameraImageUri)
        cropProfileImage(cameraImageUri, this)
    }
    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog
    lateinit var task:ImageView
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private  var pdfUri:Uri? = null
    var PICK_PDF_REQUEST_CODE : Int = 1

    var user_id = ""

    var Gender = ""
    var name = ""
    var bio = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        binding.autofetch.setOnClickListener {
            checkLocationSettings()
        }

        binding.location0.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.Designation.setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        binding.male.setOnClickListener {
            Gender = "Male"
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.female.setOnClickListener {
            Gender = "Female"
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.nonBinary.setOnClickListener {
            Gender = "Non Binary"
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.preferNotSay.setOnClickListener {
            Gender = "Prefer not to say"
            binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.save.isClickable = true
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
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

        binding.save.setOnClickListener {

            if(binding.etNameEdit.length() < 3){
                Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else{
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.setCancelable(false)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()

                uploadData(user_id)
            }
        }

        binding.uploadResume.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                startActivityForResult(intent,PICK_PDF_REQUEST_CODE)
            }

        binding.profileBackBtn.setOnClickListener {
            onBackPressed()
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
            val geocoder = Geocoder(this@EditProfileActivity)
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]

                    val city = address.locality
                    val state = address.adminArea
                    val country = address.countryName


                    binding.location0.setText("$city,$state,$country")
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun uploadData(user_id: String) {

        if (croppedImageUri != null && pdfUri == null){

            val image = prepareFilePart(croppedImageUri!!, "Profile_pic", applicationContext)

            val respo  = RetrofitBuilder.ProfileApis.updateUserProfileWithoutPDF(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.dText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Gender),
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
                        onBackPressed()
                        }

                }
                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                }
            })

        } else if (pdfUri != null && croppedImageUri != null){
            val parcelFileDescriptor = contentResolver.openFileDescriptor(
                pdfUri!!,"r",null
            )?:return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file =  File(cacheDir, "${getRandomString(6)}.pdf")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val body = UploadRequestBody(file,"pdf")

            val imgBody = prepareFilePart(croppedImageUri!!, "Profile_pic", applicationContext)

            val respo  = RetrofitBuilder.ProfileApis.updateUserProfileWithPDFImg(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Gender),
                imgBody!!,
                MultipartBody.Part.createFormData("resume", file.name, body),
            )
            respo.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
//                    Toast.makeText(applicationContext,"user_id : "+user_id, Toast.LENGTH_SHORT).show()
                    Log.d("image file", file.toString())
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
        } else if (pdfUri != null){
            val parcelFileDescriptor = contentResolver.openFileDescriptor(
                pdfUri!!,"r",null
            )?:return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file =  File(cacheDir, "${getRandomString(6)}.pdf")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val body = UploadRequestBody(file,"pdf")

            val respo  = RetrofitBuilder.ProfileApis.updateUserProfileWithPDF(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Gender),
                MultipartBody.Part.createFormData("resume", file.name, body),
            )
            respo.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
//                    Toast.makeText(applicationContext,"user_id : "+user_id, Toast.LENGTH_SHORT).show()
                    Log.d("image file", file.toString())
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
        else {
            val pWithoutImg = RetrofitBuilder.ProfileApis.updateUserProfileWithoutImgPDF(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.etNameEdit.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.bioEt.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Gender),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.dText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.location0.text.toString()),
            )
            pWithoutImg.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
//                    Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, Gender, Toast.LENGTH_SHORT).show()
                        saveFullName(applicationContext, binding.etNameEdit.text.toString())
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
                    name = response.body()?.Full_name.toString()
                    saveFullName(applicationContext, name.toString())
                    saveProfileImage(applicationContext, "$image")
                    val mail = response.body()?.Email
                    Glide.with(applicationContext).load(image).into(binding.profileEdit)
                    binding.etNameEdit.setText(name)
                    binding.bioEt.setText(response.body()!!.userBio)
                    bio = (response.body()!!.userBio)
                    binding.location0.setText(response.body()!!.location)
                    response.body()!!.vendorInfo
                    try {
                        binding.dText.text = response.body()?.normalUserInfo!!.get(0).jobTitle
//                        Toast.makeText(applicationContext, response.body()?.Gender.toString(), Toast.LENGTH_SHORT).show()
                        when (response.body()!!.Gender) {
                            "Male" -> {
                                Gender = "Male"
                                binding.male.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
                            }
                            "Female" -> {
                                Gender = "Female"
                                binding.female.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
                            }
                            "Non Binary" -> {
                                Gender = "Non Binary"
                                binding.nonBinary.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
                            }
                            "Prefer not to say" -> {
                                Gender = "Prefer not to say"
                                binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
                            }
                        }
                    } catch (e : Exception){
                        Log.d("NullPointer", e.toString())
                    }

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
        }   else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            }else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == PICK_PDF_REQUEST_CODE) {
            pdfUri = data?.data
            binding.uploadResume.text = (pdfUri.toString())
            binding.save.isClickable = true
            binding.save.setCardBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.green_own
                )
            )
        }
    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }

    fun compressImage(imageUri: Uri): Uri? {
        var compressed: Uri? = null
        try {
            val imageBitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg", System.currentTimeMillis())
            val finalFile = File(path, fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            croppedImageUri = compressed
            binding.profileEdit.setImageURI(croppedImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return compressed
    }


    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        binding.dText.text = jobTitleFrBo
        binding.save.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
        binding.save.isClickable = true
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.location0.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, DashBoardActivity :: class.java)
        intent.putExtra("frag", "profile")
        startActivity(intent)
        finish()
    }
}