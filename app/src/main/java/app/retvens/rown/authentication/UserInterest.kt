package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserInterestBinding

class UserInterest : AppCompatActivity() {
    lateinit var binding: ActivityUserInterestBinding

    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("user").toString()
        binding.userName.text = "Hello, $username!"

        binding.cardContinueInterest.setOnClickListener {
            startActivity(Intent(this,UserContacts::class.java))
        }
    }
}