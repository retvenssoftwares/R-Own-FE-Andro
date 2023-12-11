package app.retvens.rown

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.UpdateAppActivity
import app.retvens.rown.DataCollections.onboarding.VersionUpdate
import app.retvens.rown.authentication.*
import app.retvens.rown.utils.getProfileInfo
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appVersion : TextView
    private lateinit var updateYourApp : TextView
    private lateinit var auth:FirebaseAuth

    var versionName = ""
    var updateAvailable = "" 

    var updateLink = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appVersion = findViewById(R.id.appVersion)
        updateYourApp = findViewById(R.id.updateYourApp)


        var server = 0
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        versionName = info.versionName.toString()
        appVersion.text = versionName
        appVersion.visibility = View.GONE
        getAppVersion(){
            updateAvailable = "updateAvailable"
            updateLink = it
        }

        getProfileInfo(this){
            server = it
        }
        auth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val sharedPreferences = getSharedPreferences("Move", MODE_PRIVATE)
            val move = sharedPreferences.getString("MoveTo", "")

            if (updateAvailable == "updateAvailable"){
                val intent = Intent(this, UpdateAppActivity::class.java)
                intent.putExtra("updateLink", updateLink)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (move == "MoveToPI"){
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
                intent.putExtra("server", "$server")
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

    private fun getAppVersion(onVersionChange : (String) -> Unit) {
        val version = RetrofitBuilder.retrofitBuilder.getAppUpdate()
        version.enqueue(object : Callback<VersionUpdate?> {
            override fun onResponse(
                call: Call<VersionUpdate?>,
                response: Response<VersionUpdate?>
            ) {
                if (response.isSuccessful) {
                    Log.d("appUpdate", response.body()!!.get(0).Android_version.toString())
//                    appVersion.text = response.body()!!.get(0).Android_version.toString()
                    if (versionName != response.body()!!.get(0).Android_version.toString()){
//                        updateYourApp.visibility = View.VISIBLE
                        onVersionChange.invoke(response.body()!!.get(0).updateLink)
                    }
                }
            }
            override fun onFailure(call: Call<VersionUpdate?>, t: Throwable) {

            }
        })
    }
}