package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetGoingBack
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.bottomsheet.BottomSheetWhatToPost
import app.retvens.rown.databinding.ActivityCreatePostBinding
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CreatePost : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener,
    BottomSheetGoingBack.OnBottomGoingBackClickListener,
    BottomSheetWhatToPost.OnBottomWhatToPostClickListener,
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding : ActivityCreatePostBinding


    private val PHOTO_EDITOR_REQUEST_CODE = 231 // Any integer value as a request code.

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    private var imgUriP : Uri ?= null
    private var imgUri1 : Uri ?= null
    private var imgUri2 : Uri ?= null
    private var imgUri3 : Uri ?= null
    private var imgUri4 : Uri ?= null
    private var imgUri5 : Uri ?= null

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

        binding.createP.setOnClickListener {
            selectedImg = 1
            val bottomSheet = BottomSheetWhatToPost()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetWhatToPost.WTP_TAG)}
            bottomSheet.setOnWhatToPostClickListener(this)
//            openBottomSheet()
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

        binding.imgPreview.setOnClickListener {
            if (imgUriP != null){

                /*try {
                    val intent = ImageEditorIntentBuilder(this, sourceImagePath, outputFilePath)
                        .withAddText() // Add the features you need
                        .withPaintFeature()
                        .withFilterFeature()
                        .withRotateFeature()
                        .withCropFeature()
                        .withBrightnessFeature()
                        .withSaturationFeature()
                        .withBeautyFeature()
                        .withStickerFeature()
                        .forcePortrait(true) // Add this to force portrait mode (It's set to false by default)
                        .setSupportActionBarVisibility(false) // To hide app's default action bar
                        .build()
                    EditImageActivity.start(activity, intent, PHOTO_EDITOR_REQUEST_CODE)
                } catch (e: Exception) {
                    Log.e(
                        "Demo App",
                        e.message!!
                    ) // This could throw if either `sourcePath` or `outputPath` is blank or Null
                }*/
            }
        }

        binding.deletePost.setOnClickListener {
            if (imgUriP == imgUri1){
                imgUriP = null
                imgUri1 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img1.setImageURI(imgUriP)
            } else if(imgUriP == imgUri2){
                imgUriP = null
                imgUri2 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img2.setImageURI(imgUriP)
            } else if(imgUriP == imgUri3){
                imgUriP = null
                imgUri3 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img3.setImageURI(imgUriP)
            } else if(imgUriP == imgUri4){
                imgUriP = null
                imgUri4 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img4.setImageURI(imgUriP)
            } else if(imgUriP == imgUri5){
                imgUriP = null
                imgUri5 = null
                binding.imgPreview.setImageURI(imgUriP)
                binding.img5.setImageURI(imgUriP)
            }else{
                Toast.makeText(this, "can't del", Toast.LENGTH_SHORT).show()
            }
        }

        binding.userLocationPost.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
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

                if (selectedImg == 1){
                    binding.whatDY.visibility = View.VISIBLE
                    binding.postsLayout.visibility = View.VISIBLE
                    binding.deletePost.visibility = View.VISIBLE
                    binding.updateEvent.visibility = View.GONE
                    binding.cardEventPost.visibility = View.GONE
                    binding.cardVenue.visibility = View.GONE
                    binding.checkIn.visibility = View.GONE

                    binding.imgPreview.setImageURI(croppedImage)
                    binding.img1.setImageURI(croppedImage)
                    imgUri1 = compressImage(croppedImage)
                    imgUriP = imgUri1
                } else if (selectedImg == 2){
                    binding.imgPreview.setImageURI(croppedImage)
                    binding.img2.setImageURI(croppedImage)
                    imgUri2 = compressImage(croppedImage)
                    imgUriP = imgUri2
                } else if (selectedImg == 3){
                    binding.imgPreview.setImageURI(croppedImage)
                    binding.img3.setImageURI(croppedImage)
                    imgUri3 = compressImage(croppedImage)
                    imgUriP = imgUri3
                } else if (selectedImg == 4){
                    binding.imgPreview.setImageURI(croppedImage)
                    binding.img4.setImageURI(croppedImage)
                    imgUri4 = compressImage(croppedImage)
                    imgUriP = imgUri4
                } else if (selectedImg == 5){
                    binding.imgPreview.setImageURI(croppedImage)
                    binding.img5.setImageURI(croppedImage)
                    imgUri5 = compressImage(croppedImage)
                    imgUriP = imgUri5
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT)
                    .show()
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

        options.setAspectRatio(16, 9)
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
    /*------------------------------CAMERA FUNCTIONALITIES AND SET LOCALE LANGUAGE--------------*/
    override fun bottomWhatToPostClick(WhatToPostFrBo: String) {
        when (WhatToPostFrBo) {
            "Click" -> {
                openCamera()
            }
            "Share" -> {
                openGallery()
            }
            "Update" -> {
                binding.postsLayout.visibility = View.GONE
                binding.deletePost.visibility = View.GONE
                binding.whatDY.visibility = View.GONE
                binding.updateEvent.visibility = View.VISIBLE
                binding.cardEventPost.visibility = View.GONE
                binding.cardVenue.visibility = View.GONE
                binding.checkIn.visibility = View.GONE
                postUpdateEvent()
            }
            "Check" -> {
                binding.postsLayout.visibility = View.GONE
                binding.deletePost.visibility = View.GONE
                binding.whatDY.visibility = View.GONE
                binding.updateEvent.visibility = View.GONE
                binding.cardEventPost.visibility = View.GONE
                binding.cardVenue.visibility = View.GONE
                binding.checkIn.visibility = View.VISIBLE
                checkIn()
            }
            "Poll" -> {
                binding.postsLayout.visibility = View.GONE
                binding.deletePost.visibility = View.GONE
                binding.whatDY.visibility = View.VISIBLE
                binding.updateEvent.visibility = View.GONE
                binding.cardEventPost.visibility = View.GONE
                binding.cardVenue.visibility = View.GONE
                binding.checkIn.visibility = View.GONE
                startActivity(Intent(applicationContext, CreatePollActivity::class.java))
            }
        }
    }
    private fun postUpdateEvent() {
        binding.etLocationPostEvent.setOnClickListener {
            isEvent = true
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.etEventVenuePost.setOnClickListener {
            Toast.makeText(applicationContext, "Will implement along with api", Toast.LENGTH_SHORT).show()
        }
        binding.nextUpdateEvent.setOnClickListener {
            binding.whatDY.visibility = View.VISIBLE
            binding.updateEvent.visibility = View.GONE
            binding.cardEventPost.visibility = View.VISIBLE
        }
    }

    private fun checkIn() {
        binding.etLocationPost.setOnClickListener {
            isEvent = false
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
        binding.etVenuePost.setOnClickListener {
            Toast.makeText(applicationContext, "Will implement along with api", Toast.LENGTH_SHORT).show()
        }

        binding.nextCheckIn.setOnClickListener {
            binding.whatDY.visibility = View.VISIBLE
            binding.checkIn.visibility = View.GONE
            binding.cardVenue.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val bottomSheet = BottomSheetGoingBack()
        val fragManager = supportFragmentManager
        fragManager.let{bottomSheet.show(it, BottomSheetGoingBack.GoingBack_TAG)}
        bottomSheet.setOnGoingBackClickListener(this)
    }
    override fun bottomGoingBackClick(GoingBackFrBo: String) {
        if (GoingBackFrBo == "Discard"){
            super.onBackPressed()
        }
    }
    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        if (isEvent){
            binding.etLocationPostEvent.setText(CountryStateCityFrBo)
        } else {
            binding.etLocationPost.setText(CountryStateCityFrBo)
        }
    }
}