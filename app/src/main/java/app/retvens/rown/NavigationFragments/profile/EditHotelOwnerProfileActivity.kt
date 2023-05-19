package app.retvens.rown.NavigationFragments.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEditHotelOwnerProfileBinding

class EditHotelOwnerProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditHotelOwnerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelOwnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hotelType.setOnClickListener {
            openHotelTypeBottom()
        }

    }
    private fun openHotelTypeBottom() {

        val dialogRole = Dialog(this)
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.singleHotelRL).setOnClickListener {
            binding.hotelType.text = "Single Hotel"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.hotelChainRL).setOnClickListener {
            binding.hotelType.text = "Hotel Chain"
            dialogRole.dismiss()
        }
    }

}