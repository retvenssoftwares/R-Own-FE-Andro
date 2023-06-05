package app.retvens.rown.Dashboard.profileCompletion.frags

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.VendorServicesAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.CreateVendorDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.PostVendorSerivces
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ProfileCompletion.VendorServicesData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetServiceName
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
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

class VendorsFragment : Fragment(), BackHandler, BottomSheetServiceName.OnBottomSNClickListener {

    lateinit var servicesET : TextInputEditText
    private lateinit var selectLogo:ImageView
    private lateinit var setLogo:ShapeableImageView
    lateinit var dialog: Dialog
    var REQUEST_CAMERA_PERMISSION : Int = 0
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    private var logoOfImageUri: Uri? = null //finalUri
    private lateinit var brandName:TextInputEditText
    private lateinit var brandNameLayout: TextInputLayout
    private lateinit var brandDescriptionLayout: TextInputLayout
    private lateinit var portfolioLayout: TextInputLayout
    private lateinit var websiteLayout: TextInputLayout
    private lateinit var brandDesc:TextInputEditText
    private lateinit var portfolio:TextInputEditText
    private lateinit var website:TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorServicesAdapter: VendorServicesAdapter
    private  var selectedServices:ArrayList<String> = ArrayList()
    private var setectedIds:ArrayList<String> = ArrayList()
    private var vendorId:String = ""
    private lateinit var searchBar:EditText

    lateinit var progressDialog : Dialog

    private var cameraImageUri: Uri? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropImage(cameraImageUri!!)
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

        getVendor()
    }


    private fun getVendor() {
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val data = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        data.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    vendorId = response.vendorInfo.vendor_id
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun sendServices() {

        for (x in setectedIds){
//
//            Toast.makeText(requireContext(),x.toString(),Toast.LENGTH_SHORT).show()


            val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            val data = PostVendorSerivces(user_id,x)

            val send = RetrofitBuilder.profileCompletion.setServices(vendorId,data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful){
                        val response = response.body()!!
                        Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })

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
            MultipartBody.Part.createFormData("Vendorimg", file.name, body)

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
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

                    logoOfImageUri = croppedImage
                compressImage(logoOfImageUri!!)
//                setLogo.setImageURI(logoOfImageUri)

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

        recyclerView = dialogRole.findViewById(R.id.brand_services)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchBar = dialogRole.findViewById(R.id.searchServices)

        getServices()



    }

    private fun getServices() {
        val data = RetrofitBuilder.profileCompletion.getServices()

        data.enqueue(object : Callback<List<VendorServicesData>?>,
            VendorServicesAdapter.OnJobClickListener {
            override fun onResponse(
                call: Call<List<VendorServicesData>?>,
                response: Response<List<VendorServicesData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    val originalData = response.toList()
                    vendorServicesAdapter = VendorServicesAdapter(requireContext(),response)
                    vendorServicesAdapter.notifyDataSetChanged()
                    recyclerView.adapter = vendorServicesAdapter

                    vendorServicesAdapter.setOnJobClickListener(this)

                    searchBar.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            val filterData = originalData.filter { item ->
                                item.service_name.contains(p0.toString(),ignoreCase = true)
                            }

                            vendorServicesAdapter.updateData(filterData)
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<VendorServicesData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onJobClick(job: VendorServicesData) {
                val index = selectedServices.indexOf(job.service_name)
                val index1 = setectedIds.indexOf(job.serviceId)
                if (index == -1) {
                    selectedServices.add(job.service_name)
                    setectedIds.add(job.serviceId)
                } else {
                    selectedServices.removeAt(index)
                    setectedIds.removeAt(index1)
                }

                Log.e("serives",selectedServices.toString())

                servicesET.setText(selectedServices.toString())
            }
        })
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
        val options = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.OFF).also {

                it.setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setOutputCompressQuality(20)
                    .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    .start(requireContext(), this)
            }
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

            logoOfImageUri = compressed
            setLogo.setImageURI(logoOfImageUri)
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