package app.retvens.rown.Dashboard.profileCompletion.frags

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetServiceName
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yalantis.ucrop.UCrop
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

class VendorsFragment : Fragment(), BackHandler, BottomSheetServiceName.OnBottomSNClickListener {

    lateinit var servicesLayout : TextInputLayout
    lateinit var servicesET : TextInputEditText
    private lateinit var selectLogo:ImageView
    private lateinit var setLogo:ShapeableImageView
    lateinit var dialog: Dialog
    private lateinit var brandName:TextInputEditText
    private lateinit var brandNameLayout: TextInputLayout
    private lateinit var brandDescriptionLayout: TextInputLayout
    private lateinit var portfolioLayout: TextInputLayout
    private lateinit var websiteLayout: TextInputLayout
    private lateinit var brandDesc:TextInputEditText
    private lateinit var portfolio:TextInputEditText
    private lateinit var website:TextInputEditText
    lateinit var progressDialog : Dialog

    private var cameraImageUri: Uri? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropImage(cameraImageUri!!)
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
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    contract.launch(cameraImageUri)

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

    var REQUEST_CAMERA_PERMISSION : Int = 0
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    private var logoOfImageUri: Uri? = null //finalUri

    //Cropped image uri
    private var croppedImageUri: Uri?= null

    private var imgUri1 : Uri?= null   // Final uri for img1
    private var imgUri2 : Uri?= null   // Final uri for img2
    private var imgUri3 : Uri?= null   // Final uri for img3

    private var selectedImg = 0

    private var fileList : ArrayList<MultipartBody.Part> = ArrayList()
    private var imagesList : ArrayList<Uri> = ArrayList()

    private lateinit var img1 : ImageView
    private lateinit var img2 : ImageView
    private lateinit var img3 : ImageView

    private lateinit var deleteImg1 : ImageView
    private lateinit var deleteImg2 : ImageView
    private lateinit var deleteImg3 : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraImageUri = createImageUri()!!

        img1 = view.findViewById(R.id.img_1)
        img2 = view.findViewById(R.id.img_2)
        img3 = view.findViewById(R.id.img_3)

        deleteImg1 = view.findViewById(R.id.delete_img1)
        deleteImg2 = view.findViewById(R.id.delete_img2)
        deleteImg3 = view.findViewById(R.id.delete_img3)

        servicesLayout = view.findViewById(R.id.serviceLayout)
        servicesET = view.findViewById(R.id.vendor_services_et)
        servicesET.setOnClickListener {
            val bottomSheet = BottomSheetServiceName()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetServiceName.SN_TAG)}
            bottomSheet.setOnSNclickListener(this)
        }

        selectLogo = view.findViewById(R.id.camera_vendor)
        setLogo = view.findViewById(R.id.hotel_vendor_profile)
        selectLogo.setOnClickListener {
            selectedImg = 4
            //Requesting Permission For CAMERA
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

        brandName = view.findViewById(R.id.brand_name)
        brandNameLayout = view.findViewById(R.id.brand_name_layout)
        brandDesc = view.findViewById(R.id.brand_desc)
        brandDescriptionLayout = view.findViewById(R.id.brand_desc_layout)
        portfolio = view.findViewById(R.id.portfolio_link)
        portfolioLayout = view.findViewById(R.id.portfolio_link_layout)
        website = view.findViewById(R.id.website_link)
        websiteLayout = view.findViewById(R.id.website_link_layout)

        img1.setOnClickListener {
            selectedImg = 1
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        img2.setOnClickListener {
            selectedImg = 2
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        img3.setOnClickListener {
            selectedImg = 3
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        deleteImg1.setOnClickListener {
            imagesList.remove(imgUri1)
            imgUri1 = null
            img1.setImageURI(imgUri1)
            deleteImg1.visibility = View.GONE
            selectedImg = 1
        }
        deleteImg2.setOnClickListener {
            imagesList.remove(imgUri2)
            imgUri2 = null
            img2.setImageURI(imgUri2)
            deleteImg2.visibility = View.GONE
            selectedImg = if (imgUri1 == null){
                1
            } else {
                2
            }
        }
        deleteImg3.setOnClickListener {
            imagesList.remove(imgUri3)
            imgUri3 = null
            img3.setImageURI(imgUri3)
            deleteImg3.visibility = View.GONE
            selectedImg = if (imgUri1 == null){
                1
            } else if (imgUri2 == null){
                2
            } else{
                3
            }
        }

        val submit = view.findViewById<CardView>(R.id.card_job_next)
        submit.setOnClickListener {
            if (logoOfImageUri == null){
                Toast.makeText(context, "Please select an Logo", Toast.LENGTH_SHORT).show()
            } else if(brandName.length() < 2){
                brandNameLayout.error = "Please enter your brand name"
            } else if(brandDesc.length() < 3){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.error = "Please enter Description"
            } else if(servicesET.length() < 6){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.isErrorEnabled = false
                portfolioLayout.isErrorEnabled = false
                servicesLayout.error = "Please enter an valid website link"
            } else if(website.length() < 6){
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.isErrorEnabled = false
                portfolioLayout.isErrorEnabled = false
                servicesLayout.isErrorEnabled = false
                websiteLayout.error = "Please enter an valid website link"
            } else if(imagesList.isEmpty()){
                Toast.makeText(context, "Please select at least one portfolio image", Toast.LENGTH_SHORT).show()
            } else {
                brandNameLayout.isErrorEnabled = false
                brandDescriptionLayout.isErrorEnabled = false
                portfolioLayout.isErrorEnabled = false
                servicesLayout.isErrorEnabled = false
                websiteLayout.isErrorEnabled = false
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
        val file =  File(requireContext().cacheDir, "cropped_${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        imagesList.forEach {
            fileList.add(prepareFilePart(it, "portfolioLinkdata", requireContext())!!)
        }

        val send = RetrofitBuilder.profileCompletion.uploadVendorData(user_id,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),description),
            MultipartBody.Part.createFormData("Vendorimg", file.name, body),
            fileList,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),website)
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
                cropImage(logoOfImageUri!!)
//                setLogo.setImageURI(logoOfImageUri)


            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                when (selectedImg) {
                    1 -> {
                        deleteImg1.visibility = View.VISIBLE
                        imgUri1 = compressImage(croppedImage)
                        img1.setImageURI(imgUri1)
                        imagesList.add(imgUri1!!)
                    }
                    2 -> {
                        deleteImg2.visibility = View.VISIBLE
                        img2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imagesList.add(imgUri2!!)
                    }
                    3 -> {
                        deleteImg3.visibility = View.VISIBLE
                        imgUri3 = compressImage(croppedImage)
                        img3.setImageURI(imgUri3)
                        imagesList.add(imgUri3!!)
                    }
                    4 -> {
                        logoOfImageUri = compressImage( croppedImage!!)
                        setLogo.setImageURI(logoOfImageUri)
                    }
                }

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()
            }
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
        contract.launch(cameraImageUri)
        dialog.dismiss()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    private fun createImageUri(): Uri? {
        val image = File(context?.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(requireContext(),
            "app.retvens.rown.fileProvider",
            image
        )
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

            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            context?.sendBroadcast(intent)
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

    override fun bottomSNClick(serviceName: String) {
        servicesET.setText(serviceName)
    }

}