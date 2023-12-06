package app.retvens.rown.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

 fun setLocale(language: String, baseContext: Context) {
    val locale  = Locale(language)
    Locale.setDefault(locale)
    val configuration = Configuration()
    configuration.locale = locale
    baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

    val editor : SharedPreferences.Editor = baseContext.getSharedPreferences("Settings",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("MY_LANG", language)
    editor.apply()
}
fun loadLocale(context: Context){
    val sharedPreferences = context.getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
    val language = sharedPreferences.getString("MY_LANG", "")
    setLocale(language!!, context)
}
