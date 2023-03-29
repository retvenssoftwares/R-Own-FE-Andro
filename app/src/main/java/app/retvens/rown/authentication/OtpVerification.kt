package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerification : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifificationBinding

    lateinit var otpET1 : EditText
    lateinit var otpET2 : EditText
    lateinit var otpET3 : EditText
    lateinit var otpET4 : EditText
    lateinit var otpET5 : EditText
    lateinit var otpET6 : EditText
    lateinit var otp : String

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpET1 = findViewById(R.id.otpET1)
        otpET2 = findViewById(R.id.otpET2)
        otpET3 = findViewById(R.id.otpET3)
        otpET4 = findViewById(R.id.otpET4)
        otpET5 = findViewById(R.id.otpET5)
        otpET6 = findViewById(R.id.otpET6)

        otp = "$otpET1$otpET2$otpET3$otpET4$otpET5$otpET6"

        auth=FirebaseAuth.getInstance()

        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        binding.textPhoneOtp.text = intent.getStringExtra("phone").toString()

        binding.cardVerifyOtp.setOnClickListener {

            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this,PersonalInformation::class.java))
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, PersonalInformation::class.java))
                    Toast.makeText(applicationContext,"Otp Verified Successfully",Toast.LENGTH_SHORT).show()
                    finish()
// ...
                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}