package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import app.retvens.rown.R
import app.retvens.rown.utils.setLocale
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetLanguage : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetLanguagelickListener ? = null
    fun setOnLangClickListener(listener: OnBottomSheetLanguagelickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetLanguage? {
        return BottomSheetLanguage()
    }
    interface OnBottomSheetLanguagelickListener{
        fun bottomLangClick(language : String)
    }

    companion object {
        const val CTC_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.bt_close).setOnClickListener { dismiss() }

        val language_arabic = view.findViewById<ImageView>(R.id.language_arabic)
        val language_english = view.findViewById<ImageView>(R.id.language_english)
        val language_hindi = view.findViewById<ImageView>(R.id.language_hindi)
        val language_spanish = view.findViewById<ImageView>(R.id.language_spanish)
        val language_german = view.findViewById<ImageView>(R.id.language_german)
        val language_japanese = view.findViewById<ImageView>(R.id.language_japanese)
        val language_portuguese = view.findViewById<ImageView>(R.id.language_portuguese)
        val language_italian = view.findViewById<ImageView>(R.id.language_italian)
        val language_french = view.findViewById<ImageView>(R.id.language_french)
        val language_russian = view.findViewById<ImageView>(R.id.language_russian)
        val language_chinese = view.findViewById<ImageView>(R.id.language_chinese)

        val r1 = view.findViewById<RadioButton>(R.id.radio_1)
        val r2 =view.findViewById<RadioButton>(R.id.radio_2)
        val r3 = view.findViewById<RadioButton>(R.id.radio_3)
        val r4 = view.findViewById<RadioButton>(R.id.radio_4)
        val r5 = view.findViewById<RadioButton>(R.id.radio_5)
        val r6 = view.findViewById<RadioButton>(R.id.radio_6)
        val r7 = view.findViewById<RadioButton>(R.id.radio_7)
        val r8  = view.findViewById<RadioButton>(R.id.radio_8)
        val r9 = view.findViewById<RadioButton>(R.id.radio_9)
        val r10 = view.findViewById<RadioButton>(R.id.radio_10)
        val r11 = view.findViewById<RadioButton>(R.id.radio_11)

        val sharedPreferences = context?.getSharedPreferences("Settings", AppCompatActivity.MODE_PRIVATE)
        when (sharedPreferences?.getString("MY_LANG", "")) {
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
            mListener?.bottomLangClick("ar")
            dismiss()
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
            mListener?.bottomLangClick("")
            dismiss()
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
            mListener?.bottomLangClick("hi")
            dismiss()
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
            mListener?.bottomLangClick("es")
            dismiss()
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
            mListener?.bottomLangClick("de")
            dismiss()
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
            mListener?.bottomLangClick("ja")
            dismiss()
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
            mListener?.bottomLangClick("pt")
            dismiss()
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
            mListener?.bottomLangClick("it")
            dismiss()
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
            mListener?.bottomLangClick("fr")
            dismiss()
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
            mListener?.bottomLangClick("ru")
            dismiss()
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
            mListener?.bottomLangClick("zh")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}