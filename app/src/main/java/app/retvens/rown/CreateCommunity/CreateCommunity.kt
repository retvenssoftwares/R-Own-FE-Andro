package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R

class CreateCommunity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)

        findViewById<ImageButton>(R.id.createCommunity_backBtn).setOnClickListener {onBackPressed()}

        val nextBtn = findViewById<ImageView>(R.id.next)
        val communityName = findViewById<EditText>(R.id.community_name)
        val communityDescrip = findViewById<EditText>(R.id.community_Description)


        nextBtn.setOnClickListener {

            val name = communityName.text.toString().trim()
            val description = communityDescrip.text.toString().trim()
            if (name.length < 3){
                Toast.makeText(this, "Please enter community name", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CreateCummVisibilitySetting::class.java)
                intent.putExtra("name", name)
                intent.putExtra("desc", description)
                startActivity(intent)
            }
        }

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn)

        backbtn.setOnClickListener {
            startActivity(Intent(this,DashBoardActivity::class.java))
        }

    }
}