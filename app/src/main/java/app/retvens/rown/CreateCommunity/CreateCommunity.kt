package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import app.retvens.rown.R

class CreateCommunity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)

        val nextBtn = findViewById<ImageView>(R.id.next)

        nextBtn.setOnClickListener {
            startActivity(Intent(this,SelectMembers::class.java))
        }

    }
}