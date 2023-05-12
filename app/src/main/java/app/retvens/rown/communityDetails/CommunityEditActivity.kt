package app.retvens.rown.communityDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCommunityEditBinding
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CommunityEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityEditBinding

    private  var communityUri: Uri? = null
    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private  var cameraImageUri: Uri? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropImage(cameraImageUri!!)
    }

    private lateinit var profile: ShapeableImageView
    private lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")

        binding.communityNameEdit.setText(title)

        Glide.with(applicationContext).load(image).into(binding.communityEditProfile)

        profile = findViewById(R.id.community_edit_profile)

        binding.communityEditProfile.setOnClickListener {
            openBottomCameraSheet()
        }

    }

    private fun openBottomCameraSheet() {

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

    private fun openCamera() {
        contract.launch(cameraImageUri)
        dialog.dismiss()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    private fun deleteImage() {
        communityUri = null
        profile.setImageURI(communityUri)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                cropImage(imageUri)
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

                compressImage(croppedImage)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(applicationContext,"Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()
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

        options.setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
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

            communityUri = compressed
            profile.setImageURI(communityUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }
}