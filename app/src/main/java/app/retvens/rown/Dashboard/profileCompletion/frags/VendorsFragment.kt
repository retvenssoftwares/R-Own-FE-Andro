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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
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

class VendorsFragment : Fragment(), BackHandler {

    lateinit var servicesET : TextInputEditText
    private lateinit var selectLogo:ImageView
    private lateinit var setLogo:ShapeableImageView
    lateinit var dialog: Dialog
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    private var logoOfImageUri: Uri? = null
    private lateinit var brandName:TextInputEditText
    private lateinit var brandNameLayout: TextInputLayout
    private lateinit var brandDescriptionLayout: TextInputLayout
    private lateinit var portfolioLayout: TextInputLayout
    private lateinit var websiteLayout: TextInputLayout
    private lateinit var brandDesc:TextInputEditText
    private lateinit var portfolio:TextInputEditText
    private lateinit var website:TextInputEditText

    lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesET = view.findViewById(R.id.vendor_services_et)
        servicesET.setOnClickListener {
            openVendorService()
        }

        selectLogo = view.findViewById(R.id.camera_vendor)
        setLogo = view.findViewById(R.id.hotel_vendor_profile)
        selectLogo.setOnClickListener {
            openBottomCameraSheet()
        }

        brandName = view.findViewById(R.id.brand_name)
        brandNameLayout = view.findViewById(R.id.brand_name_layout)
        brandDesc = view.findViewById(R.id.brand_desc)
        brandDescriptionLayout = view.findViewById(R.id.brand_desc_layout)
        portfolio = view.findViewById(R.id.portfolio_link)
        portfolioLayout = view.findViewById(R.id.portfolio_link_layout)
        website = view.findViewById(R.id.website_link)
        websiteLayout = view.findViewById(R.id.website_link_layout)

        val submit = view.findViewById<CardView>(R.id.card_job_next)
        submit.setOnClickListener {
            if (logoOfImageUri == null){
                Toast.makeText(context, "Please select an Logo", Toast.LENGTH_SHORT).show()
            } else if(brandName.length() < 2){
                brandNameLayout.error = "Please enter your brand name"
            } else if(brandDesc.length() < 3){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.error = "Please enter Description"
            } else if(portfolio.length() < 3){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.isErrorEnabled = false
                portfolioLayout.error = "Please enter your Portfolio Link"
            } else if(website.length() < 3){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.isErrorEnabled = false
                portfolioLayout.isErrorEnabled = false
                websiteLayout.error = "Please enter your website link"
            } else {
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()

                uploadData()
            }
        }
    }

    private fun uploadData() {
        val name = brandName.text.toString()
        val description = brandDesc.text.toString()
        val portfolio = portfolio.text.toString()
        val website = website.text.toString()

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            logoOfImageUri!!,"r",null
        )?:return

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(requireContext().cacheDir, "cropped_${requireContext().contentResolver.getFileName(logoOfImageUri!!)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.profileCompletion.uploadVendorData(user_id,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),description),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),portfolio),
            MultipartBody.Part.createFormData("vendorImage", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"Oo7PCzo0-"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),website)
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
                    startActivity(Intent(requireContext(),DashBoardActivity::class.java))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {

                logoOfImageUri = imageUri
                setLogo.setImageURI(logoOfImageUri)


            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

                    logoOfImageUri = croppedImage

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context,"Try Again : ${resultingImage.error}",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openVendorService() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_vendor_services)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
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
        logoOfImageUri = null
        setLogo.setImageURI(logoOfImageUri)
        dialog.dismiss()
    }
    private fun openCamera() {
        contract.launch(logoOfImageUri)
        dialog.dismiss()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        cropImage(cameraHotelChainImageUri)

            setLogo.setImageURI(logoOfImageUri)
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