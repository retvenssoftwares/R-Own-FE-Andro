package app.retvens.rown.authentication

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetLanguage
import app.retvens.rown.databinding.ActivityLoginBinding
import app.retvens.rown.utils.loadLocale
import app.retvens.rown.utils.moveTo
import app.retvens.rown.utils.setLocale
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() , BottomSheetLanguage.OnBottomSheetLanguagelickListener{
    lateinit var binding: ActivityLoginBinding


    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
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
        loadLocale(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()


        binding.languageFromLogin.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            val bottomSheet = BottomSheetLanguage()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLanguage.CTC_TAG)}
            bottomSheet.setOnLangClickListener(this)
//             openBottomLanguageSheet()
        }

        binding.editPhone.addTextChangedListener {
            if (binding.editPhone.text.length == 10){
                binding.cardContinue.isClickable = true
                binding.continueText.setBackgroundColor(ContextCompat.getColor(this, R.color.green_own))
            } else {
                binding.cardContinue.isClickable = false
                binding.continueText.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_60))
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Get the TelephonyManager instance
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Retrieve the phone number
            val phoneNumber = telephonyManager.line1Number

            // Set the phone number to the EditText
            if (phoneNumber.length == 14){
                phoneNum = phoneNumber.drop(4)
                binding.editPhone.setText(phoneNum)
            } else if (phoneNumber.length == 13){
                phoneNum = phoneNumber.drop(3)
                binding.editPhone.setText(phoneNum)
            } else if (phoneNumber.length == 12){
                phoneNum = phoneNumber.drop(2)
                binding.editPhone.setText(phoneNum)
            } else if (phoneNumber.length == 11){
                phoneNum = phoneNumber.drop(1)
                binding.editPhone.setText(phoneNum)
            }else{
                binding.editPhone.setText(phoneNum)
            }
        } else {
            // Permission has not been granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE)
        }

        FirebaseApp.initializeApp(this)

        auth=FirebaseAuth.getInstance()

        binding.privacyPolicy.setOnClickListener{
            val uri : Uri = Uri.parse("https://www.retvensservices.com/privacy-policy")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        binding.tnc.setOnClickListener{
            val uri : Uri = Uri.parse("https://www.r-own.com/terms-and-conditions")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        binding.contentPolicies.setOnClickListener{
            val uri : Uri = Uri.parse("https://www.r-own.com/terms-and-conditions")
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



    private fun requestNotificationPermission() {
        if (!isNotificationPermissionGranted()) {
            Log.e("check","1")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For Android Oreo and above, open the app's notification settings
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivity(intent)
            } else {
                Log.e("check","2")
                // For older Android versions, open the app's application settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("check","3")
            // For Android Oreo and above, check if the notification channel is enabled
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.getNotificationChannel("YOUR_CHANNEL_ID")?.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            Log.e("check","4")
            // For older Android versions, check if the app has notification permission
            NotificationManagerCompat.from(this).areNotificationsEnabled()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (isNotificationPermissionGranted()) {
                Log.e("Notification", "Accepted")
            } else {
                Log.e("Notification", "Denied")
            }
        }

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
        loadLocale(applicationContext)
    }

    override fun onRestart() {
        super.onRestart()
        loadLocale(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        loadLocale(applicationContext)
    }
    override fun onPause() {
        super.onPause()
        loadLocale(applicationContext)
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