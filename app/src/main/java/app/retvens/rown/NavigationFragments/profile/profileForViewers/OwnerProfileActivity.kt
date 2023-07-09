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
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.events.EventsProfileFragment
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsFragmentProfile
import app.retvens.rown.NavigationFragments.profile.jobs.JobsOnProfileFragment
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.settingForViewers.AboutProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ReportProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ShareQRActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetSharePost
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import app.retvens.rown.utils.showFullImage
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerProfileActivity : AppCompatActivity() {
    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var verificationS : ImageView
    lateinit var name : TextView
    lateinit var profile_username : TextView
    lateinit var websiteLink : TextView
    lateinit var bio : TextView

    lateinit var polls : TextView
    lateinit var jobs : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var hotels : TextView
    lateinit var events : TextView
    lateinit var postCount:TextView
    lateinit var connCount:TextView

    lateinit var connStatus:TextView
    lateinit var reject:TextView
    lateinit var rejectCard: CardView
    lateinit var card_message:CardView

    var created = ""
    var location = ""
    var nameProfile = ""
    var verification = ""
    var profilePic = ""
    var address =  ""
    var userName = ""
    var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_profile)

        profile = findViewById(R.id.profile)
        verificationS = findViewById(R.id.verification)
        profile_username = findViewById(R.id.profile_username)
        name = findViewById(R.id.profile_name)
        bio = findViewById(R.id.bio)
        websiteLink = findViewById(R.id.linkText)
        reject = findViewById(R.id.reject)
        rejectCard = findViewById(R.id.openReview)


        postCount = findViewById(R.id.posts_count)
        connCount = findViewById(R.id.connections_count)
        connStatus = findViewById(R.id.connStatus)
        card_message = findViewById(R.id.card_message)


        val userID = intent.getStringExtra("userId").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        if (user_id == userID){
            connStatus.visibility = View.GONE
            card_message.visibility = View.GONE
        }

        getUserPofile(userID,user_id)

        profile.setOnLongClickListener {
            showFullImage(profilePic, this)
            true
        }

        card_message.setOnClickListener{
            val intent = Intent(applicationContext, MesiboMessagingActivity::class.java)
            intent.putExtra(MesiboUI.PEER, address)
            startActivity(intent)
        }
        connStatus.setOnClickListener {
            if (connStatus.text == "Remove"){

                removeConnection(userID,user_id, applicationContext){
                    connStatus.text = "CONNECT"
                    card_message.visibility = View.GONE
                }

            } else if (connStatus.text == "CONNECT") {

                sendConnectionRequest(userID, applicationContext){
                    connStatus.text = "Requested"
                }

            } else  if (connStatus.text == "Requested") {

                removeConnRequest(userID, applicationContext){
                    connStatus.text = "CONNECT"
                }

            } else if (connStatus.text == "Accept Connection") {
                connStatus.text = "Remove"

                rejectCard.visibility = View.GONE
                card_message.visibility = View.VISIBLE
            }
        }

        websiteLink.setOnClickListener{
            val uri = Uri.parse("https://" + websiteLink.text.toString())
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener {
            onBackPressed()
        }

        polls = findViewById(R.id.polls)
        jobs = findViewById(R.id.jobs)
        media = findViewById(R.id.media)
        status = findViewById(R.id.status)
        hotels = findViewById(R.id.hotels)
        events = findViewById(R.id.events)

        jobs.setOnClickListener {
            jobs.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, JobsOnProfileFragment(userID))
            transaction.commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, MediaFragment(userID, false, nameProfile))
            transaction.commit()
        }
        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, PollsFragment(userID, false, nameProfile))
            transaction.commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, StatusFragment(userID, false, nameProfile))
            transaction.commit()
        }
        hotels.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            events.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,HotelsFragmentProfile(userID, false, nameProfile))
            transaction.commit()
        }
        events.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, EventsProfileFragment(userID, false))
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
                val share = encodeData("Profile",userID,verification,nameProfile,userName,"Hotel Owner",profilePic)
                val bottomSheet = BottomSheetSharePost(share)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetSharePost.Company_TAG)}
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

        val getProfile = RetrofitBuilder.connectionApi.getconnOwnerProfile(userID,userId)

        getProfile.enqueue(object : Callback<OwnerProfileDataClass?> {
            override fun onResponse(
                call: Call<OwnerProfileDataClass?>,
                response: Response<OwnerProfileDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    profilePic = response.profiledata.Profile_pic

                    if (profilePic.isNotEmpty()){
                    Glide.with(applicationContext).load(response.profiledata.Profile_pic).into(profile)
                    } else {
                        profile.setImageResource(R.drawable.svg_user)
                    }

                    profile_username.text = response.profiledata.User_name
                    name.text = response.profiledata.Full_name
                    nameProfile = response.profiledata.Full_name
                    bio.text = response.profiledata.userBio
                    websiteLink.text = response.profile.hotelOwnerInfo.websiteLink
                    userName = response.profiledata.User_name
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, nameProfile))
                    transaction.commit()

                    connCount.text = response.connection_Count.toString()
                    postCount.text = response.post_count.toString()
                    address = response.profiledata.Mesibo_account.get(0).address
                    created = response.profile.Created_On
                    location = response.profile.location
                    verification = response.profile.verificationStatus

                    if (verification != "false"){
                        verificationS.visibility = View.VISIBLE
                    }

                    if (response.connectionStatus == "Connected"){
                        connStatus.text = "Remove"
                        card_message.visibility = View.VISIBLE
                    }else if (response.connectionStatus == "Not connected"){
                        connStatus.text = "CONNECT"
                    }else if (response.connectionStatus ==  "Confirm request"){
                        connStatus.text = "Accept"
                        rejectCard.visibility = View.VISIBLE
                        reject.text = "REJECT"
                    } else{
                        connStatus.text = response.connectionStatus
                    }
                }
            }

            override fun onFailure(call: Call<OwnerProfileDataClass?>, t: Throwable) {

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

    fun encodeData(
        messageType: String,
        userID: String,
        verificationStatus: String,
        fullName: String,
        userName: String,
        userRole: String,
        profilePictureLink: String
    ): String {
        val encodedMessageType = encodeString(messageType, 3)
        val encodedUserID = encodeString(userID, 5)
        val encodedVerificationStatus = encodeString(verificationStatus, 2)
        val encodedFullName = encodeString(fullName, 4)
        val encodedUserName = encodeString(userName, 1)
        val encodedUserRole = encodeString(userRole, 6)
        val encodedProfilePictureLink = encodeString(profilePictureLink, 7)

        return "$encodedMessageType|$encodedUserID|$encodedVerificationStatus|$encodedFullName|$encodedUserName|$encodedUserRole|$encodedProfilePictureLink"
    }

    fun encodeString(input: String, shift: Int): String {
        val encodedData = StringBuilder()
        for (char in input) {
            if (char.isLetter()) {
                val base = if (char.isLowerCase()) 'a' else 'A'
                val encodedAscii = (char.toInt() - base.toInt() + shift) % 26
                val encodedChar = (encodedAscii + base.toInt()).toChar()
                encodedData.append(encodedChar)
            } else {
                encodedData.append(char)
            }
        }
        return encodedData.toString()
    }
}