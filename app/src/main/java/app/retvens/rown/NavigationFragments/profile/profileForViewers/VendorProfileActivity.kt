package app.retvens.rown.NavigationFragments.profile.profileForViewers

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.OwnerProfileDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.VendorProfileDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.services.ServicesFragment
import app.retvens.rown.NavigationFragments.profile.settingForViewers.AboutProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ReportProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ShareQRActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.R
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import app.retvens.rown.utils.showFullImage
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class VendorProfileActivity : AppCompatActivity() {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var verificationS : ImageView
    lateinit var name : TextView
    lateinit var bio : TextView
    lateinit var websiteLink : TextView
    lateinit var profile_username : TextView

    lateinit var polls : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var services : TextView
    lateinit var postCount:TextView
    lateinit var connCount:TextView

    lateinit var connStatus:TextView
    lateinit var card_message: CardView

    var created = ""
    var location = ""
    var verification = ""
    var profilePic = ""
    var address =  ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_profile)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener {
            onBackPressed()
        }

        profile = findViewById(R.id.profile)
        verificationS = findViewById(R.id.verification)
        profile_username = findViewById(R.id.profile_username)
        name = findViewById(R.id.profile_name)
        bio = findViewById(R.id.bio)
        websiteLink = findViewById(R.id.linkText)

        postCount = findViewById(R.id.posts_count)
        connCount = findViewById(R.id.connections_count)

        connStatus = findViewById(R.id.connStatus)
        card_message = findViewById(R.id.card_message)

        polls = findViewById(R.id.polls)
        media = findViewById(R.id.media)
        status = findViewById(R.id.status)
        services = findViewById(R.id.services)

        val userId = intent.getStringExtra("userId").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        getUserPofile(userId,user_id)

//        val connStat = intent.getStringExtra("status").toString()
//        val address = intent.getStringExtra("address").toString()

//        if (address.isNotEmpty() && connStat == "Connected"){
//            card_message.visibility = View.VISIBLE
//        }
        card_message.setOnClickListener{
            if (address.isNotEmpty()) {
                val intent = Intent(applicationContext, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, address)
                startActivity(intent)
            }
        }

        profile.setOnLongClickListener {
            showFullImage(profilePic, this)
            true
        }

        connStatus.setOnClickListener {
            if (connStatus.text == "Remove"){
                removeConnection(userId,user_id, applicationContext, connStatus)
            } else if (connStatus.text == "CONNECT") {
                sendConnectionRequest(userId, applicationContext, connStatus)
                connStatus.text = "Requested"
            } else  if (connStatus.text == "Requested") {
                removeConnRequest(userId, applicationContext, connStatus)
            } else if (connStatus.text == "Accept Connection") {
                accceptRequest(userId)
            }
        }

        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, PollsFragment(userId, false, name.text.toString()))
            transaction.commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, MediaFragment(userId, false, name.text.toString()))
            transaction.commit()
        }
        services.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, ServicesFragment(userId, false, name.text.toString()))
            transaction.commit()
        }

        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, StatusFragment(userId, false, name.text.toString()))
            transaction.commit()
        }

        websiteLink.setOnClickListener{
            val uri = Uri.parse("https://" + websiteLink.text.toString())
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
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
                intent.putExtra("user_id",userId)
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

        val getProfile = RetrofitBuilder.connectionApi.getVendorProfile(userID,userId)

        getProfile.enqueue(object : Callback<VendorProfileDataClass?> {
            override fun onResponse(
                call: Call<VendorProfileDataClass?>,
                response: Response<VendorProfileDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("response",response.toString())
                    try{
                        address = response.roleDetails.Mesibo_account.get(0).address

                        profilePic = response.roleDetails.Profile_pic
                        Glide.with(applicationContext).load(response.roleDetails.Profile_pic)
                            .into(profile)
                        name.text = response.roleDetails.Full_name
                        profile_username.text = response.roleDetails.User_name
                        bio.text = response.roleDetails.vendorInfo.vendorDescription
                        websiteLink.text = response.roleDetails.vendorInfo.websiteLink

                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userId, false, name.text.toString()))
                        transaction.commit()

                        connCount.text = response.connectioncount.toString()
                        postCount.text = response.postcount.toString()

                        created = response.roleDetails.Created_On
                        location = response.roleDetails.location
                        verification = response.roleDetails.verificationStatus

                        if (verification != "false"){
                            verificationS.visibility = View.VISIBLE
                        }

                        if (response.connectionStatus == "Connected"){
                            connStatus.text = "Remove"
                            card_message.visibility = View.VISIBLE
//                            connStat = "Connected"
                        }else if (response.connectionStatus == "Not connected"){
                            connStatus.text = "CONNECT"
//                            connStat = "Not Connected"
                        }else if (response.connectionStatus ==  "Confirm request"){
                            connStatus.text = "Accept Connection"
//                            connStat = "Confirm request"
                        } else{
//                            connStat = response.data.connectionStatus
                            connStatus.text = response.connectionStatus
                        }
                    } catch (e : NullPointerException){
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("new", e.toString())
                    }
                } else{
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VendorProfileDataClass?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
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