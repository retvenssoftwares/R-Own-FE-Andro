package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding

class OtpVerification : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifificationBinding

    lateinit var otpET1 : EditText
    lateinit var otpET2 : EditText
    lateinit var otpET3 : EditText
    lateinit var otpET4 : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpET1 = findViewById(R.id.otpET1)
        otpET2 = findViewById(R.id.otpET2)
        otpET3 = findViewById(R.id.otpET3)
        otpET4 = findViewById(R.id.otpET4)

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