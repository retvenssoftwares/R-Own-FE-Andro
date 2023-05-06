package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateUserName
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.utils.saveProgress
import app.retvens.rown.utils.saveUserId
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mesibo.calls.api.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UsernameFragment : Fragment() {

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
    private lateinit var editor:Editor

    lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        view.findViewById<ImageView>(R.id.dob_cal_img).setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        /*-------------Calendar Setup--------------*/


        //Define Fields
        firstName = view.findViewById(R.id.first_name)
        firstNameLayout = view.findViewById(R.id.first_name_layout)
        lastName = view.findViewById(R.id.last_name)
        lastNameLayout = view.findViewById(R.id.last_name_layout)
        userName = view.findViewById(R.id.complete_username)
        userNameLayout = view.findViewById(R.id.complete_username_layout)

        val complete = view.findViewById<CardView>(R.id.card_complete_continue)

        complete.setOnClickListener {
            if(firstName.length() < 3){
                firstNameLayout.error = "Please enter your first name"
            } else if(lastName.length() < 3){
                firstNameLayout.isErrorEnabled = false
                lastNameLayout.error = "Please enter your last name"
            } else if(dobEt.length() < 3){
                lastNameLayout.isErrorEnabled = false
                dobEtLayout.error = "Please select your D.O.B"
            } else if(userName.length() < 3){
                dobEtLayout.isErrorEnabled = false
                userNameLayout.error = "Please enter an username"
            } else {
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

        getProfileInfo()

    }

    private fun getProfileInfo() {
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val send = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        send.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful && isAdded){

                    val response = response.body()!!
                    name.setText("Hi ${response.Full_name}")

                    Glide.with(requireContext()).load(response.Profile_pic).into(profile)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
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

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val update = UpdateUserName(fullName,date,username)
        val send = RetrofitBuilder.profileCompletion.setUsername(user_id,update)

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){

                    val onboardingPrefs = requireContext().getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

                    editor = onboardingPrefs.edit()

                    editor.putBoolean("UsernameFragment", true)
                    Log.e("Onboarding", "UsernameFragment set to true")
                    editor.apply()

//                    DashBoardActivity.number.progress = "50"
                    saveProgress(requireContext(), "60")

                    val prefValue = onboardingPrefs.getBoolean("UsernameFragment", false)
                    Log.e("PrefValue", "UsernameFragment preference value: $prefValue")

                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    val fragment = LocationFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })







    }
}
