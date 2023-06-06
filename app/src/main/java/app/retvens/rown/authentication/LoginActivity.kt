package app.retvens.rown.authentication

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.onboarding.SearchUser
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityLoginBinding
import app.retvens.rown.utils.moveTo
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    val REQUEST_CODE = 102
    private var phoneNum: String = ""
    lateinit var dialog: Dialog
    lateinit var progressDialog: Dialog
    var mLastClickTime: Long = 0

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var phone:String

    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, set the phone number to the EditText
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
                    // Get the TelephonyManager instance
                    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                    // Retrieve the phone number
                    val phoneNumber = telephonyManager.line1Number

                    // Set the phone number to the EditText
                    phoneNum = phoneNumber.drop(2)
                    binding.editPhone.setText(phoneNum)
                }else {
                    // Permission has been denied, handle it accordingly
                    // For example, show a message or disable functionality that requires the permission
                    Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).toString()
                }
            }
        } else {
            // Permission has been denied, handle it accordingly
            // For example, show a message or disable functionality that requires the permission
            Toast.makeText(this,"Something bad", Toast.LENGTH_SHORT).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.languageFromLogin.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
             openBottomLanguageSheet()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
            // Get the TelephonyManager instance
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Retrieve the phone number
            val phoneNumber = telephonyManager.line1Number

            // Set the phone number to the EditText
            if (phoneNumber.length > 10){
                phoneNum = phoneNumber.drop(2)
                binding.editPhone.setText(phoneNum)
            }else{
                binding.editPhone.setText(phoneNum)
            }
        } else {
            // Permission has not been granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_NUMBERS), REQUEST_CODE)
        }

        FirebaseApp.initializeApp(this)

        auth=FirebaseAuth.getInstance()

        binding.privacyPolicy.setOnClickListener{
            val uri : Uri = Uri.parse("https://www.retvensservices.com/privacy-policy")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        //googleLogin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.imgGoogle.setOnClickListener {

            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
            resultLauncher.launch(Intent(googleSignInClient.signInIntent))
        }

        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, DashBoardActivity::class.java))
            finish()
        }else{

        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                finish()
                progressDialog.dismiss()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG).show()
                Log.e("error",e.message.toString())
                progressDialog.dismiss()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                dialog.dismiss()


                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                phoneNum = binding.editPhone.text.toString()
//                Toast.makeText(applicationContext,"Otp will be send to Enter Mobile Number",Toast.LENGTH_SHORT).show()
                var intent = Intent(applicationContext,OtpVerification::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("phone",phone)
                intent.putExtra("phoneNum",phoneNum)
                startActivity(intent)
                progressDialog.dismiss()
            }
        }

        binding.cardContinue.setOnClickListener {
            if (binding.editPhone.length() < 10){
                Toast.makeText(this,"Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }else{
//                getProfil()
                val countryCode = binding.countryCode.selectedCountryCode
                val phoneNo = binding.editPhone.text.toString()
                phone = "+$countryCode$phoneNo"
//                phoneNumber = "+$countryCode$phoneNo"
                showBottomDialog(phone)
            }
        }
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

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignIn", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignIn", "Google sign in failed", e)
                    progressDialog.dismiss()
                }
            }else{
                progressDialog.dismiss()
                Log.w("SignIn", exception.toString())
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
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
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
//            Toast.makeText(this,"Sending..", Toast.LENGTH_SHORT).show()
            sendVerificationcode(phone)
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithCredential:success")
//                    Toast.makeText(applicationContext,"YOU ARE SUCCESSFULLY LOGIN",Toast.LENGTH_SHORT).show()
                    moveTo(this,"MoveToPIP")
                    progressDialog.dismiss()
                    val intent = Intent(this, PersonalInformationPhone::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    progressDialog.dismiss()
                    Log.d("SignIn", "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext,"signInWithGoogle : Failure",Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onStart() {
        super.onStart()
        loadLocale()
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
}