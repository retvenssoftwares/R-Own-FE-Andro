package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.R

class CreateCommunity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)

        val nextBtn = findViewById<ImageView>(R.id.next)
        val communityName = findViewById<EditText>(R.id.community_name)
        val communityDescrip = findViewById<EditText>(R.id.community_Description)


        nextBtn.setOnClickListener {

            val name = communityName.text.toString().trim()
            val description = communityDescrip.text.toString().trim()

            val intent = Intent(this,CreateCummVisibilitySetting::class.java)
            intent.putExtra("name",name)
            intent.putExtra("desc",description)
            startActivity(intent)
        }

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn)

        backbtn.setOnClickListener {
            startActivity(Intent(this,DashBoardActivity::class.java))
        }

    }
}