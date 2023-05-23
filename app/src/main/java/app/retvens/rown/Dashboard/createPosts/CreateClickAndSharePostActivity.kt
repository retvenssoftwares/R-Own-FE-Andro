package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetGoingBack
import app.retvens.rown.bottomsheet.BottomSheetSelectAudience
import app.retvens.rown.bottomsheet.BottomSheetWhatToPost
import app.retvens.rown.databinding.ActivityCreateClickAndSharePostBinding
import com.bumptech.glide.Glide
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreateClickAndSharePostActivity : AppCompatActivity(),
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener {
    lateinit var binding:ActivityCreateClickAndSharePostBinding

    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog

    var canSee : Int ?= 0

    var selectedImg : Int = 0

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropImage(cameraImageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClickAndSharePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        contract.launch(cameraImageUri)

        binding.imgPreview.setOnClickListener {//Requesting Permission For CAMERA
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
            contract.launch(cameraImageUri)
        }

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

        binding.editImage.setOnClickListener {
            if (croppedImageUri != null){

                val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)

                dsPhotoEditorIntent.data = croppedImageUri

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Edited Image")

                startActivityForResult(dsPhotoEditorIntent, 100)

            }
        }

        binding.deletePost.setOnClickListener {
            croppedImageUri = null
            binding.imgPreview.setImageURI(croppedImageUri)

            binding.deletePost.visibility = View.GONE
            binding.editImage.visibility = View.GONE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val resultingImage = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    val croppedImage = resultingImage.uri

                    binding.deletePost.visibility = View.VISIBLE
                    binding.editImage.visibility = View.VISIBLE

                    croppedImageUri = compressImage(croppedImage)
                    binding.imgPreview.setImageURI(croppedImageUri)
                }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Toast.makeText(this, "Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()

            } else if (requestCode == 100){
                    val outputUri: Uri? = data!!.data
                    if (outputUri != null){
                        croppedImageUri = outputUri
                        binding.imgPreview.setImageURI(croppedImageUri)
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
}