package app.retvens.rown.Dashboard.createPosts

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.databinding.ActivityCreatePostBinding
import app.retvens.rown.utils.compressImage
import app.retvens.rown.utils.cropImage
import app.retvens.rown.utils.prepareFilePart
import com.bumptech.glide.Glide
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreatePostActivity : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener,BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding: ActivityCreatePostBinding

    private var fileList : ArrayList<MultipartBody.Part> = ArrayList()
    private var imagesList : ArrayList<Uri> = ArrayList()

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
        cropImage(cameraImageUri, this)
    }

    var canSee : Int ?= 0

    var selectedImg : Int = 0

    var isEvent : Boolean = false

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

                    openGallery()

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
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
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
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
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
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
            } else {
                imgUriP = imgUri3
                binding.imgPreview.setImageURI(imgUri3)
            }
        }
        binding.img4.setOnClickListener {
            selectedImg = 4
            if (imgUri4 == null){
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
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
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
                imagesList.remove(imgUri1)
                imgUriP = null
                imgUri1 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img1.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri2){
                imagesList.remove(imgUri2)
                imgUriP = null
                imgUri2 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img2.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri3){
                imagesList.remove(imgUri3)
                imgUriP = null
                imgUri3 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img3.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri4){
                imagesList.remove(imgUri4)
                imgUriP = null
                imgUri4 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img4.setImageURI(imgUriP)
                if (imgUri1 == null && imgUri2 == null && imgUri3 == null && imgUri4 == null && imgUri5 == null){
                    binding.deletePost.visibility = View.GONE
                    binding.editImage.visibility = View.GONE
                }
            } else if(imgUriP == imgUri5){
                imagesList.remove(imgUri5)
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
            }else if (imagesList.isEmpty()){
                Toast.makeText(applicationContext,"Select Image",Toast.LENGTH_SHORT).show()
//            }else if (binding.etLocationPostEvent.text.toString().isEmpty()){
//                binding.etLocationPostEvent.error = "select location first!!"
            }else{
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                createPost(user_id)
            }
        }

        binding.etLocationPostEvent.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

    }

    private fun createPost(userId: String) {

        val canSee = binding.canSeeText.text.toString()
        val canComment = binding.canCommentText.text.toString()
        val caption = binding.whatDYEt.text.toString()
        val location = binding.etLocationPostEvent.text.toString()

        imagesList.forEach {
            fileList.add(prepareFilePart(it, "media", applicationContext)!!)
        }

        val sendPost  = RetrofitBuilder.feedsApi.createMultiPost(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"share some media"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canSee),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),canComment),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),caption),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),location),
            fileList
            )

        sendPost.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    val response = response.body()!!
                    val intent = Intent(applicationContext,DashBoardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else{
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                onBackPressed()
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
                cropImage(imageUri, this)
            }
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                when (selectedImg) {
                    1 -> {
                        binding.deletePost.visibility = View.VISIBLE
                        binding.editImage.visibility = View.VISIBLE

                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img1.setImageURI(croppedImage)
                        imgUri1 = compressImage(croppedImage, this)
                        imgUriP = imgUri1
                        imagesList.add(imgUri1!!)
                    }
                    2 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage, this)
                        imgUriP = imgUri2
                        imagesList.add(imgUri2!!)
                    }
                    3 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img3.setImageURI(croppedImage)
                        imgUri3 = compressImage(croppedImage, this)
                        imgUriP = imgUri3
                        imagesList.add(imgUri3!!)
                    }
                    4 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img4.setImageURI(croppedImage)
                        imgUri4 = compressImage(croppedImage, this)
                        imgUriP = imgUri4
                        imagesList.add(imgUri4!!)
                    }
                    5 -> {
                        binding.imgPreview.setImageURI(croppedImage)
                        binding.img5.setImageURI(croppedImage)
                        imgUri5 = compressImage(croppedImage, this)
                        imgUriP = imgUri5
                        imagesList.add(imgUri5!!)
                    }
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 100){

            val outputUri: Uri? = data!!.data
            if (outputUri != null){

            if (imgUriP == imgUri1) {
                imagesList.remove(imgUri1)
                imgUriP = outputUri
                imgUri1 = imgUriP
                imagesList.add(imgUri1!!)
                binding.imgPreview.setImageURI(imgUriP)
                binding.img1.setImageURI(imgUri1)
            } else if (imgUriP == imgUri2){
                imagesList.remove(imgUri2)
                imgUriP = outputUri
                imgUri2 = imgUriP
                imagesList.add(imgUri2!!)
                binding.imgPreview.setImageURI(imgUriP)
                binding.img2.setImageURI(imgUri2)
            } else if (imgUriP == imgUri3){
                imagesList.remove(imgUri3)
                imgUriP = outputUri
                imgUri3 = imgUriP
                imagesList.add(imgUri3!!)
                binding.imgPreview.setImageURI(imgUriP)
                binding.img3.setImageURI(imgUri3)
            } else if (imgUriP == imgUri4){
                imagesList.remove(imgUri4)
                imgUriP = outputUri
                imgUri4 = imgUriP
                imagesList.add(imgUri4!!)
                binding.imgPreview.setImageURI(imgUriP)
                binding.img4.setImageURI(imgUri4)
            } else if (imgUriP == imgUri5){
                imagesList.remove(imgUri5)
                imgUriP = outputUri
                imgUri5 = imgUriP
                imagesList.add(imgUri5!!)
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
    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            binding.canSeeText.text = audienceFrBo
        }else{
            binding.canCommentText.text = audienceFrBo
        }
    }
    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocationPostEvent.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

}