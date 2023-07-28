package app.retvens.rown.Dashboard.profileCompletion.frags

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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.DataCollections.ProfileCompletion.HotelOwnerInfoData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetLocation
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

class HotelOwnerFragment : Fragment(), BackHandler,
    BottomSheetRating.OnBottomRatingClickListener,
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener{


    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var task:ImageView
    lateinit var hotelType : TextInputEditText
    lateinit var singleHotelLayout : ConstraintLayout
    lateinit var chainHotelLayout : ConstraintLayout
    private var nextScreen : Int = 0
    private var cameraUser : String = ""
    lateinit var dialog: Dialog


    lateinit var progressDialog : Dialog

    /*-----------------------SINGLE HOTEL----------------------------------*/
    /*-----------------------SINGLE HOTEL--------------------------------*/
    lateinit var noOfHotels : TextInputEditText
    lateinit var hotelOwnerStarET : TextInputEditText
    lateinit var chainHotelDescriptionLayout : TextInputLayout
    lateinit var chainHotelDescriptionET : TextInputEditText
    lateinit var hotelOwnerLocationET : TextInputEditText
    lateinit var hotelOwnerProfile  : ShapeableImageView
    lateinit var hotelOwnerCover  : ShapeableImageView
    lateinit var ownerHotel : TextInputEditText
    lateinit var ownerHotelLayout: TextInputLayout
    lateinit var hotelOwnerStarLayout: TextInputLayout
    lateinit var hotelOwnerLocationLayout: TextInputLayout

    private var croppedOwnerProfileImageUri: Uri? = null // Final Uri for Owner Profile
    private var croppedOwnerCoverImageUri: Uri? = null  // Final Uri for Owner Cover
    /*-----------------------HOTEL CHAIN--------------------------------*/
    /*-----------------------HOTEL CHAIN--------------------------------*/

    lateinit var chainName:TextInputEditText
    lateinit var website:TextInputEditText
    lateinit var bookingLink:TextInputEditText

    lateinit var chainNameLayout : TextInputLayout
    lateinit var websiteLayout : TextInputLayout
    lateinit var bookingLinkLayout : TextInputLayout

    lateinit var cameraHotelChain : ImageView
    lateinit var hotelChainProfile : ShapeableImageView
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainImageUri: Uri?= null  // Final Uri for Hotel chain

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraHotelChainImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropImage(cameraHotelChainImageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_owner, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownerHotel = view.findViewById(R.id.owner_Hotel)
        ownerHotelLayout = view.findViewById(R.id.owner_Hotel_layout)

        chainHotelDescriptionLayout = view.findViewById<TextInputLayout>(R.id.chainHotelDescriptionLayout)
        chainHotelDescriptionET = view.findViewById<TextInputEditText>(R.id.chainHotelDescriptionET)

        chainName = view.findViewById(R.id.hotelchain_Name)
        chainNameLayout = view.findViewById(R.id.hotelchain_Name_layout)
        website = view.findViewById(R.id.hotel_Website)
        websiteLayout = view.findViewById(R.id.hotel_Website_layout)
        bookingLink = view.findViewById(R.id.hotel_bokkinglink)
        bookingLinkLayout = view.findViewById(R.id.hotel_bokkinglink_layout)

        hotelType = view.findViewById<TextInputEditText>(R.id.hotel_type_et)
        singleHotelLayout = view.findViewById(R.id.cons_single_hotel)
        chainHotelLayout = view.findViewById(R.id.cons_chain_hotel)
        hotelType.setOnClickListener {
            openHotelTypeBottom()
        }

        /*-----------------------SINGLE HOTEL--------------------------------*/
        /*-----------------------SINGLE HOTEL--------------------------------*/
        noOfHotels = view.findViewById<TextInputEditText>(R.id.noOfHotels)
        hotelOwnerStarET = view.findViewById<TextInputEditText>(R.id.hotel_owner_star_et)
        hotelOwnerStarLayout = view.findViewById(R.id.hotel_owner_star)
        hotelOwnerLocationLayout = view.findViewById(R.id.hotel_location_field)
        hotelOwnerLocationET = view.findViewById(R.id.et_location)

        hotelOwnerLocationET.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        val decline = view.findViewById<ImageView>(R.id.decline)
        decline.setOnClickListener {
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        }

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

        hotelOwnerStarET.setOnClickListener {
            val bottomSheet = BottomSheetRating()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetRating.RATING_TAG)}
            bottomSheet.setOnRatingClickListener(this)
        }

        hotelOwnerProfile = view.findViewById(R.id.hotel_owner_profile)
        hotelOwnerProfile.setOnClickListener {
            cameraUser = "OwnerProfile" //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openBottomCameraSheet("OwnerProfile")
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        hotelOwnerCover = view.findViewById(R.id.hotel_owner_cover)
        hotelOwnerCover.setOnClickListener {
            cameraUser = "OwnerCover"
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openBottomCameraSheet("OwnerCover")
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        /*-----------------------HOTEL CHAIN--------------------------------*/
        /*-----------------------HOTEL CHAIN--------------------------------*/
        cameraHotelChainImageUri = createImageUri()!!

        hotelChainProfile = view.findViewById(R.id.hotel_chain_profile)
        cameraHotelChain = view.findViewById(R.id.camera_hotelChain)
        cameraHotelChain.setOnClickListener {
            cameraUser = "ChainProfile"
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openBottomCameraSheet("ChainProfile")
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        view.findViewById<CardView>(R.id.card_owner_next).setOnClickListener {
            if (nextScreen == 1){
                val hotels = noOfHotels.text.toString()

                    if (croppedHotelChainImageUri == null){
                        Toast.makeText(context, "Please select an logo", Toast.LENGTH_SHORT).show()
                    } else if(chainName.length() < 2){
                        chainNameLayout.error = "Please enter Chain name"
                    } else if(website.length() < 2){
                        websiteLayout.error = "Website"
                    } else if(bookingLink.length() < 2){
                        bookingLinkLayout.error = "Booking Link"
                    } else if (hotels.isEmpty()){
                        Toast.makeText(requireContext(), "input hotels number", Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog = Dialog(requireContext())
                        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        progressDialog.setCancelable(false)
                        progressDialog.setContentView(R.layout.progress_dialoge)
                        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                        Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                        progressDialog.show()

                        UploadChainData()
                    }
            }else {
                if (croppedOwnerProfileImageUri == null) {
                    Toast.makeText(context, "Please select an logo", Toast.LENGTH_SHORT).show()
                } else if (croppedOwnerCoverImageUri == null) {
                    Toast.makeText(context, "Please select an Cover Image", Toast.LENGTH_SHORT)
                        .show()
                } else if (ownerHotel.length() < 2) {
                    ownerHotelLayout.error = "Please enter owner name"
                } else if (chainHotelDescriptionET.length() < 2) {
                    chainHotelDescriptionLayout.error = "Please enter owner name"
                } else if (hotelOwnerStarET.text.toString() == "Select Your Hotel Rating") {
                    ownerHotelLayout.isErrorEnabled = false
                    hotelOwnerStarLayout.error = "Select Your Hotel Rating"
                } else if (hotelOwnerLocationET.text.toString() == "Select Your Location") {
                    ownerHotelLayout.isErrorEnabled = false
                    hotelOwnerStarLayout.isErrorEnabled = false
                    hotelOwnerLocationLayout.error = "Select Your Location"
                } else {
                    progressDialog = Dialog(requireContext())
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    progressDialog.setCancelable(false)
                    progressDialog.setContentView(R.layout.progress_dialoge)
                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                    Glide.with(requireContext()).load(R.drawable.animated_logo_transparent)
                        .into(image)
                    progressDialog.show()
                    UploadData()
                }
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


                    hotelOwnerLocationET.setText("$city,$state,$country")
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun UploadChainData() {

        val type = hotelType.text.toString()
        val number = noOfHotels.text.toString()
        val name = chainName.text.toString()
        val website = website.text.toString()
        val book = bookingLink.text.toString()

        val data = HotelOwnerInfoData(name,type,number,website,book)

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val update = RetrofitBuilder.profileCompletion.setHotelInfo(user_id,data)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    progressDialog.dismiss()
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    val fragment = HotelOwnerChainFragment()

                    val bundle = Bundle()
                    bundle.putString("hotels", number)

                    fragment.arguments = bundle

                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun UploadData() {



        val type = hotelType.text.toString()
        val hotelName = ownerHotel.text.toString()
        val hotelDescription = chainHotelDescriptionET.text.toString()
        val star = hotelOwnerStarET.text.toString()
        val location = hotelOwnerLocationET.text.toString()

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            croppedOwnerCoverImageUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(requireContext().cacheDir, "${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val parcelFileDescriptor1 = requireContext().contentResolver.openFileDescriptor(
            croppedOwnerProfileImageUri!!,"r",null
        )?:return

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val inputStream1 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
        val file1 = File(requireContext().cacheDir, "${getRandomString(6)}.jpg")
        val outputStream1 = FileOutputStream(file1)
        inputStream1.copyTo(outputStream1)
        val body1 = UploadRequestBody(file1,"image")

        val send = RetrofitBuilder.profileCompletion.uploadHotelData(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelName),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelDescription),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),location),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),star),
            MultipartBody.Part.createFormData("hotelLogo", file1.name, body1),
            MultipartBody.Part.createFormData("hotelCoverpic", file.name, body),
        )

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){

                    profileComStatus(context!!, "100")
                    profileCompletionStatus = "100"

                    progressDialog.dismiss()
                    startActivity(Intent(requireContext(),DashBoardActivity::class.java))
                    activity?.finish()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),response.message().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun openBottomCameraSheet(user : String) {

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
        croppedHotelChainImageUri = null
        hotelChainProfile.setImageURI(croppedHotelChainImageUri)
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
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cropImage(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(requireContext().filesDir, "croppedImage.jpg").toUri()

        UCrop.of(inputUri, outputUri)
            .withAspectRatio(1F, 1F)
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

            Log.d("owner", "cameraUser")
            if (cameraUser == "ChainProfile") {
                Log.d("owner", cameraUser)
                croppedHotelChainImageUri = compressed
                hotelChainProfile.setImageURI(croppedHotelChainImageUri)
            }else if (cameraUser == "OwnerProfile"){
                Log.d("owner", cameraUser)
                croppedOwnerProfileImageUri = compressed
                hotelOwnerProfile.setImageURI(croppedOwnerProfileImageUri)
            }else{
                Log.d("owner", cameraUser)
                croppedOwnerCoverImageUri = compressed
                hotelOwnerCover.setImageURI(croppedOwnerCoverImageUri)
            }

            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            context?.sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(requireContext(),
            "app.retvens.rown.fileProvider",
            image
        )
    }


    private fun openBottomStarSelection() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_rating)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.oneStar).setOnClickListener {
            hotelOwnerStarET.setText("1 -3 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.fourStar).setOnClickListener {
            hotelOwnerStarET.setText("4 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.fiveStar).setOnClickListener {
            hotelOwnerStarET.setText("5 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.sixStar).setOnClickListener {
            hotelOwnerStarET.setText("6 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.sevenStar).setOnClickListener {
            hotelOwnerStarET.setText("7 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.eightStar).setOnClickListener {
            hotelOwnerStarET.setText("8 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.nineStar).setOnClickListener {
            hotelOwnerStarET.setText("9 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.tenStar).setOnClickListener {
            hotelOwnerStarET.setText("10 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.elevenStar).setOnClickListener {
            hotelOwnerStarET.setText(">11 Star")
            dialogRole.dismiss()
        }

    }
    private fun openHotelTypeBottom() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.singleHotelRL).setOnClickListener {
            chainHotelLayout.visibility = View.GONE
            singleHotelLayout.visibility = View.VISIBLE
            nextScreen = 0
            hotelType.setText("Single Hotel")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.hotelChainRL).setOnClickListener {
            chainHotelLayout.visibility = View.VISIBLE
            singleHotelLayout.visibility = View.GONE
            nextScreen = 1
            hotelType.setText("Hotel Chain")
            dialogRole.dismiss()
        }
    }

    override fun handleBackPressed(): Boolean {

        val fragment = BasicInformationFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true
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
        hotelOwnerStarET.setText(ratingFrBo)
    }


    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        hotelOwnerLocationET.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

}