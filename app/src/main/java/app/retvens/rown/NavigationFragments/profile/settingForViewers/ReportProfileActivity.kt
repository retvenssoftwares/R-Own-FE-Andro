package app.retvens.rown.NavigationFragments.profile.settingForViewers

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.cardview.widget.CardView
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityReportProfileBinding

class ReportProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityReportProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.specificPost.setOnClickListener {
            startActivity(Intent(this, SelectPostActivity::class.java))
        }

        binding.wrongWith.setOnClickListener {

            val dialogLanguage = Dialog(this)
            dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogLanguage.setContentView(R.layout.bottom_sheet_reason_for_report)
            dialogLanguage.setCancelable(true)

            dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogLanguage.window?.setGravity(Gravity.BOTTOM)
            dialogLanguage.show()

            dialogLanguage.findViewById<CardView>(R.id.card_report_next).setOnClickListener {
                startActivity(Intent(this, SelectPostActivity::class.java))
            }

        }

    }
}