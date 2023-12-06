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
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetEditYourPost
import app.retvens.rown.bottomsheet.BottomSheetReport
import app.retvens.rown.databinding.ActivityReportProfileBinding

class ReportProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityReportProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("user_id")

        binding.communityBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding.wrongWith.setOnClickListener {

            val bottomSheet = BottomSheetReport(userId!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditYourPost.Hotelier_TAG)}


        }

    }

}