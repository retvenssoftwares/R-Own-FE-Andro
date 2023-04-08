package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import app.retvens.rown.R
import java.util.*

class BasicFun() {
    fun openBottomLanguageSheet(context:Context, intent: Intent) {
        val dialogLanguage = Dialog(context)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_sheet_language)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        val language_arabic = dialogLanguage.findViewById<ImageView>(R.id.language_arabic)
        val language_english = dialogLanguage.findViewById<ImageView>(R.id.language_english)
        val language_hindi = dialogLanguage.findViewById<ImageView>(R.id.language_hindi)
        val language_spanish = dialogLanguage.findViewById<ImageView>(R.id.language_spanish)
        val language_german = dialogLanguage.findViewById<ImageView>(R.id.language_german)
        val language_japanese = dialogLanguage.findViewById<ImageView>(R.id.language_japanese)
        val language_portuguese = dialogLanguage.findViewById<ImageView>(R.id.language_portuguese)
        val language_italian = dialogLanguage.findViewById<ImageView>(R.id.language_italian)
        val language_french = dialogLanguage.findViewById<ImageView>(R.id.language_french)
        val language_russian = dialogLanguage.findViewById<ImageView>(R.id.language_russian)
        val language_chinese = dialogLanguage.findViewById<ImageView>(R.id.language_chinese)

        val r1 = dialogLanguage.findViewById<RadioButton>(R.id.radio_1)
        val r2 =dialogLanguage.findViewById<RadioButton>(R.id.radio_2)
        val r3 = dialogLanguage.findViewById<RadioButton>(R.id.radio_3)
        val r4 = dialogLanguage.findViewById<RadioButton>(R.id.radio_4)
        val r5 = dialogLanguage.findViewById<RadioButton>(R.id.radio_5)
        val r6 = dialogLanguage.findViewById<RadioButton>(R.id.radio_6)
        val r7 = dialogLanguage.findViewById<RadioButton>(R.id.radio_7)
        val r8  = dialogLanguage.findViewById<RadioButton>(R.id.radio_8)
        val r9 = dialogLanguage.findViewById<RadioButton>(R.id.radio_9)
        val r10 = dialogLanguage.findViewById<RadioButton>(R.id.radio_10)
        val r11 = dialogLanguage.findViewById<RadioButton>(R.id.radio_11)

        dialogLanguage.findViewById<ImageView>(R.id.bt_close).setOnClickListener {
            dialogLanguage.dismiss()
        }

        val sharedPreferences = context.getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        val language = sharedPreferences.getString("MY_LANG", "")

        /* when{
             language == "ar" -> r1.isChecked = true
             language == "" -> r2.isChecked = true
             language == "hi" -> r3.isChecked = true
             language == "es" -> r4.isChecked = true
             language == "de" -> r5.isChecked = true
             language == "ja" -> r6.isChecked = true
             language == "pt" -> r7.isChecked = true
             language == "it" -> r8.isChecked = true
             language == "fr" -> r9.isChecked = true
             language == "ru" -> r10.isChecked = true
             language == "zh" -> r11.isChecked = true
         }*/

        if (language == "ar"){
            r1.isChecked = true
            language_arabic.setImageResource(R.drawable.arabic_language_selected)
        } else if (language == ""){
            r2.isChecked = true
            language_english.setImageResource(R.drawable.english_language_selected)
        } else if (language == "hi"){
            r3.isChecked = true
            language_hindi.setImageResource(R.drawable.hindi_language_selected)
        } else if (language == "es"){
            r4.isChecked = true
            language_spanish.setImageResource(R.drawable.spanish_language_selected)
        } else if (language == "de"){
            r5.isChecked = true
            language_german.setImageResource(R.drawable.german_language_selected)
        } else if (language == "ja"){
            r6.isChecked = true
            language_japanese.setImageResource(R.drawable.japanese_language_selected)
        } else if (language == "pt"){
            r7.isChecked = true
            language_portuguese.setImageResource(R.drawable.portuguese_language_selected)
        } else if (language == "it"){
            r8.isChecked = true
            language_italian.setImageResource(R.drawable.italian_language_selected)
        } else if (language == "fr"){
            r9.isChecked = true
            language_french.setImageResource(R.drawable.french_language_selected)
        } else if (language == "ru"){
            r10.isChecked = true
            language_russian.setImageResource(R.drawable.russian_language_selected)
        } else if (language == "zh"){
            r11.isChecked = true
            language_chinese.setImageResource(R.drawable.chinese_language_selected)
        }

        r1.setOnClickListener {
            setLocale("ar",context)
            dialogLanguage.dismiss()
//            recreate()

        }
        r2.setOnClickListener {
            setLocale("", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r3.setOnClickListener {
            setLocale("hi", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r4.setOnClickListener {
            setLocale("es", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r5.setOnClickListener {
            setLocale("de", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r6.setOnClickListener {
            setLocale("ja", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r7.setOnClickListener {
            setLocale("pt", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r8.setOnClickListener {
            setLocale("it", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r9.setOnClickListener {
            setLocale("fr", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r10.setOnClickListener {
            setLocale("ru", context)
            dialogLanguage.dismiss()
//            recreate()
        }
        r11.setOnClickListener {
            setLocale("zh", context)
            dialogLanguage.dismiss()
//            recreate()
        }
    }

    fun setLocale(language: String, context: Context) {
        val locale  = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

        val editor : SharedPreferences.Editor = context.getSharedPreferences("Settings",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString("MY_LANG", language)
        editor.apply()
    }
    fun loadLocale(context:Context){
        val sharedPreferences = context.getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        val language = sharedPreferences.getString("MY_LANG", "")
        setLocale(language!!, context)
    }
}