package app.retvens.rown.NavigationFragments.profile.hotels

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
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.databinding.ActivityEditHotelDetailsBinding
import app.retvens.rown.utils.getRandomString
import com.bumptech.glide.Glide
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
import java.util.Random
import kotlin.streams.asSequence


class EditHotelDetailsActivity : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding : ActivityEditHotelDetailsBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    var REQUEST_CAMERA_PERMISSION : Int = 0
    private var selectedImg = 0

    private var fileList : ArrayList<MultipartBody.Part> = ArrayList()
    private var imagesList : ArrayList<Uri> = ArrayList()

    lateinit var progressDialog: Dialog
    private lateinit var bodyImage1:UploadRequestBody
    private lateinit var bodyImage2:UploadRequestBody
    private lateinit var bodyImage3:UploadRequestBody
    //Cropped image uri

    var fileImage1:File ?= null
    private lateinit var fileImage2:File
    private lateinit var fileImage3:File

    private var croppedImageUri: Uri?= null

    private var imgUri1 : Uri?= null   // Final uri for img1
    private var imgUri2 : Uri?= null   // Final uri for img2
    private var imgUri3 : Uri?= null   // Final uri for img3

    private var uploadImg1 = false
    private var uploadImg2 = false
    private var uploadImg3 = false

    private var hotelId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBackBtn.setOnClickListener {
            onBackPressed()
        }

        val hotelName = intent.getStringExtra("name")
        val img1 = intent.getStringExtra("img1")
        val img2 = intent.getStringExtra("img2")
        val img3 = intent.getStringExtra("img3")
        val location = intent.getStringExtra("location")
        val Hoteldescription = intent.getStringExtra("hotelDescription").toString()

        hotelId = intent.getStringExtra("hotelId").toString()

        val image1Uri = main(img1.toString())
        val imageUri2 = main(img2.toString())
        val imageUri3 = main(img3.toString())

//        imgUri1 = image1Uri
//        imgUri2 = imageUri2
//        imgUri3 = imageUri3

        binding.overviewEt.setText(Hoteldescription)
        binding.locationText.text = location
        binding.profileUsername.text ="$hotelName Details"
        binding.etNameEdit.setText(hotelName)
        try {
            Glide.with(this).load(img1).into(binding.img1)
            Glide.with(this).load(img2).into(binding.img2)
            Glide.with(this).load(img3).into(binding.img3)
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }

        binding.hotelLocation.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
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

        binding.updateHotel.setOnClickListener {
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()

            updateData()
        }

        getHotel()
    }
    private fun getHotel() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val hotel = RetrofitBuilder.ProfileApis.getHotelInfo(hotelId)
        hotel.enqueue(object : Callback<HotelData?> {
            override fun onResponse(call: Call<HotelData?>, response: Response<HotelData?>) {
                if (response.isSuccessful){
                    val data = response.body()!!
                    binding.etNameEdit.setText(data.hotelName)
                    binding.overviewEt.setText(data.Hoteldescription)

                    try {
                        if (data.gallery.get(0).Image1.isNotEmpty()){
                            Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
                        }
                        if (data.gallery.get(0).Image2.isNotEmpty()){
                            Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)
                        }
                        if (data.gallery.get(0).Image3.isNotEmpty()){
                            Glide.with(applicationContext).load(data.gallery.get(0).Image3).into(binding.img3)
                        }
                    }catch (e:IndexOutOfBoundsException){
                        Log.e("error",e.message.toString())
                    }
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HotelData?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun main(uri: String): Uri? {
        return try {
            Uri.parse(uri)
        } catch (e: Exception) {
            println("Invalid URI: $e")
            null
        }
    }
    private fun updateData() {
        val hotelId = intent.getStringExtra("hotelId")
        val name = binding.etNameEdit.text.toString()
        val description = binding.overviewEt.text.toString()
        val location = binding.locationText.text.toString()

        if (imagesList.isNotEmpty()) {

            if (uploadImg1) {
                val image = app.retvens.rown.utils.prepareFilePart(
                    imgUri1!!,
                    "galleryImages1",
                    applicationContext
                )

                val respo = RetrofitBuilder.ProfileApis.updateHotels1(
                    hotelId!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                    image!!
                )
                respo.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "image1 : ${response.body()?.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            t.localizedMessage?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            if (uploadImg2) {
                val image = app.retvens.rown.utils.prepareFilePart(
                    imgUri2!!,
                    "galleryImages2",
                    applicationContext
                )

                val respo = RetrofitBuilder.ProfileApis.updateHotels2(
                    hotelId!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                    image!!
                )
                respo.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "image2 : ${response.body()?.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            t.localizedMessage?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            if (uploadImg3) {
                val image = app.retvens.rown.utils.prepareFilePart(
                    imgUri3!!,
                    "galleryImages3",
                    applicationContext
                )

                val respo = RetrofitBuilder.ProfileApis.updateHotels3(
                    hotelId!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                    image!!
                )
                respo.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            t.localizedMessage?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

        } else {
                val respo = RetrofitBuilder.ProfileApis.updateHotels(
                    hotelId!!,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                )
                respo.enqueue(object : Callback<UpdateResponse?> {
                    override fun onResponse(
                        call: Call<UpdateResponse?>,
                        response: Response<UpdateResponse?>
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }

                    override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            t.localizedMessage?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

    private fun prepareFilePart(fileUri: Uri, randomId : String, imageCounter : Int): MultipartBody.Part? {
        val randomString = Random().ints(randomId.length.toLong(), 0, randomId.length)
            .asSequence()
            .map(randomId::get)
            .joinToString("")

        val filesDir = applicationContext.filesDir
        val file = File(filesDir,"$randomString.png")

        val inputStream = contentResolver.openInputStream(fileUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("galleryImages$imageCounter", file.name, requestBody)
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
//                if (selectedImg == 1) {
//                    binding.img1.setImageURI(imageUri)
//                } else if (selectedImg == 2) {
//                    binding.img2.setImageURI(imageUri)
//                } else if (selectedImg == 3) {
//                    binding.img3.setImageURI(imageUri)
//                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                when (selectedImg) {
                    1 -> {
                        imgUri1 = compressImage(croppedImage)
                        imagesList.add(imgUri1!!)
                        binding.img1.setImageURI(imgUri1!!)
                        binding.deleteImg1.visibility = View.VISIBLE
                        uploadImg1 = true
                    }
                    2 -> {
                        imgUri2 = compressImage(croppedImage)
                        imagesList.add(imgUri2!!)
                        binding.img2.setImageURI(imgUri2!!)
                        binding.deleteImg2.visibility = View.VISIBLE
                        uploadImg2 = true
                    }
                    3 -> {
                        imgUri3 = compressImage(croppedImage)
                        imagesList.add(imgUri3!!)
                        binding.img3.setImageURI(imgUri3!!)
                        binding.deleteImg3.visibility = View.VISIBLE
                        uploadImg3 = true
                    }
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
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
//            if (selectedImg == 1) {
//                binding.img1.setImageURI(imageUri)
//            } else if (selectedImg == 2) {
//                binding.img2.setImageURI(imageUri)
//            } else if (selectedImg == 3) {
//                binding.img3.setImageURI(imageUri)
//            }

            croppedImageUri = compressed
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.locationText.text = CountryStateCityFrBo
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, HotelDetailsProfileActivity::class.java)
        intent.putExtra("hotelId", hotelId)
        startActivity(intent)
        finish()
    }
}