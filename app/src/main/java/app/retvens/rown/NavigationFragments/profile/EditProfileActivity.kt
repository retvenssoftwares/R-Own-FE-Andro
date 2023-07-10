package app.retvens.rown.NavigationFragments.profile

import android.app.Activity
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
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.authentication.UserInterest
import app.retvens.rown.bottomsheet.BottomSheetJobDesignation
import app.retvens.rown.bottomsheet.BottomSheetJobTitle
import app.retvens.rown.databinding.ActivityEditProfileBinding
import app.retvens.rown.utils.cropProfileImage
import app.retvens.rown.utils.getRandomString
import app.retvens.rown.utils.prepareFilePart
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import com.bumptech.glide.Glide
import com.mesibo.api.Mesibo
import com.yalantis.ucrop.UCrop
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

class EditProfileActivity : AppCompatActivity(), BottomSheetJobTitle.OnBottomJobTitleClickListener {
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

    private  var pdfUri:Uri? = null
    var PICK_PDF_REQUEST_CODE : Int = 1

    var user_id = ""

    var Gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.Designation.setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        binding.male.setOnClickListener {
            Gender = "Male"
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.female.setOnClickListener {
            Gender = "Female"
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.nonBinary.setOnClickListener {
            Gender = "Non Binary"
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.preferNotSay.setOnClickListener {
            Gender = "Prefer not to say"
            binding.male.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.female.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.nonBinary.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.preferNotSay.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
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
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),binding.dText.text.toString())
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
                    val name = response.body()?.Full_name
                    saveFullName(applicationContext, name.toString())
                    saveProfileImage(applicationContext, "$image")
                    val mail = response.body()?.Email
                    Glide.with(applicationContext).load(image).into(binding.profileEdit)
                    binding.etNameEdit.setText(name)
                    binding.bioEt.setText(response.body()!!.userBio)
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
                    } catch (e : NullPointerException){
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
        }
    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }

    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
            val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg",System.currentTimeMillis())
            val finalFile = File(path,fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            croppedImageUri = compressed
            binding.profileEdit.setImageURI(croppedImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        binding.dText.text = jobTitleFrBo
    }
}