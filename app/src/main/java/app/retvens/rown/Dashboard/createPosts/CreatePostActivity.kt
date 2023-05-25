package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
import android.content.ContentResolver
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
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetGoingBack
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.bottomsheet.BottomSheetWhatToPost
import app.retvens.rown.databinding.ActivityCreatePostBinding
import com.bumptech.glide.Glide
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
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

class CreatePostActivity : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener {
    lateinit var binding: ActivityCreatePostBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    private var imgUriP : Uri?= null
    private var imgUri1 : Uri?= null   // Final uri for img1
    private var imgUri2 : Uri?= null   // Final uri for img2
    private var imgUri3 : Uri?= null   // Final uri for img3
    private var imgUri4 : Uri?= null   // Final uri for img4
    private var imgUri5 : Uri?= null   // Final uri for img5

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri

    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropImage(cameraImageUri)
    }

    var canSee : Int ?= 0

    var selectedImg : Int = 0

    var isEvent : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(applicationContext).load(profilePic).into(binding.userCompleteProfile)
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

        binding.img1.setOnClickListener {
            selectedImg = 1
            if (imgUri1 == null) {
                //Requesting Permission For CAMERA
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                    )
                }
                openGallery()
            } else {
                imgUriP = imgUri1
                binding.imgPreview.setImageURI(imgUri1)
            }
        }
        binding.img2.setOnClickListener {
            selectedImg = 2
            if (imgUri2 == null) {
                //Requesting Permission For CAMERA
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                    )
                }
                openGallery()
            } else {
                imgUriP = imgUri2
                binding.imgPreview.setImageURI(imgUri2)
            }
        }
        binding.img3.setOnClickListener {
            selectedImg = 3
            if (imgUri3 == null) {
                //Requesting Permission For CAMERA
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                    )
                }
                openGallery()
            } else {
                imgUriP = imgUri3
                binding.imgPreview.setImageURI(imgUri3)
            }
        }
        binding.img4.setOnClickListener {
            selectedImg = 4
            if (imgUri4 == null){
                //Requesting Permission For CAMERA
                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                    )
                }
                openGallery()
            }else{
                imgUriP = imgUri4
                binding.imgPreview.setImageURI(imgUri4)
            }
        }
        binding.img5.setOnClickListener {
            selectedImg = 5
            if (imgUri5 == null) {
                //Requesting Permission For CAMERA
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                    )
                }
                openGallery()
            }else{
                imgUriP = imgUri5
                binding.imgPreview.setImageURI(imgUri5)
            }
        }

        binding.editImage.setOnClickListener {
            if (imgUriP != null){

                val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)

                dsPhotoEditorIntent.data = imgUriP

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Edited Image")

                startActivityForResult(dsPhotoEditorIntent, 100)

            }
        }

        binding.deletePost.setOnClickListener {
            if (imgUriP == imgUri1){
                imgUriP = null
                imgUri1 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img1.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri2){
                imgUriP = null
                imgUri2 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img2.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri3){
                imgUriP = null
                imgUri3 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img3.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri4){
                imgUriP = null
                imgUri4 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img4.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri5){
                imgUriP = null
                imgUri5 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img5.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            }else {
                Toast.makeText(this, "can't del", Toast.LENGTH_SHORT).show()
            }
        }


        binding.sharePost.setOnClickListener {

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.canSeeText.text == "Can See"){
                Toast.makeText(applicationContext,"Select Post Seen Status",Toast.LENGTH_SHORT).show()
            }else if (binding.canCommentText.text == "Can comment"){
                Toast.makeText(applicationContext,"Select Comment Status",Toast.LENGTH_SHORT).show()
            }else if (imgUri1 == null){
                Toast.makeText(applicationContext,"Select Image",Toast.LENGTH_SHORT).show()
            }else{
                createPost(user_id)
            }


        }

    }

    private fun createPost(userId: String) {

        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            imgUri1!!,"r",null
        )?:return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "${getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")


        val sendPost  = RetrofitBuilder.feedsApi.createPost(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"share some media"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption),
            MultipartBody.Part.createFormData("media", file.name, body)
            )

        sendPost.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext,CreateTextPost::class.java))
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
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
        binding.imgPreview.setImageURI(croppedImageUri)
        dialog.dismiss()
    }
    private fun openCamera() {
        contract.launch(cameraImageUri)
//        dialog.dismiss()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
//        dialog.dismiss()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            if (imageUri != null) {
//                compressImage(imageUri)
                cropImage(imageUri)
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImage = resultingImage.uri

                when (selectedImg) {
                    1 -> {
                        binding.deletePost.visibility = View.VISIBLE
                        binding.editImage.visibility = View.VISIBLE

                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img1.setImageURI(croppedImage)
                        imgUri1 = compressImage(croppedImage)
                        imgUriP = imgUri1
                    }
                    2 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imgUriP = imgUri2
                    }
                    3 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img3.setImageURI(croppedImage)
                        imgUri3 = compressImage(croppedImage)
                        imgUriP = imgUri3
                    }
                    4 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img4.setImageURI(croppedImage)
                        imgUri4 = compressImage(croppedImage)
                        imgUriP = imgUri4
                    }
                    5 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img5.setImageURI(croppedImage)
                        imgUri5 = compressImage(croppedImage)
                        imgUriP = imgUri5
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == 100){

            val outputUri: Uri? = data!!.data
            if (outputUri != null){

            if (imgUriP == imgUri1) {
                imgUriP = outputUri
                imgUri1 = imgUriP
                binding.imgPreview.setImageURI(imgUriP)
                binding.img1.setImageURI(imgUri1)
            } else if (imgUriP == imgUri2){
                imgUriP = outputUri
                imgUri2 = imgUriP
                binding.imgPreview.setImageURI(imgUriP)
                binding.img2.setImageURI(imgUri2)
            } else if (imgUriP == imgUri3){
                imgUriP = outputUri
                imgUri3 = imgUriP
                binding.imgPreview.setImageURI(imgUriP)
                binding.img3.setImageURI(imgUri3)
            } else if (imgUriP == imgUri4){
                imgUriP = outputUri
                imgUri4 = imgUriP
                binding.imgPreview.setImageURI(imgUriP)
                binding.img4.setImageURI(imgUri4)
            } else if (imgUriP == imgUri5){
                imgUriP = outputUri
                imgUri5 = imgUriP
                binding.imgPreview.setImageURI(imgUriP)
                binding.img5.setImageURI(imgUri5)
            }
          }
        }
    }
    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }
    private fun cropImage(imageUri: Uri) {
        val options = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)

        options.setAspectRatio(4, 3)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setOutputCompressQuality(20)
            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            .start(this)
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
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            binding.canSeeText.text = audienceFrBo
        }else{
            binding.canCommentText.text = audienceFrBo
        }
    }


//    private fun ContentResolver.getFileName(coverPhotoPart: Uri): String {
//        val random = getRandomString(6)
//
//        var name = ""
//        val returnCursor = this.query(coverPhotoPart,null,null,null,null)
//        if (returnCursor!=null){
//            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            returnCursor.moveToFirst()
//            name = returnCursor.getString(nameIndex)
//            returnCursor.close()
//        }
//        name = name+random
//        Log.e("String",name)
//        return name
//    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}