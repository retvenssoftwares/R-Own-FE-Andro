package app.retvens.rown.Dashboard.profileCompletion.frags

import android.Manifest
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
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainData
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheet
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetRating
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
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

class HotelOwnerChainFragment : Fragment(), BackHandler, BottomSheetRating.OnBottomRatingClickListener, BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener{

    lateinit var dialog: Dialog
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainCoverImageUri: Uri?= null  // Final Uri for Hotel chain Cover
    private var chainHotelList:MutableList<HotelChainData>? = null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraHotelChainImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropImage(cameraHotelChainImageUri)
        }
    }
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var nameET : TextInputEditText
    lateinit var chainHotelDescriptionET : TextInputEditText
    lateinit var location : TextInputEditText
    lateinit var rating : TextInputEditText

    lateinit var progressDialog : Dialog

    lateinit var nameTIL : TextInputLayout
    lateinit var chainHotelDescriptionLayout : TextInputLayout
    lateinit var locationTIL : TextInputLayout
    lateinit var ratingTIL : TextInputLayout

    lateinit var counterText : TextView

    lateinit var chainCover : ShapeableImageView
    lateinit var task:ImageView
    lateinit var next : CardView
    var n : Int = 2
    var counter : Int = 1
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, set the phone number to the EditText
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    openBottomCameraSheet()

                }else {
                    // Permission has been denied, handle it accordingly
                    // For example, show a message or disable functionality that requires the permission
                    Toast.makeText(context,"permission denied", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(context,"grant permission", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Permission has been denied, handle it accordingly
            // For example, show a message or disable functionality that requires the permission
            Toast.makeText(context,"Something bad", Toast.LENGTH_SHORT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_owner_chain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraHotelChainImageUri = createImageUri()!!

        next = view.findViewById(R.id.card_chain_next)

        nameET = view.findViewById<TextInputEditText>(R.id.chainHotelNameET)
        chainHotelDescriptionET = view.findViewById<TextInputEditText>(R.id.chainHotelDescriptionET)
        location = view.findViewById<TextInputEditText>(R.id.chainHotelLocationET)
        rating = view.findViewById<TextInputEditText>(R.id.chainHotelRatingET)

        nameTIL = view.findViewById<TextInputLayout>(R.id.chainHotelNameLayout)
        chainHotelDescriptionLayout = view.findViewById<TextInputLayout>(R.id.chainHotelDescriptionLayout)
        locationTIL = view.findViewById<TextInputLayout>(R.id.chainHotelLocationLayout)
        ratingTIL = view.findViewById<TextInputLayout>(R.id.chainHotelRatingLayout)

        counterText = view.findViewById(R.id.counter_text)

        task = view.findViewById(R.id.autofetch)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsClient = LocationServices.getSettingsClient(requireContext())

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        task.setOnClickListener {
            checkLocationSettings()
        }

        chainCover = view.findViewById<ShapeableImageView>(R.id.hotel_chain_cover_1)
        chainCover.setOnClickListener { //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openBottomCameraSheet()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        location.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        val decline = view.findViewById<ImageView>(R.id.decline)
        decline.setOnClickListener {
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        }

        rating.setOnClickListener {
            val bottomSheet = BottomSheetRating()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetRating.RATING_TAG)}
            bottomSheet.setOnRatingClickListener(this)
        }

        if(arguments?.getString("hotels") != null){
            val no = arguments?.getString("hotels")
            n = no!!.toInt()
        }

        counterText.text = "$counter/$n"

        next.setOnClickListener {
            if (croppedHotelChainCoverImageUri == null){
                Toast.makeText(context, "Please select an Cover", Toast.LENGTH_SHORT).show()
            } else if(chainHotelDescriptionET.length() < 2){
                chainHotelDescriptionLayout.error = "Please enter Chain name"
            } else if(nameET.length() < 2){
                nameTIL.error = "Please enter Chain name"
            } else if(location.text.toString() == "Select Your Location"){
                locationTIL.error = "Please Select Your Location"
            } else if(rating.text.toString() == "Select Rating"){
                ratingTIL.error = "Please Select Rating"
            } else {
                nameTIL.isErrorEnabled = false
                chainHotelDescriptionLayout.isErrorEnabled = false
                locationTIL.isErrorEnabled = false
                ratingTIL.isErrorEnabled = false
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                setUpChainAdapter()
            }
        }
    }

    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location services enabled, proceed to fetch location
            fetchLocation()
            Log.e("click","1")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("error",sendEx.toString())
                }
            }
        }

    }

    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
                Toast.makeText(requireContext(),"Permission Required",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reverseGeocode(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.Main) {
            val geocoder = Geocoder(requireContext())
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

            }
        }
    }

    private fun setUpChainAdapter() {
        chainHotelList = mutableListOf()

        if (counter <= n){

                uploadData()

        }else {
//            Toast.makeText(context, "All Hotels Uploaded", Toast.LENGTH_SHORT).show()
            profileCompletionStatus = "100"

            progressDialog.dismiss()
            startActivity(Intent(requireContext(),DashBoardActivity::class.java))
            activity?.finish()
        }
    }

    private fun uploadData() {
        val hotelName = nameET.text.toString()
        val hotelDescription = chainHotelDescriptionET.text.toString()
        val chainLocation = location.text.toString()
        val chainRating = rating.text.toString()


        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            croppedHotelChainCoverImageUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(requireContext().cacheDir, "${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.profileCompletion.uploadHotelChainData(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelName),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelDescription),
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
                if (response.isSuccessful && isAdded){
                    profileComStatus(context!!, "100")
                    profileCompletionStatus = "100"

                    if (counter < n) {
                        counter++
                        counterText.text = "$counter/$n"
                    } else {
                        Toast.makeText(context, "All Hotels Uploaded", Toast.LENGTH_SHORT).show()
                        profileCompletionStatus = "100"

                        progressDialog.dismiss()
                        startActivity(Intent(requireContext(),DashBoardActivity::class.java))
                        activity?.finish()
                    }

                    nameTIL.setHint("Hotel $counter Name")
                    chainHotelDescriptionLayout.setHint("Hotel $counter Description")
                    locationTIL.setHint("Hotel $counter Location")
                    ratingTIL.setHint("Hotel $counter Star rating")

                    nameET.setText("")
                    chainHotelDescriptionET.setText("")
                    location.setText("Select Your Location")
                    rating.setText("Select Rating")

                    if (counter == n) {

                    }

                    progressDialog.dismiss()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Try Again",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun handleBackPressed(): Boolean {

        val fragment = HotelOwnerFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true

    }
    private fun openLocationSheet() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_location)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()
//        getUserLocation()
//
//        recyclerView = dialogRole.findViewById(R.id.location_recycler)
//         //recyclerView. //recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun openBottomCameraSheet() {

        dialog = Dialog(requireContext())
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
        croppedHotelChainCoverImageUri = null
        chainCover.setImageURI(croppedHotelChainCoverImageUri)
        dialog.dismiss()
    }
    private fun openCamera() {
        contract.launch(cameraHotelChainImageUri)
        dialog.dismiss()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                cropImage(imageUri)
            }
        }   else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(requireContext(),
            "app.retvens.rown.fileProvider",
            image
        )
    }
    private fun cropImage(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(requireContext().filesDir, "croppedImage.jpg").toUri()

        val options : UCrop.Options = UCrop.Options()
        UCrop.of(inputUri, outputUri)
            .withAspectRatio(16F, 9F)
            .withOptions(options)
            .start(requireContext(), this)
    }
    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,imageUri)
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
            context?.sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    override fun bottomRatingClick(ratingFrBo: String) {
        rating.setText(ratingFrBo)
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
    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        location.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }
}