package app.retvens.rown.Dashboard.profileCompletion

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserNameBinding
import app.retvens.rown.Dashboard.profileCompletion.frags.UsernameFragment
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



        val fragment = UsernameFragment()
            if (fragment !=null){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_username,fragment)
                transaction.commit()
            }
    }



    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_username)
        if (currentFragment is BackHandler) {
            (currentFragment as BackHandler).handleBackPressed()
        } else {
            super.onBackPressed()
        }
    }

}

interface BackHandler {
    fun handleBackPressed() : Boolean
}

