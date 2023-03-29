package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardContinue.setOnClickListener {
            if (binding.editPhone.length() < 10){
                Toast.makeText(this,"Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }else{
                val countryCode = binding.countryCode.selectedCountryCode
                val phoneNo = binding.editPhone.text.toString()
                val phone = "$countryCode $phoneNo"
                showBottomDialog(phone)
            }
        }
    }

    private fun showBottomDialog(phone : String) {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val phoneN = dialog.findViewById<TextView>(R.id.phoneNo)
        phoneN.text = phone.toString()

        dialog.findViewById<CardView>(R.id.card_send_otp).setOnClickListener {
            Toast.makeText(applicationContext,"Sent",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,OtpVerification::class.java)
            intent.putExtra("phone",phone)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.card_change_phone).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}