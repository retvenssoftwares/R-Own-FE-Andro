package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserInterestBinding
import app.retvens.rown.utils.moveTo

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
            moveTo(this,"MoveToUC")
            val intent = Intent(applicationContext, UserContacts::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("name",username)
            startActivity(intent)
        }
    }
}