package app.retvens.rown.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit


fun setupFullHeight(bottomSheet: View) {
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    bottomSheet.layoutParams = layoutParams
}

fun convertTimeToText(dataDate: String): String? {
    var convTime: String? = null
    val suffix = "Ago"

    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val pasTime: Date = dateFormat.parse(dataDate)
        val nowTime = Date()
        val dateDiff = nowTime.time - pasTime.time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hours = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val days = TimeUnit.MILLISECONDS.toDays(dateDiff)

        when {
            seconds < 60 -> convTime = "$seconds Sec $suffix"
            minutes < 60 -> convTime = "$minutes Min $suffix"
            hours < 24 -> convTime = "$hours Hrs $suffix"
            days >= 7 -> {
                convTime = when {
                    days > 360 -> "${days / 360} Yr $suffix"
                    days > 30 -> "${days / 30} Months $suffix"
                    else -> "${days / 7} Week $suffix"
                }
            }
            days < 7 -> convTime = "$days D $suffix"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message!!)
    }
    return convTime
}


fun postLike(postId:String, context : Context, onClick : () -> Unit) {

    val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()


    val data = LikesCollection(user_id)

    val like = RetrofitBuilder.feedsApi.postLike(postId,data)

    like.enqueue(object : Callback<UpdateResponse?> {
        override fun onResponse(
            call: Call<UpdateResponse?>,
            response: Response<UpdateResponse?>
        ) {
                if (response.isSuccessful) {
                    onClick.invoke()
//                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                } else {
//                    Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT).show()
                }

        }
        override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT)
//                    .show()
        }
    })

}
