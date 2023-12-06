package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLanguageSettingBinding
import app.retvens.rown.utils.loadLocale
import app.retvens.rown.utils.setLocale

class LanguageSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityLanguageSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale(this)
        binding = ActivityLanguageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        val language_arabic = findViewById<ImageView>(R.id.language_arabic)
        val language_english = findViewById<ImageView>(R.id.language_english)
        val language_hindi = findViewById<ImageView>(R.id.language_hindi)
        val language_spanish = findViewById<ImageView>(R.id.language_spanish)
        val language_german = findViewById<ImageView>(R.id.language_german)
        val language_japanese = findViewById<ImageView>(R.id.language_japanese)
        val language_portuguese = findViewById<ImageView>(R.id.language_portuguese)
        val language_italian = findViewById<ImageView>(R.id.language_italian)
        val language_french = findViewById<ImageView>(R.id.language_french)
        val language_russian = findViewById<ImageView>(R.id.language_russian)
        val language_chinese = findViewById<ImageView>(R.id.language_chinese)

        val r1 = findViewById<RadioButton>(R.id.radio_1)
        val r2 = findViewById<RadioButton>(R.id.radio_2)
        val r3 = findViewById<RadioButton>(R.id.radio_3)
        val r4 = findViewById<RadioButton>(R.id.radio_4)
        val r5 = findViewById<RadioButton>(R.id.radio_5)
        val r6 = findViewById<RadioButton>(R.id.radio_6)
        val r7 = findViewById<RadioButton>(R.id.radio_7)
        val r8  = findViewById<RadioButton>(R.id.radio_8)
        val r9 = findViewById<RadioButton>(R.id.radio_9)
        val r10 = findViewById<RadioButton>(R.id.radio_10)
        val r11 = findViewById<RadioButton>(R.id.radio_11)

        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        when (sharedPreferences.getString("MY_LANG", "")) {
            "ar" -> {
                r1.isChecked = true
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_arabic.setImageResource(R.drawable.arabic_language_selected)
            }
            "" -> {
                language_english.setImageResource(R.drawable.english_language_selected)
                r1.isChecked = false
                r2.isChecked = true
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
            }
            "hi" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = true
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_hindi.setImageResource(R.drawable.hindi_language_selected)
            }
            "es" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = true
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_spanish.setImageResource(R.drawable.spanish_language_selected)
            }
            "de" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = true
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_german.setImageResource(R.drawable.german_language_selected)
            }
            "ja" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = true
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_japanese.setImageResource(R.drawable.japanese_language_selected)
            }
            "pt" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = true
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_portuguese.setImageResource(R.drawable.portuguese_language_selected)
            }
            "it" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = true
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = false
                language_italian.setImageResource(R.drawable.italian_language_selected)
            }
            "fr" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = true
                r10.isChecked = false
                r11.isChecked = false
                language_french.setImageResource(R.drawable.french_language_selected)
            }
            "ru" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = true
                r11.isChecked = false
                language_russian.setImageResource(R.drawable.russian_language_selected)
            }
            "zh" -> {
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                r5.isChecked = false
                r6.isChecked = false
                r7.isChecked = false
                r8.isChecked = false
                r9.isChecked = false
                r10.isChecked = false
                r11.isChecked = true
                language_chinese.setImageResource(R.drawable.chinese_language_selected)
            }
        }

        r1.setOnClickListener {
            r1.isChecked = true
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("ar",applicationContext)
            recreate()
        }
        r2.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = true
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("",applicationContext)
            recreate()
        }
        r3.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = true
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("hi",applicationContext)
            recreate()
        }
        r4.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = true
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("es",applicationContext)
            recreate()
        }
        r5.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = true
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("de",applicationContext)
            recreate()
        }
        r6.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = true
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("ja",applicationContext)
            recreate()
        }
        r7.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = true
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("pt",applicationContext)
            recreate()
        }
        r8.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = true
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = false
            setLocale("it",applicationContext)
            recreate()
        }
        r9.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = true
            r10.isChecked = false
            r11.isChecked = false
            setLocale("fr",applicationContext)
            recreate()
        }
        r10.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = true
            r11.isChecked = false
            setLocale("ru",applicationContext)
            recreate()
        }
        r11.setOnClickListener {
            r1.isChecked = false
            r2.isChecked = false
            r3.isChecked = false
            r4.isChecked = false
            r5.isChecked = false
            r6.isChecked = false
            r7.isChecked = false
            r8.isChecked = false
            r9.isChecked = false
            r10.isChecked = false
            r11.isChecked = true
            setLocale("zh",applicationContext)
            recreate()
        }
    }
}