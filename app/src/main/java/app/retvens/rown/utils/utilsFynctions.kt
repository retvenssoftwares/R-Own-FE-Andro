package app.retvens.rown.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.ProfileCompletionStatus
import app.retvens.rown.Dashboard.profileCompletion.frags.VendorsFragment
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

var role = ""
var profileCompletionStatus = "50"
var websiteLinkV = ""
var phone = ""

fun dateFormat(date : String) : String {
    // Note, MM is months, not mm
    val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)

    val datee: Date = inputFormat.parse(date)
    val outputText: String = outputFormat.format(datee)

    return outputText
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

    UCrop.of(inputUri, outputUri)
        .withAspectRatio(4F, 3F)
        .start(context as Activity)
}

fun cropImage1(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    UCrop.of(inputUri, outputUri)
        .withAspectRatio(4F, 3F)
        .withMaxResultSize(2000,2000)
        .start(context as Activity)

}
fun cropProfileImage(imageUri: Uri, context: Context) {
    val inputUri = imageUri
    val outputUri = File(context.filesDir, "croppedImage.jpg").toUri()

    UCrop.of(inputUri, outputUri)
        .withAspectRatio(1F, 1F)
        .start(context as Activity)
}
fun compressImage(imageUri: Uri, context: Context): Uri {
    lateinit var compressed : Uri
    try {
        val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,imageUri)
        val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val fileName = String.format("%d.jpg",System.currentTimeMillis())
        val finalFile = File(path,fileName)
        val fileOutputStream = FileOutputStream(finalFile)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        compressed = Uri.fromFile(finalFile)

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.setData(compressed)
        context.sendBroadcast(intent)
    }catch (e: IOException){
        e.printStackTrace()
    }
    return compressed
}
