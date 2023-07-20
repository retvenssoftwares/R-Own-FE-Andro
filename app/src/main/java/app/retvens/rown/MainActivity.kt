package app.retvens.rown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.authentication.*
import app.retvens.rown.utils.connectionCount
import app.retvens.rown.utils.getProfileInfo
import app.retvens.rown.utils.phone
import app.retvens.rown.utils.profileCompletionStatus
import app.retvens.rown.utils.role
import app.retvens.rown.utils.saveConnectionNo
import app.retvens.rown.utils.saveFullName
import app.retvens.rown.utils.saveProfileImage
import app.retvens.rown.utils.saveUserName
import app.retvens.rown.utils.verificationStatus
import app.retvens.rown.utils.websiteLinkV
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getProfileInfo(this){}

        auth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val sharedPreferences = getSharedPreferences("Move", MODE_PRIVATE)
            val move = sharedPreferences.getString("MoveTo", "")

            if (move == "MoveToPI"){
                MesiboApi.init(applicationContext)
                MesiboApi.startMesibo(true)
                val intent = Intent(this, PersonalInformation::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (move == "MoveToPIP"){
                MesiboApi.init(applicationContext)
                MesiboApi.startMesibo(true)
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
                MesiboApi.init(applicationContext)
                MesiboApi.startMesibo(true)
                val intent = Intent(this, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else{
                MesiboApi.init(applicationContext)
                MesiboApi.startMesibo(true)
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