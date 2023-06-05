package app.retvens.rown.sideNavigation

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
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityBugSpottedBinding
import app.retvens.rown.utils.getRandomString
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Random
import kotlin.streams.asSequence

class BugSpottedActivity : AppCompatActivity() {
    lateinit var binding: ActivityBugSpottedBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var croppedImageUri: Uri?= null
    var selectedImg : Int = 1

    private var fileList : ArrayList<MultipartBody.Part> = ArrayList()
    private var imagesList : ArrayList<Uri> = ArrayList()

    lateinit var progressDialog: Dialog

    private var imgUriP : Uri?= null
    private var imgUri1 : Uri?= null
    private var imgUri2 : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugSpottedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bugBackBtn.setOnClickListener { onBackPressed() }

        binding.addAttachment.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA), PICK_IMAGE_REQUEST_CODE
                )
            }
            openGallery()
        }

        binding.deleteImg1.setOnClickListener {
            imagesList.remove(imgUri1!!)
            imgUri1 = null
            binding.bgi1.setImageURI(imgUri1)
            selectedImg = 1
        }

        binding.deleteImg2.setOnClickListener {
            imagesList.remove(imgUri2!!)
            imgUri2 = null
            binding.bgi2.setImageURI(imgUri2)
            selectedImg = if (imgUri1 == null){
                            1
            }
            else {
                            2
           }
        }

        binding.sendBug.setOnClickListener {
            if (imagesList.isEmpty()){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            } else if(binding.bugET.text.length < 10){
                Toast.makeText(this , "Please explain properly", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.setCancelable(false)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                postBuggg()
            }
        }

    }

    private fun postBuggg() {

        imagesList.forEach {
            fileList.add(prepareFilePart(it)!!)
        }

        val post = RetrofitBuilder.ProfileApis.postBug(
            fileList,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.bugET.text.toString())
        )

        post.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part? {
        val filesDir = applicationContext.filesDir
        val file = File(filesDir,"${getRandomString(6)}.png")

        val inputStream = contentResolver.openInputStream(fileUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("galleryImages", file.name, requestBody)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
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
                        binding.deleteImg1.visibility = View.VISIBLE

                        binding.bgi1.setImageURI(croppedImage)
                        imgUri1 = compressImage(croppedImage)
                        imgUriP = imgUri1
                        selectedImg = 2
                        imagesList.add(imgUri1!!)
                    }
                    2 -> {
                        binding.deleteImg2.visibility = View.VISIBLE

                        binding.bgi2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imgUriP = imgUri2
                        imagesList.add(imgUri2!!)
                    }
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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

}