package app.retvens.rown.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.ProfileCompletionStatus
import app.retvens.rown.Dashboard.profileCompletion.frags.VendorsFragment
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

var role = ""
var profileCompletionStatus = "50"
var websiteLinkV = ""
var phone = ""
var profileImage = ""
var verificationStatus = ""
var connectionCount = "1"
var serverCode = 0
var isBS:Boolean = true

fun dateFormat(date : String) : String {
// Note, MM is months, not mm
//    val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
//
//    val datee: Date = inputFormat.parse(date)
//    val outputText: String = outputFormat.format(datee)
//
//    return outputText

    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var outputText2 = ""
    try {
    val outputText = inputFormat.parse(date)
     outputText2 = outputFormat.format(outputText)
    } catch (e:ParseException){ }

    return outputText2
}

fun showCalendar(context: Context, date: (String)-> Unit) {

    val cal = Calendar.getInstance()
    val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date.invoke(sdf.format(cal.time).toString())
    }

    DatePickerDialog(context, dateSetListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)).show()

}

fun setTime(context: Context, time : (String) -> Unit){

    val mTimePicker: TimePickerDialog
    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)

    mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            time.invoke((String.format("%02d:%02d", hourOfDay, minute)))
        }
    }, hour, minute, false)

    mTimePicker.show()
}


fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

 fun profileComStatus(context: Context, progress : String) {
    val sharedPreferencesU = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferencesU?.getString("user_id", "").toString()

    val pcs = RetrofitBuilder.profileCompletion.profileCompletionStatus(user_id, ProfileCompletionStatus(progress))
    pcs.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {

        }

        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {

        }
    })
}

fun prepareFilePart(fileUri: Uri, schema : String, context: Context): MultipartBody.Part? {

    val filesDir = context.filesDir
    val file = File(filesDir,"${getRandomString(6)}.png")

    val inputStream = context.contentResolver.openInputStream(fileUri)
    val outputStream = FileOutputStream(file)
    inputStream!!.copyTo(outputStream)

    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(schema, file.name, requestBody)
}

fun cropImage(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    val options : UCrop.Options = UCrop.Options()
    UCrop.of(inputUri, outputUri)
        .withAspectRatio(3F, 4F)
        .withOptions(options)
        .start(context as Activity)
}

fun cropImageHorizontal(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    val options : UCrop.Options = UCrop.Options()
    UCrop.of(inputUri, outputUri)
        .withAspectRatio(16F, 9F)
        .withMaxResultSize(2000,2000)
        .withOptions(options)
        .start(context as Activity)

}
fun cropImageFree(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    val options : UCrop.Options = UCrop.Options()
    UCrop.of(inputUri, outputUri)
        .withMaxResultSize(2000,2000)
        .withOptions(options)
        .start(context as Activity)

}
fun cropProfileImage(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    val options : UCrop.Options = UCrop.Options()
    options.setCircleDimmedLayer(true)
    UCrop.of(inputUri, outputUri)
        .withAspectRatio(1F, 1F)
        .withOptions(options)
        .start(context as Activity)
}
//fun compressImage(imageUri: Uri, context: Context): Uri {
//    var compressed : Uri? = null
//    try {
//        try {
//            val imageBitmap: Bitmap =
//                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//            val path: File =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//            val fileName = String.format("%d.jpg", System.currentTimeMillis())
//            val finalFile = File(path, fileName)
//            val fileOutputStream = FileOutputStream(finalFile)
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream)
//            fileOutputStream.flush()
//            fileOutputStream.close()
//
//            compressed = Uri.fromFile(finalFile)
//
//            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//            intent.setData(compressed)
//            context.sendBroadcast(intent)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }catch (e:NullPointerException){
//        Log.e("error",e.message.toString())
//    }
//    return compressed!!
//}

fun compressImage(imageUri: Uri, context: Context): Uri? {
    var compressed: Uri? = null
    try {
        val imageBitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        val compressedDisplayName = "compressed_${System.currentTimeMillis()}.jpg" // Unique display name

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, compressedDisplayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val contentResolver = context.contentResolver
        val compressedUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        compressedUri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            outputStream?.flush()
            outputStream?.close()

            compressed = compressedUri

            // Update the media scanner
            MediaScannerConnection.scanFile(context, arrayOf(compressedUri.toString()), null, null)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: NullPointerException) {
        Log.e("error", e.message.toString())
    }
    return compressed
}


fun showFullImage(profilePic : String, context: Context) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_full_image)


    val profile = dialog.findViewById<ImageView>(R.id.profile)
    Glide.with(context).load(profilePic).into(profile)

    dialog.show()
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
}


fun removeConnection(userID: String, userId: String, context: Context, onClick: () -> Unit) {
    val remove = RetrofitBuilder.connectionApi.removeConnection(userID, ConnectionDataClass(userId))
    remove.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
//                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                onClick.invoke()
            } else {
//                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//            Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

 fun removeConnRequest(userId: String, context: Context, onClick: () -> Unit) {
    val sharedPreferences =  context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    val removeRequest = RetrofitBuilder.connectionApi.removeRequest(userId,
        ConnectionDataClass(user_id)
    )

    removeRequest.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
                val response = response.body()!!
                onClick.invoke()
//                Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
            }else{
//                Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {

        }
    })
}

 fun rejectConnRequest(userId: String, context: Context, onClick : () -> Unit) {
    val sharedPreferences =  context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    val removeRequest = RetrofitBuilder.connectionApi.removeRequest(user_id,
        ConnectionDataClass(userId)
    )

    removeRequest.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
                val response = response.body()!!
                onClick.invoke()
//                Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
            }else{
//                Toast.makeText(context,response.code().toString(),Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT)
//                    .show()
        }
    })
}

 fun sendConnectionRequest(userId: String,context: Context, onClick: () -> Unit) {

    val sharedPreferences =  context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()


    val sendRequest = RetrofitBuilder.connectionApi.sendRequest(userId, ConnectionDataClass(user_id))

    sendRequest.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
                val response = response.body()!!
                Log.e("res",response.message.toString())
                onClick.invoke()
            }else{
                Log.e("error",response.code().toString())
            }
        }

        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
           Log.e("error",t.message.toString())
        }
    })

}

 fun acceptRequest(userId: String, context: Context, onClick : () -> Unit) {

    val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    val accept = RetrofitBuilder.connectionApi.sendRequest(user_id, ConnectionDataClass(userId))

    accept.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
                val response = response.body()!!
                onClick.invoke()
//                Toast.makeText(context,"Request Accepted",Toast.LENGTH_SHORT).show()
            }else{
//                Toast.makeText(context,"Request Accepted",Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//            Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
        }
    })
}

 fun getProfileInfo(context: Context, onClick: (Int) -> Unit) {

    val sharedPreferences =
        context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    if (user_id != "") {
        val send = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        send.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                serverCode = response.code()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val response = response.body()!!
                        phone = response.Phone
                        role = response.Role
                        profileCompletionStatus = response.profileCompletionStatus
                        verificationStatus = response.verificationStatus

                        profileImage = response.Profile_pic.toString()

                        websiteLinkV = response.vendorInfo.websiteLink

                        getSelfUserProfile(user_id,user_id, context)

                        saveFullName(context, "${response.Full_name}")
                        saveUserName(context, "${response.User_name}")
                        saveProfileImage(context, "${response.Profile_pic}")
                    }
                } else {
                    onClick.invoke(response.code())
//                    Toast.makeText(
//                        context,
//                        .toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
//                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT)
//                    .show()
            }
        })

    }
}

fun getSelfUserProfile(userId: String, userId1: String, context: Context) {

    val getProfile = RetrofitBuilder.connectionApi.getconnProfile(userId,userId1)

    getProfile.enqueue(object : Callback<NormalUserDataClass?> {
        override fun onResponse(
            call: Call<NormalUserDataClass?>,
            response: Response<NormalUserDataClass?>
        ) {
            serverCode = response.code()
            if (response.isSuccessful){
                val response = response.body()!!

                connectionCount = response.data.connCountLength.toString()

                saveConnectionNo(
                    context,
                    response.data.connCountLength.toString()
                )

//                    Toast.makeText(applicationContext, response.data.connCountLength.toString(), Toast.LENGTH_SHORT)
//                        .show()
            }else{
//                    Toast.makeText(requireContext(),response.code(),Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<NormalUserDataClass?>, t: Throwable) {
//                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
        }
    })

}
