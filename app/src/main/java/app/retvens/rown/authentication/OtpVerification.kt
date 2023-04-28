package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.MesiboDataClass
import app.retvens.rown.DataCollections.MesiboResponseClass
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.onboarding.SearchUser
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding
import app.retvens.rown.utils.moveTo
import app.retvens.rown.utils.saveUserId
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

open class OtpVerification : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifificationBinding
    lateinit var progressDialog : Dialog

    var mLastClickTime : Long = 0
    lateinit var otpET1: EditText
    lateinit var otpET2: EditText
    lateinit var otpET3: EditText
    lateinit var otpET4: EditText
    lateinit var otpET5: EditText
    lateinit var otpET6: EditText
    /*--AUTO-DETECT-OTP--*/
    private val REQ_USER_CONSENT = 200
    var otpBroadcastReciever : OtpBroadcastReciever ?= null


    var selectedETPosition: Int = 0

    lateinit var otp: String

    lateinit var storedVerificationId:String
    lateinit var phone:String
    lateinit var phoneNumber:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
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
//        startSmartUserConsent()

        showKeyBoard(otpET1)

        phoneNumber =  intent.getStringExtra("phoneNum").toString() //without CountryCode
        phone =  intent.getStringExtra("phone").toString() // with CountryCode and +
        binding.textPhoneOtp.text = intent.getStringExtra("phone").toString()
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


                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(applicationContext,"Otp will be resend",Toast.LENGTH_SHORT).show()
            }
        }


        binding.textResendOtp.setOnClickListener {
            binding.textResendOtp.visibility = View.GONE
            binding.countDownTimer.visibility = View.VISIBLE
            val timer = object: CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.countDownTimer.text = "00:${millisUntilFinished/1000}"
                    sendVerificationcode(phone)
                }

                override fun onFinish() {
                    binding.textResendOtp.visibility = View.VISIBLE
                    binding.countDownTimer.visibility = View.GONE
                }
            }
            timer.start()
        }

        binding.languageFromOtp.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            openBottomLanguageSheet()
        }
        auth = FirebaseAuth.getInstance()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")


        binding.cardVerifyOtp.setOnClickListener {

            val otp1 = otpET1.text.toString()
            val otp2 = otpET2.text.toString()
            val otp3 = otpET3.text.toString()
            val otp4 = otpET4.text.toString()
            val otp5 = otpET5.text.toString()
            val otp6 = otpET6.text.toString()

            otp = "$otp1$otp2$otp3$otp4$otp5$otp6"


            if (!otp.isEmpty()) {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()

                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )

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
                    progressDialog.dismiss()


                    Toast.makeText(
                        applicationContext,
                        "Otp Verified Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    createUserMesibo()


// ...
                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dismiss()
// The verification code entered was invalid
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        otpET1.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
                        otpET2.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
                        otpET3.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
                        otpET4.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
                        otpET5.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
                        otpET6.setBackgroundResource(R.drawable.wrong_otp_round_corner_)
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
                    Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                    Log.d("SaveM",response.toString())
                    SharedPreferenceManagerAdmin.getInstance(applicationContext).saveUser(response)

                    val key = intent.getStringExtra("key")
                    if (key == "1"){
                        startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                        finish()
                    }else{
                        searchUser()
                    }

                }else{
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MesiboResponseClass?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }
    private fun searchUser() {
        val search = RetrofitBuilder.retrofitBuilder.searchUser(SearchUser(phoneNumber.toLong()))
        search.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                val user_id = response.body()?.user_id.toString()
                val message = response.body()?.message.toString()
                saveUserId(applicationContext,user_id)
                Toast.makeText(applicationContext,response.body().toString(),Toast.LENGTH_SHORT).show()
                Log.d("search",response.body().toString())
                moveTo(applicationContext,"MoveToPI")
                val intent = Intent(applicationContext, PersonalInformation::class.java)
                intent.putExtra("phone",phoneNumber)
                intent.putExtra("user_id",user_id)
                intent.putExtra("message",message)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                Log.d("search",t.localizedMessage,t)
            }
        })
    }

    private fun openBottomLanguageSheet() {
        val dialogLanguage = Dialog(this)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_sheet_language)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        val language_arabic = dialogLanguage.findViewById<ImageView>(R.id.language_arabic)
        val language_english = dialogLanguage.findViewById<ImageView>(R.id.language_english)
        val language_hindi = dialogLanguage.findViewById<ImageView>(R.id.language_hindi)
        val language_spanish = dialogLanguage.findViewById<ImageView>(R.id.language_spanish)
        val language_german = dialogLanguage.findViewById<ImageView>(R.id.language_german)
        val language_japanese = dialogLanguage.findViewById<ImageView>(R.id.language_japanese)
        val language_portuguese = dialogLanguage.findViewById<ImageView>(R.id.language_portuguese)
        val language_italian = dialogLanguage.findViewById<ImageView>(R.id.language_italian)
        val language_french = dialogLanguage.findViewById<ImageView>(R.id.language_french)
        val language_russian = dialogLanguage.findViewById<ImageView>(R.id.language_russian)
        val language_chinese = dialogLanguage.findViewById<ImageView>(R.id.language_chinese)

        val r1 = dialogLanguage.findViewById<RadioButton>(R.id.radio_1)
        val r2 =dialogLanguage.findViewById<RadioButton>(R.id.radio_2)
        val r3 = dialogLanguage.findViewById<RadioButton>(R.id.radio_3)
        val r4 = dialogLanguage.findViewById<RadioButton>(R.id.radio_4)
        val r5 = dialogLanguage.findViewById<RadioButton>(R.id.radio_5)
        val r6 = dialogLanguage.findViewById<RadioButton>(R.id.radio_6)
        val r7 = dialogLanguage.findViewById<RadioButton>(R.id.radio_7)
        val r8  = dialogLanguage.findViewById<RadioButton>(R.id.radio_8)
        val r9 = dialogLanguage.findViewById<RadioButton>(R.id.radio_9)
        val r10 = dialogLanguage.findViewById<RadioButton>(R.id.radio_10)
        val r11 = dialogLanguage.findViewById<RadioButton>(R.id.radio_11)

        dialogLanguage.findViewById<ImageView>(R.id.bt_close).setOnClickListener {
            dialogLanguage.dismiss()
        }

        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("MY_LANG", "")

        /* when{
             language == "ar" -> r1.isChecked = true
             language == "" -> r2.isChecked = true
             language == "hi" -> r3.isChecked = true
             language == "es" -> r4.isChecked = true
             language == "de" -> r5.isChecked = true
             language == "ja" -> r6.isChecked = true
             language == "pt" -> r7.isChecked = true
             language == "it" -> r8.isChecked = true
             language == "fr" -> r9.isChecked = true
             language == "ru" -> r10.isChecked = true
             language == "zh" -> r11.isChecked = true
         }*/

        if (language == "ar"){
            r1.isChecked = true
            language_arabic.setImageResource(R.drawable.arabic_language_selected)
        } else if (language == ""){
            r2.isChecked = true
            language_english.setImageResource(R.drawable.english_language_selected)
        } else if (language == "hi"){
            r3.isChecked = true
            language_hindi.setImageResource(R.drawable.hindi_language_selected)
        } else if (language == "es"){
            r4.isChecked = true
            language_spanish.setImageResource(R.drawable.spanish_language_selected)
        } else if (language == "de"){
            r5.isChecked = true
            language_german.setImageResource(R.drawable.german_language_selected)
        } else if (language == "ja"){
            r6.isChecked = true
            language_japanese.setImageResource(R.drawable.japanese_language_selected)
        } else if (language == "pt"){
            r7.isChecked = true
            language_portuguese.setImageResource(R.drawable.portuguese_language_selected)
        } else if (language == "it"){
            r8.isChecked = true
            language_italian.setImageResource(R.drawable.italian_language_selected)
        } else if (language == "fr"){
            r9.isChecked = true
            language_french.setImageResource(R.drawable.french_language_selected)
        } else if (language == "ru"){
            r10.isChecked = true
            language_russian.setImageResource(R.drawable.russian_language_selected)
        } else if (language == "zh"){
            r11.isChecked = true
            language_chinese.setImageResource(R.drawable.chinese_language_selected)
        }

        r1.setOnClickListener {
            setLocale("ar")
            dialogLanguage.dismiss()
            recreate()
        }
        r2.setOnClickListener {
            setLocale("")
            dialogLanguage.dismiss()
            recreate()
        }
        r3.setOnClickListener {
            setLocale("hi")
            dialogLanguage.dismiss()
            recreate()
        }
        r4.setOnClickListener {
            setLocale("es")
            dialogLanguage.dismiss()
            recreate()
        }
        r5.setOnClickListener {
            setLocale("de")
            dialogLanguage.dismiss()
            recreate()
        }
        r6.setOnClickListener {
            setLocale("ja")
            dialogLanguage.dismiss()
            recreate()
        }
        r7.setOnClickListener {
            setLocale("pt")
            dialogLanguage.dismiss()
            recreate()
        }
        r8.setOnClickListener {
            setLocale("it")
            dialogLanguage.dismiss()
            recreate()
        }
        r9.setOnClickListener {
            setLocale("fr")
            dialogLanguage.dismiss()
            recreate()
        }
        r10.setOnClickListener {
            setLocale("ru")
            dialogLanguage.dismiss()
            recreate()
        }
        r11.setOnClickListener {
            setLocale("zh")
            dialogLanguage.dismiss()
            recreate()
        }
    }

    private fun setLocale(language: String) {
        val locale  = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

        val editor : SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("MY_LANG", language)
        editor.apply()
    }
    private fun loadLocale(){
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("MY_LANG", "")
        setLocale(language!!)
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
                    binding.cardVerifyOtp.visibility = View.VISIBLE
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

    override fun onStart() {
        super.onStart()
//        registerBroadCastReceiver()
        loadLocale()
    }
    override fun onStop() {
        super.onStop()
//        unregisterReceiver(otpBroadcastReciever)
    }

    override fun onRestart() {
        super.onRestart()
        loadLocale()
    }

    override fun onResume() {
        super.onResume()
        loadLocale()
    }
    override fun onPause() {
        super.onPause()
        loadLocale()
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
