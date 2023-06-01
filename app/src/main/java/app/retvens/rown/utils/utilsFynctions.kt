package app.retvens.rown.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.ProfileCompletionStatus
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

var role = ""
var profileCompletionStatus = "50"
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
