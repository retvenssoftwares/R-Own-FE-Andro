package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding

class OtpVerification : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textPhoneOtp.text = intent.getStringExtra("phone").toString()

        val pi = intent.getStringExtra("PI").toString()
        if (pi == "PI"){
            binding.cardVerifyOtp.visibility = View.GONE
        }

        binding.cardVerifyOtp.setOnClickListener {
            startActivity(Intent(this,PersonalInformation::class.java))
        }
    }
}