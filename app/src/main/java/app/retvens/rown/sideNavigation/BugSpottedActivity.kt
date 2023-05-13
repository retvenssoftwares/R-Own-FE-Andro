package app.retvens.rown.sideNavigation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityBugSpottedBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BugSpottedActivity : AppCompatActivity() {
    lateinit var binding: ActivityBugSpottedBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var croppedImageUri: Uri?= null
    var selectedImg : Int = 1
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
            imgUri1 = null
            binding.bgi1.setImageURI(imgUri1)
            selectedImg = 1
        }

        binding.deleteImg2.setOnClickListener {
            imgUri2 = null
            binding.bgi2.setImageURI(imgUri2)
            selectedImg = if (imgUri1 == null){
                            1
            }
            else {
                            2
           }
        }

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
                    }
                    2 -> {
                        binding.deleteImg2.visibility = View.VISIBLE

                        binding.bgi2.setImageURI(croppedImage)
                        imgUri2 = compressImage(croppedImage)
                        imgUriP = imgUri2
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