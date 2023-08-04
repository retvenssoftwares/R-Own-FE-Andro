package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.AddMemberData
import app.retvens.rown.DataCollections.FeedCollection.AddUserDataClass
import app.retvens.rown.DataCollections.GroupCreate
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.decodeSampledBitmapFromFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class UploadIcon : AppCompatActivity() {

    companion object {
        private const val MESIBO_MESSAGING_REQUEST_CODE = 1001
    }

    private lateinit var groupId:String
    private lateinit var description:String
    private var type:String = ""
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var adapter: UploadIconAdapter
    private lateinit var profile:ShapeableImageView
    private lateinit var select:ImageView
    private lateinit var dialog:Dialog
    private lateinit var latitude:String
    private lateinit var longitude:String
    private  var communityUri:Uri? = null
    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private  var cameraImageUri:Uri? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if (it == true) {
            cropProfileImage(cameraImageUri!!)
        }
    }

    lateinit var progressDialog: Dialog

    lateinit var name: String
    lateinit var location: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_icon)

        findViewById<ImageButton>(R.id.createCommunity_backBtn).setOnClickListener { onBackPressed() }

        cameraImageUri = createImageUri()!!

        profile = findViewById(R.id.profile)
        select = findViewById(R.id.camera)

        select.setOnClickListener {
            openBottomCameraSheet()
        }


        val members = intent.getStringArrayListExtra("number")
        val names = intent.getStringArrayListExtra("names")
        val profile = intent.getStringArrayListExtra("pic")
        val userId = intent.getStringArrayListExtra("userId")


        name = intent.getStringExtra("name").toString()
        description = intent.getStringExtra("desc")!!
        location = intent.getStringExtra("location").toString()
        type = intent.getStringExtra("type")!!
        latitude = intent.getStringExtra("latitude")!!
        longitude = intent.getStringExtra("longitude")!!

        Log.e("location",location.toString())

        val setName = findViewById<TextView>(R.id.Community_Name)

        setName.text = name

        myRecyclerView = findViewById(R.id.recyclerUpload)
        myRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)



        Log.e("number",members.toString())
        Log.e("name",names.toString())
        Log.e("profile",profile.toString())

        adapter = UploadIconAdapter(this,members,names,profile)
        myRecyclerView.adapter = adapter

        val next = findViewById<ImageView>(R.id.nextUpload)

        next.setOnClickListener {
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
            val name = intent.getStringExtra("name")
            if (communityUri != null) {
                CreateGroup(name.toString())
            }else{
                Toast.makeText(applicationContext,"Select Community Profile First!!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

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
                cropProfileImage(imageUri)
            }
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            } else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == MESIBO_MESSAGING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val cameFromUploadIcon = data?.getBooleanExtra("cameFromUploadIcon", false)
                if (cameFromUploadIcon == true) {
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    startActivity(intent)
                    finishActivity(requestCode)
                }
            }
        } else {
            // Handle other activity results
        }
    }

    fun getProfile(): MesiboProfile {
        if (groupId.toLong() > 0) return Mesibo.getProfile(groupId.toLong())
        return Mesibo.getSelfProfile()
    }

    private fun CreateGroup(name:String) {


        val sharedPreferences1 = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        val data = RetrofitBuilder.retrofitBuilder.createGroup(user_id, GroupCreate(name))

        data.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    groupId = response.group.gid.toString()
                    addMembers()

                }else{
                    Toast.makeText(applicationContext,response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun addMembers() {
        val members = intent.getStringArrayListExtra("number")

// Remove any non-digit characters from each phone number
        val cleanedNumbers = members!!.map { it.replace(Regex("[^\\d]"), "+") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")

//        Toast.makeText(applicationContext,formattedMembers.toString(),Toast.LENGTH_SHORT).show()
        Log.e("string",formattedMembers.toString())
//        Toast.makeText(applicationContext,groupId.toString(),Toast.LENGTH_SHORT).show()

//        Toast.makeText(applicationContext,members.toString(),Toast.LENGTH_SHORT).show()

        val data = AddMemberData(groupId, formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.result.toString(),Toast.LENGTH_SHORT).show()
                    createCommunity()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createCommunity() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            communityUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "${app.retvens.rown.utils.getRandomString(6)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.feedsApi.createCommunities(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),groupId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),location),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),latitude),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),longitude),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),type),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),description),
            MultipartBody.Part.createFormData("Profile_pic", file.name, body)
            )

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    addCommunityMember()

                    val profile = getProfile()
                    profile.image = decodeSampledBitmapFromFile(file,200,150)
                    profile.save()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun addCommunityMember() {
        val members = intent.getStringArrayListExtra("userId")

        for (x in members!!){

           val data = AddUserDataClass(x)

            val send = RetrofitBuilder.feedsApi.addUser(groupId,data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful){
                        progressDialog.dismiss()
                        val response = response.body()!!
//                        Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext,MesiboMessagingActivity::class.java)
                        intent.putExtra("admin","true")
                        intent.putExtra(MesiboUI.GROUP_ID,groupId.toLong())
                        intent.putExtra("page","2")
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })

        }



    }
    fun cropProfileImage(imageUri: Uri) {
        val inputUri = imageUri
        val outputUri = File(filesDir, "croppedImage.jpg").toUri()

        val options : UCrop.Options = UCrop.Options()
        options.setCircleDimmedLayer(true)
        UCrop.of(inputUri, outputUri)
            .withAspectRatio(1F, 1F)
            .withOptions(options)
            .start(this)
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

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}