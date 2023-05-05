package app.retvens.rown.Dashboard.profileCompletion

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.frags.*
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserNameBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserName : AppCompatActivity() {

    lateinit var binding : ActivityUserNameBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingPrefs = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val editor = onboardingPrefs.edit()

        if (!onboardingPrefs.getBoolean("UsernameFragment", false)) {
            editor.putBoolean("UsernameFragment", false)
        }
        if (!onboardingPrefs.getBoolean("LocationFragment", false)) {
            editor.putBoolean("LocationFragment", false)
        }
        if (!onboardingPrefs.getBoolean("BasicInformationFragment", false)) {
            editor.putBoolean("BasicInformationFragment", false)
        }
        if (!onboardingPrefs.getBoolean("JobTitleFragment", false)) {
            editor.putBoolean("JobTitleFragment", false)
        }
        if (!onboardingPrefs.getBoolean("VendorsFragment", false)) {
            editor.putBoolean("VendorsFragment", false)
        }
        if (!onboardingPrefs.getBoolean("HospitalityExpertFragment", false)) {
            editor.putBoolean("HospitalityExpertFragment", false)
        }
        if (!onboardingPrefs.getBoolean("HotelOwnerChainFragment", false)) {
            editor.putBoolean("HotelOwnerChainFragment", false)
        }
        if (!onboardingPrefs.getBoolean("HotelOwnerFragment", false)) {
            editor.putBoolean("HotelOwnerFragment", false)
        }

        editor.apply()

        val isUsernameCompleted = onboardingPrefs.getBoolean("UsernameFragment", false)
        val isLocationCompleted = onboardingPrefs.getBoolean("LocationFragment", false)
        val isBasicInfoCompleted = onboardingPrefs.getBoolean("BasicInformationFragment", false)
        val isJobTitleCompleted = onboardingPrefs.getBoolean("JobTitleFragment", false)
        val isVendorsCompleted = onboardingPrefs.getBoolean("VendorsFragment", false)
        val isHospitalityExpertCompleted = onboardingPrefs.getBoolean("HospitalityExpertFragment", false)
        val isHotelOwnerChainCompleted = onboardingPrefs.getBoolean("HotelOwnerChainFragment", false)
        val isHotelOwnerCompleted = onboardingPrefs.getBoolean("HotelOwnerFragment", false)

        if (!isUsernameCompleted) {
            val fragment = UsernameFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (!isLocationCompleted) {
            val fragment = LocationFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (!isBasicInfoCompleted) {
            val fragment = BasicInformationFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        }else{
            val sharedPreferences = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
            val nextFrag = sharedPreferences?.getInt("nextFrag", 0)
            when (nextFrag) {
                0 -> {
                    if (!isJobTitleCompleted){
                        val fragment = JobTitleFragment()
                        val transaction = supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragment_username,fragment)
                        transaction?.commit()
                    }else{
                        Toast.makeText(applicationContext,"Profile Completed",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DashBoardActivity::class.java)
                        startActivity(intent)
                    }

                }
                1 -> {
                    if (!isHotelOwnerCompleted){
                        val fragment = HotelOwnerFragment()
                        val transaction = supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragment_username,fragment)
                        transaction?.commit()
                    }else{
                        if (!isHotelOwnerChainCompleted){
                            val fragment = HotelOwnerChainFragment()
                            val transaction = supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()
                        }else{
                            Toast.makeText(applicationContext,"Profile Completed",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, DashBoardActivity::class.java)
                            startActivity(intent)
                        }

                    }

                }
                2 -> {
                    if(!isHospitalityExpertCompleted){
                        val fragment = app.retvens.rown.Dashboard.profileCompletion.frags.HospitalityExpertFragment()
                        val transaction =supportFragmentManager?.beginTransaction()
                        transaction?.replace(app.retvens.rown.R.id.fragment_username,fragment)
                        transaction?.commit()
                    }else{
                        Toast.makeText(applicationContext,"Profile Completed",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DashBoardActivity::class.java)
                        startActivity(intent)
                    }
                }
                3 -> {
                    if (!isVendorsCompleted){
                        val fragment = VendorsFragment()
                        val transaction = supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragment_username,fragment)
                        transaction?.commit()
                    }else{
                        Toast.makeText(applicationContext,"Profile Completed",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DashBoardActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        }



    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_username)
        if (currentFragment is BackHandler) {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

}

interface BackHandler {
    fun handleBackPressed() : Boolean
}

