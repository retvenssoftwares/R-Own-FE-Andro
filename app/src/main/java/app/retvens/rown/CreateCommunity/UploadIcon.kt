package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.ChatSection.GroupChat
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.AddMemberData
import app.retvens.rown.DataCollections.FeedCollection.AddUserDataClass
import app.retvens.rown.DataCollections.GroupCreate
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import com.google.android.material.imageview.ShapeableImageView
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI

class UploadIcon : AppCompatActivity() {

    private lateinit var groupId:String
    private lateinit var description:String
    private var type:String = ""
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var adapter:UploadIconAdapter
    private lateinit var profile:ShapeableImageView
    private lateinit var select:ImageView
    private lateinit var dialog:Dialog
    private  var communityUri:Uri? = null
    var PICK_IMAGE_REQUEST_CODE : Int = 0

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){

        profile.setImageURI(communityUri)
    }

    lateinit var name: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_icon)

        profile = findViewById(R.id.profile)
        select = findViewById(R.id.camera)

        select.setOnClickListener {
            openBottomCameraSheet()
        }


        val members = intent.getStringArrayListExtra("number")
        val names = intent.getStringArrayListExtra("names")
        val profile = intent.getStringArrayListExtra("pic")


        name = intent.getStringExtra("name").toString()
        description = intent.getStringExtra("desc")!!
        type = intent.getStringExtra("type")!!

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
            val name = intent.getStringExtra("name")
            CreateGroup(name.toString())

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
        contract.launch(communityUri)
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
                communityUri = imageUri
                profile.setImageURI(communityUri)
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri
                communityUri = croppedImage
                profile.setImageURI(communityUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(applicationContext,"Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun CreateGroup(name:String) {

        val GroupName = GroupCreate(name)

        val data = RetrofitBuilder.retrofitBuilder.createGroup(GroupName)

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
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun addMembers() {
        val members = intent.getStringArrayListExtra("number")

// Remove any non-digit characters from each phone number
        val cleanedNumbers = members!!.map { it.replace(Regex("[^\\d]"), "") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")


        Toast.makeText(applicationContext,"$formattedMembers",Toast.LENGTH_SHORT).show()

        val data = AddMemberData(groupId, formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.result.toString(),Toast.LENGTH_SHORT).show()
                    createCommunity()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,"error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createCommunity() {

        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            communityUri!!,"r",null
        )?:return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "cropped_${contentResolver.getFileName(communityUri!!)}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"image")

        val send = RetrofitBuilder.feedsApi.createCommunities(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),groupId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"indore"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),type),
            MultipartBody.Part.createFormData("Profile_pic", file.name, body)
            )

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                    addCommunityMember()
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
        val members = intent.getStringArrayListExtra("number")

        for (x in members!!){

           val data = AddUserDataClass("",x,"")

            val send = RetrofitBuilder.feedsApi.addUser(groupId,data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful){
                        val response = response.body()!!
                        Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,DashBoardActivity::class.java))
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
}