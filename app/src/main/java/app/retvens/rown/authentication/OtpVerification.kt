package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import androidx.appcompat.app.AppCompatActivity
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
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.onboarding.DeviceTokenClass
import app.retvens.rown.DataCollections.onboarding.SearchUser
import app.retvens.rown.MesiboApi
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
import app.retvens.rown.bottomsheet.BottomSheetLanguage
import app.retvens.rown.databinding.ActivityOtpVerifificationBinding
import app.retvens.rown.utils.loadLocale
import app.retvens.rown.utils.moveTo
import app.retvens.rown.utils.savePhoneNo
import app.retvens.rown.utils.saveUserId
import app.retvens.rown.utils.setLocale
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.mesibo.api.Mesibo
import com.mesibo.calls.api.MesiboCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

open class OtpVerification : AppCompatActivity(), BottomSheetLanguage.OnBottomSheetLanguagelickListener {
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
        loadLocale(this)
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
        binding.textPhoneOtp.text = phone
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

        generateDeviceToken(phone)


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
            val bottomSheet = BottomSheetLanguage()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLanguage.CTC_TAG)}
            bottomSheet.setOnLangClickListener(this)
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

    private fun generateDeviceToken(phone: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(BottomSheet.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast

            Log.e("token",token.toString())
            sendDeviceToken(phone,token)
        })


    }

    private fun sendDeviceToken(phone: String, token: String?) {

        val sendToken = RetrofitBuilder.retrofitBuilder.deviceToken(DeviceTokenClass(phone,token.toString()))

        sendToken.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()


//                    Toast.makeText(
//                        applicationContext,
//                        "Otp Verified Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()
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
//                    Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                    Log.d("SaveM",response.toString())
                    SharedPreferenceManagerAdmin.getInstance(applicationContext).saveUser(response)

                    MesiboApi.init(applicationContext)

                    MesiboApi.startMesibo(true)


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
                Log.e("help",t.message.toString())
            }
        })


    }
    private fun searchUser() {
//        val p = "+91" +phoneNumber.toString()
        val search = RetrofitBuilder.retrofitBuilder.searchUser(SearchUser(phone))
        search.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful) {


                val user_id = response.body()?.user_id.toString()
                val message = response.body()?.message.toString()
                saveUserId(applicationContext, user_id)
                savePhoneNo(applicationContext, phone)
//                Toast.makeText(applicationContext,response.body().toString(),Toast.LENGTH_SHORT).show()
                Log.e("search", response.body().toString())
                moveTo(applicationContext, "MoveToPI")
                val intent = Intent(applicationContext, PersonalInformation::class.java)
                intent.putExtra("phone", phone)
                intent.putExtra("user_id", user_id)
                intent.putExtra("message", message)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }else{
                        Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                Log.d("search",t.localizedMessage,t)
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

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    override fun bottomLangClick(language: String) {
        when (language) {
            "ar" -> {
                setLocale("ar", applicationContext)
                recreate()
            }
            "" -> {
                setLocale("", applicationContext)
                recreate()
            }
            "hi" -> {
                setLocale("hi",applicationContext)
                recreate()
            }
            "es" -> {
                setLocale("es",applicationContext)
                recreate()
            }
            "de" -> {
                setLocale("de",applicationContext)
                recreate()
            }
            "ja" -> {
                setLocale("ja",applicationContext)
                recreate()
            }
            "pt" -> {
                setLocale("pt",applicationContext)
                recreate()
            }
            "it" -> {
                setLocale("it",applicationContext)
                recreate()
            }
            "fr" -> {
                setLocale("fr",applicationContext)
                recreate()
            }
            "ru" -> {
                setLocale("ru",applicationContext)
                recreate()
            }
            "zh" -> {
                setLocale("zh",applicationContext)
                recreate()
            }
        }
    }
}
