package app.retvens.rown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            if (auth.currentUser != null){
                startActivity(Intent(this,DashBoardActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }

        },2000)
    }
}