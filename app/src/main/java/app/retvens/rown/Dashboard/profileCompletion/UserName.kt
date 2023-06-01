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
import app.retvens.rown.utils.profileCompletionStatus
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

       if (profileCompletionStatus == "60") {
            val fragment = LocationFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (profileCompletionStatus == "70") {
            val fragment = BasicInformationFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (profileCompletionStatus == "75") {
            val fragment = HotelOwnerFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (profileCompletionStatus == "80") {
            val fragment = JobTitleFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (profileCompletionStatus == "85") {
            val fragment = HospitalityExpertFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        } else if (profileCompletionStatus == "90") {
            val fragment = VendorsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_username, fragment)
            transaction.commit()
        }else{

           val fragment = UsernameFragment()
           val transaction = supportFragmentManager.beginTransaction()
           transaction.replace(R.id.fragment_username, fragment)
           transaction.commit()

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

