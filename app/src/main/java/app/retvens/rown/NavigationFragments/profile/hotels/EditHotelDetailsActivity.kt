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
import androidx.core.content.FileProvider
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.databinding.ActivityEditHotelDetailsBinding
import app.retvens.rown.utils.cropImage
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

    private lateinit var fileImage1:File
    private lateinit var fileImage2:File
    private lateinit var fileImage3:File

    private var croppedImageUri: Uri?= null

    private var imgUri1 : Uri?= null   // Final uri for img1
    private var imgUri2 : Uri?= null   // Final uri for img2
    private var imgUri3 : Uri?= null   // Final uri for img3
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

        val image1Uri = main(img1.toString())
        val imageUri2 = main(img2.toString())
        val imageUri3 = main(img3.toString())

        imgUri1 = image1Uri
        imgUri2 = imageUri2
        imgUri3 = imageUri3

        binding.overviewEt.setText(Hoteldescription)
        binding.locationText.text = location
        binding.profileUsername.text ="$hotelName Details"
        binding.etNameEdit.setText(hotelName)
        Glide.with(this).load(img1).into(binding.img1)
        Glide.with(this).load(img2).into(binding.img2)
        Glide.with(this).load(img3).into(binding.img3)

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

//        getHotel()
    }
    private fun getHotel() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val hotelId = intent.getStringExtra("hotelId").toString()

        val hotel = RetrofitBuilder.ProfileApis.getHotelInfo(hotelId)
        hotel.enqueue(object : Callback<HotelData?> {
            override fun onResponse(call: Call<HotelData?>, response: Response<HotelData?>) {
                if (response.isSuccessful){
                    val data = response.body()!!
                    binding.etNameEdit.setText(data.hotelName)
//                    Glide.with(applicationContext).load(data.hotelCoverpicUrl).into(binding.vendorImage)

                    if(data.gallery.size >= 3) {
//                        img1 = data.gallery.get(0).Images
//                        img2 = data.gallery.get(1).Images
//                        img3 = data.gallery.get(2).Images
//                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                        Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)
                        Glide.with(applicationContext).load(data.gallery.get(0).Image3).into(binding.img3)
                    } else if (data.gallery.size >= 2) {
//                        img1 = data.gallery.get(0).Images
//                        img2 = data.gallery.get(1).Images
//                        binding.img3.visibility = View.GONE
//                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                        Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
                        Glide.with(applicationContext).load(data.gallery.get(0).Image2).into(binding.img2)
                    } else if (data.gallery.size > 0) {
//                        binding.img2.visibility = View.GONE
//                        binding.img3.visibility = View.GONE
//                        img1 = data.gallery.get(0).Images
                        Glide.with(applicationContext).load(data.gallery.get(0).Image1).into(binding.img1)
//                        Glide.with(applicationContext).load(data.gallery.get(0).Images).into(binding.vendorImage)
                    }

                    binding.overviewEt.setText(data.Hoteldescription)
//                    hotelLogo = data.hotelLogoUrl
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

        try {
            val parcelFileDescriptorimage1 = contentResolver.openFileDescriptor(
                imgUri1!!,"r",null
            )?:return
            val inputStreamImage1 = FileInputStream(parcelFileDescriptorimage1.fileDescriptor)
            fileImage1 =  File(cacheDir, "cropped_${getRandomString(6)}.jpg")
            val outputStreamImage1 = FileOutputStream(fileImage1)
            inputStreamImage1.copyTo(outputStreamImage1)
            bodyImage1 = UploadRequestBody(fileImage1,"image")
        }catch (e:Exception){
            Log.e("error",e.message.toString())
        }



        try {
            val parcelFileDescriptorImage2 = contentResolver.openFileDescriptor(
                imgUri2!!,"r",null
            )?:return
            val inputStreamImage2 = FileInputStream(parcelFileDescriptorImage2.fileDescriptor)
            fileImage2 =  File(cacheDir, "cropped_${getRandomString(6)}.jpg")
            val outputStreamImage2 = FileOutputStream(fileImage2)
            inputStreamImage2.copyTo(outputStreamImage2)
            bodyImage2 = UploadRequestBody(fileImage2,"image")

        }catch (e:Exception){
            Log.e("error",e.message.toString())
        }

       try {
           val parcelFileDescriptorImage3 = contentResolver.openFileDescriptor(
               imgUri3!!,"r",null
           )?:return
           val inputStreamImage3 = FileInputStream(parcelFileDescriptorImage3.fileDescriptor)
           fileImage3 =  File(cacheDir, "cropped_${getRandomString(6)}.jpg")
           val outputStreamImage3 = FileOutputStream(fileImage3)
           inputStreamImage3.copyTo(outputStreamImage3)
           bodyImage3 = UploadRequestBody(fileImage3,"image")
       }catch (e:Exception){
           Log.e("error",e.message.toString())
       }

        Log.e("image1",imgUri1.toString())
        Log.e("image2",imgUri2.toString())
        Log.e("image3",imgUri3.toString())


        if (imgUri1.toString() != "null"){
            val respo = RetrofitBuilder.ProfileApis.updateHotels1(
                hotelId!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                MultipartBody.Part.createFormData("galleryImages1", fileImage1.name, bodyImage1)
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
        }else if (imgUri2.toString() != "null"){
            val respo = RetrofitBuilder.ProfileApis.updateHotels2(
                hotelId!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                MultipartBody.Part.createFormData("galleryImages2", fileImage2.name, bodyImage2),
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
        }else if (imgUri3.toString() != "null"){
            val respo = RetrofitBuilder.ProfileApis.updateHotels3(
                hotelId!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location),
                MultipartBody.Part.createFormData("galleryImages3", fileImage3.name, bodyImage3)
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
        }else{
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

    private fun prepareFilePart(fileUri: Uri, hotelId : String): MultipartBody.Part? {
        val randomString = Random().ints(hotelId.length.toLong(), 0, hotelId.length)
            .asSequence()
            .map(hotelId::get)
            .joinToString("")

        val filesDir = applicationContext.filesDir
        val file = File(filesDir,"$randomString.png")

        val inputStream = contentResolver.openInputStream(fileUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("galleryImages", file.name, requestBody)
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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                when (selectedImg) {
                    1 -> {
                        binding.deleteImg1.visibility = View.VISIBLE
                        imgUri1 = compressImage(croppedImage)
                        binding.img1.setImageURI(imgUri1)
                        imagesList.add(imgUri1!!)
                    }
                    2 -> {
                        binding.deleteImg2.visibility = View.VISIBLE
                        binding.img2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imagesList.add(imgUri2!!)
                    }
                    3 -> {
                        binding.deleteImg3.visibility = View.VISIBLE
                        imgUri3 = compressImage(croppedImage)
                        binding.img3.setImageURI(imgUri3)
                        imagesList.add(imgUri3!!)
                    }
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
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

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.locationText.text = CountryStateCityFrBo
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }
}