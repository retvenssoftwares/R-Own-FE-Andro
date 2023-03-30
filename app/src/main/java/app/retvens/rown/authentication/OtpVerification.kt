package app.retvens.rown.authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.MesiboDataClass
import app.retvens.rown.DataCollections.MesiboResponseClass
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVerification : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifificationBinding

    lateinit var otpET1: EditText
    lateinit var otpET2: EditText
    lateinit var otpET3: EditText
    lateinit var otpET4: EditText
    lateinit var otpET5: EditText
    lateinit var otpET6: EditText

    var selectedETPosition: Int = 0

    lateinit var otp: String

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

        otpET1.addTextChangedListener(textWatcher)
        otpET2.addTextChangedListener(textWatcher)
        otpET3.addTextChangedListener(textWatcher)
        otpET4.addTextChangedListener(textWatcher)
        otpET5.addTextChangedListener(textWatcher)
        otpET6.addTextChangedListener(textWatcher)

        showKeyBoard(otpET1)

        auth = FirebaseAuth.getInstance()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        binding.textPhoneOtp.text = intent.getStringExtra("phone").toString()

        binding.cardVerifyOtp.setOnClickListener {

            val otp1 = otpET1.text.toString()
            val otp2 = otpET2.text.toString()
            val otp3 = otpET3.text.toString()
            val otp4 = otpET4.text.toString()
            val otp5 = otpET5.text.toString()
            val otp6 = otpET6.text.toString()

            otp = "$otp1$otp2$otp3$otp4$otp5$otp6"


            if (!otp.isEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, PersonalInformation::class.java))
                    Toast.makeText(
                        applicationContext,
                        "Otp Verified Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    createUserMesibo()
                    finish()
// ...
                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    //MesiboUserCreation

    private fun createUserMesibo() {

        val number =  intent.getStringExtra("phone").toString()
        val send = MesiboDataClass(number)
        val data = RetrofitBuilder.retrofitBuilder.createMesiboUser(send)

        data.enqueue(object : Callback<MesiboResponseClass?> {
            override fun onResponse(
                call: Call<MesiboResponseClass?>,
                response: Response<MesiboResponseClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!



                    Toast.makeText(applicationContext,response.token,Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext,response.uid,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MesiboResponseClass?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
        override fun afterTextChanged(s: Editable?) {
            if (s!!.length > 0) {
                if (selectedETPosition == 0) {
                    selectedETPosition = 1
                    showKeyBoard(otpET2)
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2
                    showKeyBoard(otpET3)
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3
                    showKeyBoard(otpET4)
                } else if (selectedETPosition == 3) {
                    selectedETPosition = 4
                    showKeyBoard(otpET5)
                } else if (selectedETPosition == 4) {
                    selectedETPosition = 5
                    showKeyBoard(otpET6)
                }else if (selectedETPosition == 5){
                    binding.textVerifyBtn.setBackgroundColor(resources.getColor(R.color.green_own))
                }
            }
        }
    }

    fun showKeyBoard(otpET: EditText) {
        otpET.requestFocus()
        val inputMethodManager: InputMethodManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedETPosition == 5) {
                selectedETPosition = 4
                showKeyBoard(otpET5)
            } else if (selectedETPosition == 4) {
                selectedETPosition = 3
                showKeyBoard(otpET4)
            } else if (selectedETPosition == 3) {
                selectedETPosition = 2
                showKeyBoard(otpET3)
            } else if (selectedETPosition == 2) {
                selectedETPosition = 1
                showKeyBoard(otpET2)
            } else if (selectedETPosition == 1) {
                selectedETPosition = 0
                showKeyBoard(otpET1)
            }
            binding.textVerifyBtn.setBackgroundColor(resources.getColor(R.color.light_grey))
            return true
        } else {
            return super.onKeyUp(keyCode, event)
        }
    }
}
