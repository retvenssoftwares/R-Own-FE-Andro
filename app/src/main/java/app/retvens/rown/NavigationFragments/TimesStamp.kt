package app.retvens.rown.NavigationFragments

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimesStamp {

    fun convertTimeToText(dataDate: String): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "Ago"

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val pasTime: Date = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (day >= 1) {
                convTime = "$day D $suffix"
            } else {
                val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
                if (hour >= 1) {
                    convTime = "$hour Hrs $suffix"
                } else {
                    val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                    if (minute >= 1) {
                        convTime = "$minute Min $suffix"
                    } else {
                        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                        convTime = "$second Sec $suffix"
                    }
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

}