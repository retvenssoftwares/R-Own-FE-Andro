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
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityPersonalInformationBinding
import com.google.firebase.auth.ActionCodeSettings
import java.io.File
import com.google.firebase.auth.FirebaseAuth

class PersonalInformation : AppCompatActivity() {

    lateinit var binding :ActivityPersonalInformationBinding
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    lateinit var galleryImageUri: Uri

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var imageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        binding.profile.setImageURI(null)
        binding.profile.setImageURI(imageUri)
    }
    lateinit var dialog: Dialog

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textWelcome.text= Html.fromHtml("<font color=${Color.BLACK}>Welcome On </font>" +
                "<font color=${Color.GREEN}> Board</font>")

        imageUri = createImageUri()!!

        binding.camera.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
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

        auth = FirebaseAuth.getInstance()

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

            mailVerification()

        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun mailVerification() {

        val mail = binding.etEmail.text.toString()

        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://app.retvens.com/emailSignInLink")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("app.retvens.rown", true, "12")
            .build()

        auth.sendSignInLinkToEmail(mail, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"mail is sent to $mail",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                    Log.e("error",task.exception?.message.toString())
                }
            }
//
        val emailLink = intent.data.toString()

        if (auth.isSignInWithEmailLink(emailLink)) {
            auth.signInWithEmailLink(mail, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"mail is verified",Toast.LENGTH_SHORT).show()
                        val user = task.result?.user
                        // do something with the user object
                    } else {
                        Toast.makeText(applicationContext,"fail to verify",Toast.LENGTH_SHORT).show()
                        // handle sign-in failure
                    }
                }
        }
//
//        auth.createUserWithEmailAndPassword(mail, "000000")
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Send verification email to the user
//                    val user = auth.currentUser
//                    user?.sendEmailVerification()
//                        ?.addOnCompleteListener { verificationTask ->
//                            if (verificationTask.isSuccessful) {
//                                Toast.makeText(applicationContext, "Verification email sent", Toast.LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(applicationContext, "Failed to send verification email", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                } else {
//                    Toast.makeText(applicationContext, "Failed to create account", Toast.LENGTH_SHORT).show()
//                }
//            }



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
        binding.profile.setImageURI(null)
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
            binding.profile.setImageURI(imageUri)
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