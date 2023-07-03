package app.retvens.rown.NavigationFragments.profile.profileForViewers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.settingForViewers.AboutProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ReportProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ShareQRActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import app.retvens.rown.utils.showFullImage
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var verificationS : ImageView
    lateinit var name : TextView
    lateinit var bio : TextView
    lateinit var profile_username : TextView

    lateinit var polls : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var postCount:TextView
    lateinit var connCount:TextView
    lateinit var connStatus:TextView
    lateinit var card_message: CardView

    var created = ""
    var location = ""
    var verification = ""
    var nameProfile = ""
    var profilePic = ""
    var connStat =  ""
    var address =  ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener {
            onBackPressed()
        }

        profile = findViewById(R.id.profile)

        verificationS = findViewById(R.id.verification)
        profile_username = findViewById(R.id.profile_username)
        name = findViewById(R.id.profile_name)
        bio = findViewById(R.id.bio)

        polls = findViewById(R.id.polls)
        media = findViewById(R.id.media)
        status = findViewById(R.id.status)

        postCount = findViewById(R.id.posts_count)
        connCount = findViewById(R.id.connections_count)
        connStatus = findViewById(R.id.connStatus)
        card_message = findViewById(R.id.card_message)


        val userID = intent.getStringExtra("userId").toString()
//        connStat = intent.getStringExtra("status").toString()
//        val address = intent.getStringExtra("address").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

//        if(connStat.isNotEmpty()){
////            Toast.makeText(applicationContext, connStat, Toast.LENGTH_SHORT).show()
//            if (connStat == "Not Connected"){
//                connStatus.text = "CONNECT"
//            } else if (connStat == "Connected"){
//                connStatus.text = "Interact"
//            } else {
//                connStatus.text = connStat
//            }
//        }

        getUserPofile(userID,user_id)

        card_message.setOnClickListener{
            val intent = Intent(applicationContext, MesiboMessagingActivity::class.java)
            intent.putExtra(MesiboUI.PEER, address)
            startActivity(intent)
        }


        profile.setOnLongClickListener {
            showFullImage(profilePic, this)
            true
        }

        connStatus.setOnClickListener {
            if (connStatus.text == "Remove"){
                removeConnection(userID,user_id, applicationContext, connStatus)
                card_message.visibility = View.GONE
            } else if (connStatus.text == "CONNECT") {
                sendConnectionRequest(userID, applicationContext, connStatus)
                connStatus.text = "Requested"
            } else  if (connStatus.text == "Requested") {
                removeConnRequest(userID, applicationContext, connStatus)
            } else if (connStatus.text == "Accept Connection") {
                accceptRequest(userID)
                if (address.isNotEmpty() && connStat == "Connected"){
                    card_message.visibility = View.VISIBLE
                }
            }
        }

        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,PollsFragment(userID, false, nameProfile))
            transaction.commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, nameProfile))
            transaction.commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,StatusFragment(userID, false, nameProfile))
            transaction.commit()
        }

        setting = findViewById(R.id.profile_setting)
        setting.setOnClickListener {

            val dialogLanguage = Dialog(this)
            dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogLanguage.setContentView(R.layout.bottom_profile_setting_for_viewers)
            dialogLanguage.setCancelable(true)

            dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogLanguage.window?.setGravity(Gravity.BOTTOM)
            dialogLanguage.show()

            dialogLanguage.findViewById<LinearLayout>(R.id.about).setOnClickListener {
                val intent = Intent(this, AboutProfileActivity::class.java)
                intent.putExtra("created", created)
                intent.putExtra("location", location)
                intent.putExtra("verification", verification)
                startActivity(intent)
                dialogLanguage.dismiss()
            }

            dialogLanguage.findViewById<LinearLayout>(R.id.report).setOnClickListener {
                val intent = Intent(this,ReportProfileActivity::class.java)
                intent.putExtra("user_id",userID)
                startActivity(intent)
                dialogLanguage.dismiss()
            }

            dialogLanguage.findViewById<LinearLayout>(R.id.shareQr).setOnClickListener {
                startActivity(Intent(this, ShareQRActivity::class.java))
                dialogLanguage.dismiss()
            }

            dialogLanguage.findViewById<LinearLayout>(R.id.shareProfileInMessage).setOnClickListener {

                dialogLanguage.dismiss()
            }

            dialogLanguage.findViewById<LinearLayout>(R.id.shareProfileVia).setOnClickListener {

                dialogLanguage.dismiss()
            }

            dialogLanguage.findViewById<LinearLayout>(R.id.copyUrl).setOnClickListener {

                dialogLanguage.dismiss()
            }

//            val bottomSheet = BottomSheetProfileSetting()
//            val fragManager = supportFragmentManager
//            fragManager.let{bottomSheet.show(it, BottomSheetProfileSetting.WTP_TAG)}
//            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }

    }

    private fun getUserPofile(userID: String, userId: String) {

        val getProfile = RetrofitBuilder.connectionApi.getconnProfile(userID,userId)

        getProfile.enqueue(object : Callback<NormalUserDataClass?> {
            override fun onResponse(
                call: Call<NormalUserDataClass?>,
                response: Response<NormalUserDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    profilePic = response.data.profile.Profile_pic
                    Glide.with(applicationContext).load(response.data.profile.Profile_pic).into(profile)
                    profile_username.text = response.data.profile.User_name
                    name.text = response.data.profile.Full_name
                    nameProfile = response.data.profile.Full_name.toString()
                    bio.text = response.data.profile.userBio

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, nameProfile))
                    transaction.commit()

                    connCount.text = response.data.connCountLength.toString()
                    postCount.text = response.data.postCountLength.toString()

                    address = response.data.profile.Mesibo_account.get(0).address

                    created = response.data.profile.Created_On
                    location = response.data.profile.location
                    verification = response.data.profile.verificationStatus
                    if (verification != "false"){
                        verificationS.visibility = View.VISIBLE
                    }
                    Toast.makeText(applicationContext,response.data.connectionStatus,Toast.LENGTH_SHORT).show()

                    if (response.data.connectionStatus == "Connected"){
                        connStatus.text = "Remove"
                        card_message.visibility = View.VISIBLE
                        connStat = "Connected"
                    }else if (response.data.connectionStatus == "Not connected"){
                        connStatus.text = "CONNECT"
                        connStat = "Not Connected"
                    }else if (response.data.connectionStatus ==  "Confirm request"){
                        connStatus.text = "Accept Connection"
                        connStat = "Confirm request"
                    } else{
                        connStat = response.data.connectionStatus
                        connStatus.text = response.data.connectionStatus
                    }
                }
            }

            override fun onFailure(call: Call<NormalUserDataClass?>, t: Throwable) {

            }
        })


    }
    private fun accceptRequest(userId: String) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val accept = RetrofitBuilder.connectionApi.sendRequest(user_id, ConnectionDataClass(userId))

        accept.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    connStatus.text = "Remove"
                    connStat = "Connected"
                    Toast.makeText(applicationContext,"Request Accepted",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,"Request Accepted",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

}