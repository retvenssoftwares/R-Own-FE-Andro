package app.retvens.rown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.authentication.LoginActivity
import app.retvens.rown.authentication.PersonalInformationPhone
import app.retvens.rown.authentication.UserContacts
import app.retvens.rown.authentication.UserInterest
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val sharedPreferences = getSharedPreferences("Move", MODE_PRIVATE)
            val move = sharedPreferences.getString("MoveTo", "")

            if (move == "MoveToPI"){
                val intent = Intent(this, PersonalInformationPhone::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (move == "MoveToI"){
                val intent = Intent(this, UserInterest::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (move == "MoveToUC"){
                val intent = Intent(this, UserContacts::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (move == "MoveToD"){
                val intent = Intent(this, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

//            if (auth.currentUser != null){
//                startActivity(Intent(this,DashBoardActivity::class.java))
//                finish()
//            }


        },2000)
    }
}