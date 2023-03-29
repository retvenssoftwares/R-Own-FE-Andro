package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    lateinit var dialog: Dialog

    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var phone:String
    lateinit var phoneNumber:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseApp.initializeApp(this)

        auth=FirebaseAuth.getInstance()

        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
            finish()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                dialog.dismiss()

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(applicationContext,"Otp will be send to Enter Mobile Number",Toast.LENGTH_SHORT).show()
                var intent = Intent(applicationContext,OtpVerification::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("phone",phone)
                startActivity(intent)
            }
        }

        binding.cardContinue.setOnClickListener {
            if (binding.editPhone.length() < 10){
                Toast.makeText(this,"Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }else{
                val countryCode = binding.countryCode.selectedCountryCode
                val phoneNo = binding.editPhone.text.toString()
                phone = "+$countryCode $phoneNo"
                phoneNumber = "+$countryCode$phoneNo"
                showBottomDialog(phone)
            }
        }
    }

    private fun showBottomDialog(phone : String) {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val phoneN = dialog.findViewById<TextView>(R.id.phoneNo)
        phoneN.text = phone.toString()

        dialog.findViewById<CardView>(R.id.card_send_otp).setOnClickListener {
            Toast.makeText(this,"Sending..", Toast.LENGTH_SHORT).show()
            sendVerificationcode(phoneNumber)
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
    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}