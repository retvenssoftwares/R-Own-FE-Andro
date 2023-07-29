package app.retvens.rown.NavigationFragments.profile

import android.Manifest
import android.app.Dialog
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
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.frags.BasicInformationFragment
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.databinding.ActivityEditVendorInfoBinding
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import com.bumptech.glide.Glide
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
import java.lang.NullPointerException

class EditVendorInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditVendorInfoBinding

    lateinit var dialog: Dialog
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

    var description = ""
    var website = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVendorInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchUser()

        cameraImageUri = createImageUri()!!

        binding.profileBackBtn.setOnClickListener { onBackPressed() }

        binding.cameraEdit.setOnClickListener {
            selectedImg = 4
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openBottomCameraSheet()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        binding.img1.setOnClickListener {
            selectedImg = 1
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        binding.img2.setOnClickListener {
            selectedImg = 2
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
        binding.img3.setOnClickListener {
            selectedImg = 3
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        binding.deleteImg1.setOnClickListener {
            imagesList.remove(imgUri1)
            imgUri1 = null
            binding.img1.setImageURI(imgUri1)
            binding.deleteImg1.visibility = View.GONE
            selectedImg = 1
        }
        binding.deleteImg2.setOnClickListener {
            imagesList.remove(imgUri2)
            imgUri2 = null
            binding.img2.setImageURI(imgUri2)
            binding.deleteImg2.visibility = View.GONE
            selectedImg = if (imgUri1 == null){
                1
            } else {
                2
            }
        }
        binding.deleteImg3.setOnClickListener {
            imagesList.remove(imgUri3)
            imgUri3 = null
            binding.img3.setImageURI(imgUri3)
            binding.deleteImg3.visibility = View.GONE
            selectedImg = if (imgUri1 == null){
                1
            } else if (imgUri2 == null){
                2
            } else{
                3
            }
        }

        binding.save.isClickable = true

        binding.vendorNameEt.addTextChangedListener {
            binding.save.isClickable = true
        }

        binding.descriptionoEt.addTextChangedListener {
            if (binding.descriptionoEt.text.toString() != description) {
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
        binding.websiteLink.addTextChangedListener {
            if (binding.websiteLink.text.toString() != website) {
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
            if (binding.vendorNameEt.text.isEmpty()){
                Toast.makeText(applicationContext, "Please Enter Vendor Name", Toast.LENGTH_SHORT).show()
//            } else if (binding.websiteLink.text.isEmpty()){
//                Toast.makeText(applicationContext, "Please Enter website", Toast.LENGTH_SHORT).show()
            } else if(binding.descriptionoEt.length() < 3){
                Toast.makeText(applicationContext, "Please enter Description", Toast.LENGTH_SHORT).show()
//            } else if(imagesList.isEmpty()){
//                Toast.makeText(applicationContext, "Please select at least one portfolio image", Toast.LENGTH_SHORT).show()
            } else {
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
        val name = binding.vendorNameEt.text.toString()
        val description = binding.descriptionoEt.text.toString()
        val website = binding.websiteLink.text.toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()


//        imagesList.forEach {
//            fileList.add(prepareFilePart(it, "portfolioLinkdata", applicationContext)!!)
//        }

        if (logoOfImageUri != null && imagesList.isNotEmpty()) {
            val vendorImg = prepareFilePart(logoOfImageUri!!, "Vendorimg", applicationContext)

            if (imagesList.size == 3) {
                val file1 = prepareFilePart(imgUri1!!, "portfolioImages1", applicationContext)
                val file2 = prepareFilePart(imgUri1!!, "portfolioImages2", applicationContext)
                val file3 = prepareFilePart(imgUri1!!, "portfolioImages3", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorData(
                    user_id,
                    vendorImg!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    file2!!,
                    file3!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )

                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else if (imagesList.size == 2) {
                val file1 =
                    prepareFilePart(imagesList.get(0), "portfolioImages1", applicationContext)
                val file2 =
                    prepareFilePart(imagesList.get(1), "portfolioImages2", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorData2(
                    user_id,
                    vendorImg!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    file2!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )
                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            } else if (imagesList.size == 1) {
                val file1 =
                    prepareFilePart(imagesList.get(0), "portfolioImages1", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorData1(
                    user_id,
                    vendorImg!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )

                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

        } else if (logoOfImageUri == null && imagesList.isNotEmpty()) {

            if (imagesList.size == 3) {
                val file1 = prepareFilePart(imgUri1!!, "portfolioImages1", applicationContext)
                val file2 = prepareFilePart(imgUri1!!, "portfolioImages2", applicationContext)
                val file3 = prepareFilePart(imgUri1!!, "portfolioImages3", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorDataWithoutLogo(
                    user_id,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    file2!!,
                    file3!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )

                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else if (imagesList.size == 2) {
                val file1 =
                    prepareFilePart(imagesList.get(0), "portfolioImages1", applicationContext)
                val file2 =
                    prepareFilePart(imagesList.get(1), "portfolioImages2", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorDataWithoutLogo2(
                    user_id,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    file2!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )
                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            } else if (imagesList.size == 1) {
                val file1 =
                    prepareFilePart(imagesList.get(0), "portfolioImages1", applicationContext)
                val send = RetrofitBuilder.profileCompletion.uploadVendorDataWithoutLogo1(
                    user_id,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    file1!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
                )

                send.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        if (response.isSuccessful) {
//                            profileComStatus(applicationContext, "100")
//                            profileCompletionStatus = "100"

                            progressDialog.dismiss()
                            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                            finish()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

        } else if(logoOfImageUri != null && imagesList.isEmpty()) {
            val vendorImg = prepareFilePart(logoOfImageUri!!, "Vendorimg", applicationContext)

            val send = RetrofitBuilder.profileCompletion.updateVendorDataWithoutGallery(
                user_id,
                vendorImg!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
            )
            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful) {
//                        profileComStatus(applicationContext, "100")
//                        profileCompletionStatus = "100"

                        progressDialog.dismiss()
                        startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })

        } else {
            val send = RetrofitBuilder.profileCompletion.updateVendorData(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website)
            )
            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful) {
//                        profileComStatus(applicationContext, "100")
//                        profileCompletionStatus = "100"

                        progressDialog.dismiss()
                        startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
    }
    private fun fetchUser() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val fetch = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)
        fetch.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                Log.d("fetch",response.body().toString())

                if (response.isSuccessful) {
                    if (response.body()!!.vendorInfo.vendorImage.isNotEmpty()) {
                        Glide.with(applicationContext)
                            .load(response.body()!!.vendorInfo.vendorImage).into(binding.brandLogo)
                    }
                    description = (response.body()!!.vendorInfo.vendorDescription)
                    website = (response.body()!!.vendorInfo.websiteLink)

                    binding.vendorNameEt.setText(response.body()!!.vendorInfo.vendorName)
                    binding.descriptionoEt.setText(response.body()!!.vendorInfo.vendorDescription)
                    binding.websiteLink.setText(response.body()!!.vendorInfo.websiteLink)

//                    response.body()!!.vendorInfo.portfolioLink.forEach {
                        if(response.body()!!.vendorInfo.portfolioLink.get(0).Image3.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.body()!!.vendorInfo.portfolioLink.get(0).Image3).into(binding.img3)
                        }
                        if (response.body()!!.vendorInfo.portfolioLink.get(0).Image2.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.body()!!.vendorInfo.portfolioLink.get(0).Image2).into(binding.img2)
                        }
                        if (response.body()!!.vendorInfo.portfolioLink.get(0).Image1.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.body()!!.vendorInfo.portfolioLink.get(0).Image1).into(binding.img1)
                        }

//                    }

                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage, Toast.LENGTH_SHORT).show()
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
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                when (selectedImg) {
                    1 -> {
                        binding.deleteImg1.visibility = View.VISIBLE
                        imgUri1 = compressImage(croppedImage)
                        binding.img1.setImageURI(imgUri1)
                        imagesList.add(imgUri1!!)
                        binding.save.isClickable = true
                        binding.save.setCardBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.green_own
                            )
                        )
                    }
                    2 -> {
                        binding.deleteImg2.visibility = View.VISIBLE
                        binding.img2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imagesList.add(imgUri2!!)
                        binding.save.isClickable = true
                        binding.save.setCardBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.green_own
                            )
                        )
                    }
                    3 -> {
                        binding.deleteImg3.visibility = View.VISIBLE
                        imgUri3 = compressImage(croppedImage)
                        binding.img3.setImageURI(imgUri3)
                        imagesList.add(imgUri3!!)
                        binding.save.isClickable = true
                        binding.save.setCardBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.green_own
                            )
                        )
                    }
                    4 -> {
                        logoOfImageUri = compressImage( croppedImage!!)
                        binding.brandLogo.setImageURI(logoOfImageUri)
                        binding.save.isClickable = true
                        binding.save.setCardBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.green_own
                            )
                        )
                    }
                }

            }else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
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

    private fun openBottomCameraSheet() {

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_camera)

        dialog.findViewById<ImageView>(R.id.delete_img).setOnClickListener {
            deleteImage()
        }

        dialog.findViewById<LinearLayout>(R.id.pick_from_gallery).setOnClickListener {
            openGalleryP()
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
        binding.brandLogo.setImageURI(logoOfImageUri)
        dialog.dismiss()
    }
    private fun openCamera() {
        contract.launch(cameraImageUri)
        dialog.dismiss()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }
    private fun openGalleryP() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    private fun createImageUri(): Uri? {
        val image = File(filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(this,
            "app.retvens.rown.fileProvider",
            image
        )
    }
    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
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
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

}