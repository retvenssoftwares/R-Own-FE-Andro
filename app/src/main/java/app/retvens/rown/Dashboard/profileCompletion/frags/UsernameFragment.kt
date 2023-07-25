package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateUserName
import app.retvens.rown.R
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import app.retvens.rown.utils.saveUserName
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UsernameFragment : Fragment(), BackHandler {

    lateinit var dobEt : TextInputEditText
    private lateinit var firstName:TextInputEditText
    private lateinit var lastName:TextInputEditText
    private lateinit var userName:TextInputEditText
    lateinit var dobEtLayout : TextInputLayout
    private lateinit var firstNameLayout:TextInputLayout
    private lateinit var lastNameLayout:TextInputLayout
    private lateinit var userNameLayout:TextInputLayout
    private lateinit var sdf:SimpleDateFormat
    private lateinit var cal:Calendar
    private lateinit var profile: ShapeableImageView
    private lateinit var name:TextView
    private lateinit var complete:CardView
    private lateinit var usernameVerified:TextView
    private lateinit var editor:Editor

    lateinit var progressDialog : Dialog

    var user_id : String ?= ""

    var isUsernameVerified = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferencesU = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferencesU?.getString("user_id", "").toString()

        /*-------------Calendar Setup--------------*/
        dobEt = view.findViewById(R.id.dob_et)
        dobEtLayout = view.findViewById(R.id.dob)

        cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            sdf = SimpleDateFormat(myFormat, Locale.US)
            dobEt.setText(sdf.format(cal.time))


        }
        view.findViewById<TextInputEditText>(R.id.dob_et).setOnClickListener {
           val dpd = DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

            dpd.datePicker.maxDate = System.currentTimeMillis()
               dpd.show()

        }
        /*-------------Calendar Setup--------------*/


        //Define Fields
        firstName = view.findViewById(R.id.first_name)
        firstNameLayout = view.findViewById(R.id.first_name_layout)
        lastName = view.findViewById(R.id.last_name)
        lastNameLayout = view.findViewById(R.id.last_name_layout)
        userName = view.findViewById(R.id.complete_username)
        userNameLayout = view.findViewById(R.id.complete_username_layout)

        usernameVerified = view.findViewById(R.id.usernameVerified)

        userNameLayout.setEndIconOnClickListener {
            if(userName.length() < 4) {
                Toast.makeText(context, "Please enter a valid username", Toast.LENGTH_SHORT).show()
            } else if (!isUsernameVerified) {
                verifyUserName()
            }
        }

        userName.doAfterTextChanged {
            if (userName.text.toString() == " "){
                userName.setText("")
            }
            isUsernameVerified = false
            userNameLayout.setEndIconDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.svg_verify))
            usernameVerified.text = ""
            complete.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_40))
            complete.isClickable = false
        }

        complete = view.findViewById<CardView>(R.id.card_complete_continue)
        complete.isClickable = false
        complete.setOnClickListener {
            if(firstName.length() < 3){
                firstNameLayout.error = "Please enter your first name"
            } else if(lastName.length() < 3){
                firstNameLayout.isErrorEnabled = false
                lastNameLayout.error = "Please enter your last name"
            } else if(dobEt.text.toString() == "XX/XX/XXXX"){
                lastNameLayout.isErrorEnabled = false
                firstNameLayout.isErrorEnabled = false
                dobEtLayout.error = "Please select your D.O.B"
            } else if(userName.length() < 3){
                lastNameLayout.isErrorEnabled = false
                firstNameLayout.isErrorEnabled = false
                dobEtLayout.isErrorEnabled = false
                usernameVerified.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                usernameVerified.text ="Please enter a valid username"
            }
            else if(!isUsernameVerified){
                lastNameLayout.isErrorEnabled = false
                firstNameLayout.isErrorEnabled = false
                dobEtLayout.isErrorEnabled = false
                usernameVerified.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                usernameVerified.text = "Please verify your username"
            }
            else {
                lastNameLayout.isErrorEnabled = false
                firstNameLayout.isErrorEnabled = false
                dobEtLayout.isErrorEnabled = false
                userNameLayout.isErrorEnabled = false
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                sendInfo()
            }
        }

        profile = view.findViewById(R.id.user_complete_profile)
        name = view.findViewById(R.id.user_complete_name)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(profile)
        } else {
            profile.setImageResource(R.drawable.svg_user)
        }

        name.setText("Hi $profileName")

        val nameParts = profileName.split(" ")
        val firstN = nameParts.getOrElse(0) { "" }
        var lastN = ""
        try {
            lastN = nameParts.getOrElse(1) { "" }
        } catch (e: IndexOutOfBoundsException) {
            Log.e("error", e.message.toString())
        } catch (e: NullPointerException) {
            Log.e("error", e.message.toString())
        }

        firstName.setText(firstN)
        lastName.setText(lastN)

    }
    private fun verifyUserName() {

        val username = userName.text.toString()
        val sharedPreferencesU = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferencesU?.getString("user_id", "").toString()
        val verify = RetrofitBuilder.profileCompletion.verifyUsername(user_id!!,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),username))

        verify.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    if (response.body()?.message == "user_name already exist"){
                        isUsernameVerified = false
                        usernameVerified.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                        usernameVerified.text = "Username already exist, Please enter another username"
                    } else {
                        isUsernameVerified = true
                        userNameLayout.setEndIconDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.png_check))
                        usernameVerified.text = "Congratulations! $username username is available"
                        usernameVerified.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_own))
                        complete.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_own))
                        complete.isClickable = true
                    }
                } else {
                    try {
                        usernameVerified.text = "Retry - ${response.body()!!.message}"
                    }catch(e : Exception){
                        usernameVerified.text = "Try another Username"
                    }
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
               Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun sendInfo() {

        val first = firstName.text.toString()
        val last = lastName.text.toString()
        val username = userName.text.toString()

        val fullName = "$first $last"

        val date = sdf.format(cal.time).toString()

        val update = UpdateUserName(fullName,date,username)
        val send = RetrofitBuilder.profileCompletion.setUsername(user_id!!,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),fullName),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),date),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),username))

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    saveUserName(requireContext(), username)
                    profileComStatus(context!!, "60")
                    profileCompletionStatus = "60"
                    val fragment = BioGenderFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        t.localizedMessage?.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressDialog.dismiss()
            }
        })
    }
    override fun handleBackPressed(): Boolean {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_going_back)

        val yesStatement = dialog.findViewById<TextView>(R.id.syjh)
        yesStatement.text = "Keep editing and complete your profile"

        val noStatement = dialog.findViewById<TextView>(R.id.syh)
        noStatement.text = "You will not be availble to edit these changes again"

        val goBack = dialog.findViewById<TextView>(R.id.sjt)
        goBack.text = "Go Back"

        dialog.findViewById<ConstraintLayout>(R.id.keep_edit).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<ConstraintLayout>(R.id.discard).setOnClickListener {
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
            activity?.finish()
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        return true
    }

}
