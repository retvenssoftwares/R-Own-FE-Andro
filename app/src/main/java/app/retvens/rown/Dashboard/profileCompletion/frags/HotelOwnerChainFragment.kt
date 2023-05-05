package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
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
import androidx.core.content.FileProvider
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
import app.retvens.rown.bottomsheet.BottomSheetRating
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class HotelOwnerChainFragment : Fragment(), BackHandler, BottomSheetRating.OnBottomRatingClickListener{

    lateinit var dialog: Dialog
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainCoverImageUri: Uri?= null  // Final Uri for Hotel chain Cover
    private var chainHotelList:MutableList<HotelChainData>? = null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraHotelChainImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        croppedHotelChainCoverImageUri = cameraHotelChainImageUri
        chainCover.setImageURI(croppedHotelChainCoverImageUri)
    }

    lateinit var nameET : TextInputEditText
    lateinit var location : TextInputEditText
    lateinit var rating : TextInputEditText

    lateinit var progressDialog : Dialog

    lateinit var nameTIL : TextInputLayout
    lateinit var locationTIL : TextInputLayout
    lateinit var ratingTIL : TextInputLayout

    lateinit var counterText : TextView

    lateinit var chainCover : ShapeableImageView

    lateinit var next : CardView
    var n : Int = 2
    var counter : Int = 1

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
        location = view.findViewById<TextInputEditText>(R.id.chainHotelLocationET)
        rating = view.findViewById<TextInputEditText>(R.id.chainHotelRatingET)

        nameTIL = view.findViewById<TextInputLayout>(R.id.chainHotelNameLayout)
        locationTIL = view.findViewById<TextInputLayout>(R.id.chainHotelLocationLayout)
        ratingTIL = view.findViewById<TextInputLayout>(R.id.chainHotelRatingLayout)

        counterText = view.findViewById(R.id.counter_text)

        chainCover = view.findViewById<ShapeableImageView>(R.id.hotel_chain_cover_1)
        chainCover.setOnClickListener {
            openBottomCameraSheet()
        }
        location.setOnClickListener {
            openLocationSheet()
        }

        rating.setOnClickListener {
            val bottomSheet = BottomSheetRating()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetRating.RATING_TAG)}
        }

        if(arguments?.getString("hotels") != null){
            val no = arguments?.getString("hotels")
            n = no!!.toInt()
        }

        counterText.text = "$counter/$n"

        next.setOnClickListener {
            if (croppedHotelChainCoverImageUri == null){
                Toast.makeText(context, "Please select an Cover", Toast.LENGTH_SHORT).show()
            } else if(nameET.length() < 2){
                nameTIL.error = "Please enter Chain name"
            } else {
                nameTIL.isErrorEnabled = false
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
//                progressDialog.show()
                setUpChainAdapter()
            }
        }
    }

    private fun setUpChainAdapter() {
        chainHotelList = mutableListOf()

        if (counter < n){

            counter++
            nameTIL.setHint("Hotel $counter Name")
            locationTIL.setHint("Hotel $counter Location")
            ratingTIL.setHint("Hotel $counter Star rating")

            uploadData()
        }else {
            Toast.makeText(context, "All Hotels Uploaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val hotelName = nameET.text.toString()
        val chainLocation = location.text.toString()
        val chainRating = rating.text.toString()


        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            croppedHotelChainCoverImageUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(requireContext().cacheDir, "cropped_${requireContext().contentResolver.getFileName(croppedHotelChainCoverImageUri!!)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.profileCompletion.uploadHotelChainData(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),hotelName),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),chainLocation),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),chainRating),
            MultipartBody.Part.createFormData("hotelProfilepic", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id)
        )

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
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
//        recyclerView.setHasFixedSize(true)
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
                croppedHotelChainCoverImageUri = imageUri
                chainCover.setImageURI(croppedHotelChainCoverImageUri)
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri
                croppedHotelChainCoverImageUri = croppedImage
                chainCover.setImageURI(croppedHotelChainCoverImageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context,"Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()
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

    override fun BottomRatongClick(ratingFrBo: String) {
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
}