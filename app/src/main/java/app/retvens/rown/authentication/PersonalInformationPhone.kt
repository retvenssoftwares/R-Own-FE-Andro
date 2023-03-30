package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityPersonalInformationPhoneBinding
import java.io.File

class PersonalInformationPhone : AppCompatActivity() {

    lateinit var binding : ActivityPersonalInformationPhoneBinding
    var PICK_IMAGE_REQUEST_CODE : Int = 0

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var imageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        binding.profilePhone.setImageURI(null)
        binding.profilePhone.setImageURI(imageUri)
    }
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textWelcomePhone.text= Html.fromHtml("<font color=${Color.BLACK}>Welcome On </font>" +
                "<font color=${Color.GREEN}> Board</font>")

        imageUri = createImageUri()!!

        binding.cameraPhone.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
            openBottomSheet()
        }

        binding.cardSavePersonPhone.setOnClickListener {
            if(binding.etNamePhone.length() < 3){
                binding.nameLayout.error = "Please enter your name"
            } else if(binding.etPhonePerson.length() < 10){
                binding.phoneLayout.error = "Enter a valid Email"
            } else{
                binding.phoneLayout.isErrorEnabled = false
                val mail = binding.etPhonePerson.text.toString()
                mail.trim()
                openBottomSheetEmail(mail)
            }
        }
    }

    private fun openBottomSheetEmail(email:String) {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout_mail)

        val eMail = dialog.findViewById<TextView>(R.id.text_eMail)
        eMail.text = email

        dialog.findViewById<CardView>(R.id.card_go).setOnClickListener {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

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
        binding.profilePhone.setImageURI(null)
        dialog.dismiss()
    }

    private fun openCamera() {
        contract.launch(imageUri)
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
            binding.profilePhone.setImageURI(imageUri)
        }
    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(applicationContext,
            "app.retvens.rown.fileProvider",
            image
        )
    }
}