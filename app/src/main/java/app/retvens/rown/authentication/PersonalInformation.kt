package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import android.widget.Toast
import androidx.cardview.widget.CardView
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityPersonalInformationBinding

class PersonalInformation : AppCompatActivity() {

    lateinit var binding :ActivityPersonalInformationBinding
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textWelcome.text= Html.fromHtml("<font color=${Color.BLACK}>Welcome On </font>" +
                "<font color=${Color.GREEN}> Board</font>")

        binding.camera.setOnClickListener {
            openBottomSheet()
        }

        binding.cardSavePerson.setOnClickListener {
            if(binding.etName.length() < 3){
                binding.nameLayout.error = "Please enter your name"
            } else if(binding.etEmail.length() < 10){
                binding.emailLayout.error = "Enter a valid Email"
            } else{
                binding.emailLayout.isErrorEnabled = false
                val mail = binding.etEmail.text.toString()
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
            val intent = Intent(this,DashBoardActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
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

    }

    private fun openCamera() {

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            binding.profile.setImageURI(imageUri)
        }
    }
}