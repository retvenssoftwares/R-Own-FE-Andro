package app.retvens.rown.authentication

import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.Interest
import app.retvens.rown.DataCollections.MesiboAccount
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityPersonalInformationBinding
import app.retvens.rown.utils.moveTo
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*


class PersonalInformation : AppCompatActivity() {

    lateinit var binding :ActivityPersonalInformationBinding
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri ?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        compressImage(cameraImageUri)
        cropImage(cameraImageUri)
    }
    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog
    lateinit var username : String
    lateinit var eMail : String

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraImageUri = createImageUri()!!

        binding.camera.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
            openBottomSheet()
        }

        val addresse = SharedPreferenceManagerAdmin.getInstance(this).user.address.toString()
        val token = SharedPreferenceManagerAdmin.getInstance(this).user.token.toString()
//        val uid = SharedPreferenceManagerAdmin.getInstance(this).user.uid!!.toInt()
        Log.d("shared",addresse)
        Log.d("shared",token)
//        Log.d("shared",uid.toString())
        Toast.makeText(applicationContext,SharedPreferenceManagerAdmin.getInstance(this).user.address.toString(),Toast.LENGTH_SHORT).show()

        binding.cardSavePerson.setOnClickListener {

            if(binding.etName.length() < 3){
                binding.nameLayout.error = "Please enter your name"
            } else if(binding.etEmail.length() < 10){
                binding.emailLayout.error = "Enter a valid Email"
            } else{
                binding.emailLayout.isErrorEnabled = false
                username = binding.etName.text.toString()
                eMail = binding.etEmail.text.toString()
                eMail.trim()
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.setCancelable(false)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog.show()
//                openBottomSheetEmail(mail)
//                mailVerification(eMail)
                uploadData()
            }
        }

        auth = FirebaseAuth.getInstance()

    }

    private fun openBottomSheetEmail(mail:String) {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout_mail)

        val eMail = dialog.findViewById<TextView>(R.id.text_eMail)
        eMail.text = mail

        dialog.findViewById<CardView>(R.id.card_go).setOnClickListener {
            dialog.dismiss()
            val emailLink = intent.data.toString()
            if (auth.isSignInWithEmailLink(emailLink)) {
                auth.signInWithEmailLink(mail, emailLink)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(applicationContext,"mail is verified",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,DashBoardActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            val user = task.result?.user
                            // do something with the user object
                        } else {
                            Toast.makeText(applicationContext,"fail to verify",Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                            val intent = Intent(this,UserInterest::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("user",username)
                            startActivity(intent)
                            // handle sign-in failure
                        }
                    }
            }
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun mailVerification(mail:String) {

//        val mail = binding.etEmail.text.toString()

        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://app.retvens.com/emailSignInLink")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("app.retvens.rown", true, "12")
            .build()

        auth.sendSignInLinkToEmail(mail, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    openBottomSheetEmail(mail)
                    Toast.makeText(applicationContext,"mail is sent to $mail",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,UserInterest::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("user",username)
//                    startActivity(intent)
                    Log.e("error",task.exception?.message.toString())
                }
            }
//


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

    private fun uploadData() {
        val phone : Long = intent.getStringExtra("phone")?.toLong()!!
//        val phone : Long = 21412481214

        val _id = SharedPreferenceManagerAdmin.getInstance(this).user.__v.toString()
        val addresse = SharedPreferenceManagerAdmin.getInstance(this).user.address.toString()
        val token = SharedPreferenceManagerAdmin.getInstance(this).user.token.toString()
        val uid = SharedPreferenceManagerAdmin.getInstance(this).user.uid!!.toInt()

//        val addresse = "address"
//        val token = "StoredInSharedPreferenceManagerAdmin"
//        val uid = 18355
//        Toast.makeText(applicationContext,SharedPreferenceManagerAdmin.getInstance(this).user.uid.toString(),Toast.LENGTH_SHORT).show()

        val Interest = Interest(_id,uid.toString())
        val Mesibo_account = MesiboAccount(_id,addresse,token,uid)


        if (croppedImageUri != null){

            val filesDir = applicationContext.filesDir
            val file = File(filesDir,"image.png")

            val inputStream = contentResolver.openInputStream(croppedImageUri!!)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

            val respo  = RetrofitBuilder.retrofitBuilder.uploadUserProfile(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),username),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),eMail),
                phone,
                MultipartBody.Part.createFormData("Profile_pic", file.name, requestBody),
                Mesibo_account.uid,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Mesibo_account.address),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Mesibo_account.token),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Interest.id),
                0,0
            )
            respo.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("image file", file.toString())
                    Log.d("image", response.toString())
                    if (response.message().toString() != "Request Entity Too Large"){
                        moveTo(this@PersonalInformation,"MoveToI")
                        val intent = Intent(applicationContext,UserInterest::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("user",username)
                        startActivity(intent)
                    }
//                Toast.makeText(applicationContext,file.name.toString(),Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            val pWithoutImg = RetrofitBuilder.retrofitBuilder.uploadUserProfileWithoutImg(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),username),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),eMail),
                phone,
                Mesibo_account.uid,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Mesibo_account.address),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Mesibo_account.token),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Interest.id)
                )
            pWithoutImg.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                    if (response.message().toString() != "Request Entity Too Large"){
                        moveTo(this@PersonalInformation,"MoveToI")
                        val intent = Intent(applicationContext,UserInterest::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("user",username)
                        startActivity(intent)
                    }
                }
                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        }

       
    }
    private fun ContentResolver.getFileName(coverPhotoPart: Uri): String {

        var name = ""
        val returnCursor = this.query(coverPhotoPart,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
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
        binding.profile.setImageURI(croppedImageUri)
        dialog.dismiss()
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

               compressImage(croppedImage)

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

          croppedImageUri = compressed
          binding.profile.setImageURI(croppedImageUri)
          val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
          intent.setData(compressed)
          sendBroadcast(intent)
      }catch (e:IOException){
          e.printStackTrace()
      }
        return compressed
    }

    private fun loadLocale(){
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("MY_LANG", "")
        setLocale(language!!)
    }

    private fun setLocale(language: String) {
        val locale  = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

        val editor : SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("MY_LANG", language)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        loadLocale()
    }

    override fun onRestart() {
        super.onRestart()
        loadLocale()
    }

    override fun onResume() {
        super.onResume()
        loadLocale()
    }
    override fun onPause() {
        super.onPause()
        loadLocale()
    }
}