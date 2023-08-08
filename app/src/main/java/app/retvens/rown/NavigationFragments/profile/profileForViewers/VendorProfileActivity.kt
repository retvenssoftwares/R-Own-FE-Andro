package app.retvens.rown.NavigationFragments.profile.profileForViewers

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.BlockAccount
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
import app.retvens.rown.bottomsheet.BottomSheetRemoveConnection
import app.retvens.rown.bottomsheet.BottomSheetSharePost
import app.retvens.rown.utils.acceptRequest
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import app.retvens.rown.utils.showFullImage
import app.retvens.rown.viewAll.vendorsDetails.VendorDetailsActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mesibo.api.Mesibo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class VendorProfileActivity : AppCompatActivity(), BottomSheetRemoveConnection.OnBottomSheetRemoveConnectionClickListener  {

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
    lateinit var reject:TextView
    lateinit var rejectCard: CardView
    lateinit var card_message: CardView
    private lateinit var progressDialog:Dialog

    lateinit var viewPP: CardView
    var created = ""
    var location = ""
    var verification = ""
    var profilePic = ""
    var address =  ""
    var fullName = ""
    var userName =  ""
    var role = ""
    var userId = ""
    var user_id = ""
    var seeStatus = ""
    var selected = 1

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

        reject = findViewById(R.id.reject)
        rejectCard = findViewById(R.id.openReview)

        postCount = findViewById(R.id.posts_count)
        connCount = findViewById(R.id.connections_count)

        connStatus = findViewById(R.id.connStatus)
        card_message = findViewById(R.id.card_message)

        polls = findViewById(R.id.polls)
        media = findViewById(R.id.media)
        status = findViewById(R.id.status)
        services = findViewById(R.id.services)

        viewPP = findViewById(R.id.viewPP)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        val refresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)

        userId = intent.getStringExtra("userId").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences?.getString("user_id", "").toString()

        refresh.setOnRefreshListener {
            if (selected == 1) {
                getUserPofile(userId, user_id)
            } else if (selected == 2){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, PollsFragment(userId, false, name.text.toString()))
                transaction.commit()
            } else if (selected == 3){

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, StatusFragment(userId, false, name.text.toString(), seeStatus))
                transaction.commit()
            } else if (selected == 4){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, ServicesFragment(userId, false, name.text.toString(),role))
                transaction.commit()
            }
            refresh.isRefreshing = false
        }

        viewPP.setOnClickListener {
            val intent = Intent(applicationContext, VendorDetailsActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        getUserPofile(userId,user_id)

        if (user_id == userId){
            connStatus.visibility = View.GONE
            card_message.visibility = View.GONE
        }

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
                val bottomSheet = BottomSheetRemoveConnection()
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetRemoveConnection.Remove_TAG)}
                bottomSheet.setOnBottomSheetRemoveConnectionClickListener(this)
            } else if (connStatus.text == "CONNECT") {

                sendConnectionRequest(userId, applicationContext){
                    connStatus.text = "Requested"
                }

            } else  if (connStatus.text == "Requested") {

                removeConnRequest(userId, applicationContext){
                    connStatus.text = "CONNECT"
                }

            } else if (connStatus.text == "Accept") {

                acceptRequest(userId, applicationContext){
                    connStatus.text = "Remove"

                    rejectCard.visibility = View.GONE
                }
            }
        }

        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 2
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, PollsFragment(userId, false, name.text.toString()))
            transaction.commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 1

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, MediaFragment(userId, false, name.text.toString(),seeStatus))
            transaction.commit()
        }
        services.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 4
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, ServicesFragment(userId, false, name.text.toString(),role))
            transaction.commit()
        }

        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            services.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 3

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, StatusFragment(userId, false, name.text.toString(), seeStatus))
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
                val share = encodeData("Profile",userId,verification, fullName ,userName,"Business Vendor / Freelancerb",profilePic)
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

            dialogLanguage.findViewById<LinearLayout>(R.id.block).setOnClickListener {
                blockUser(userId)
                dialogLanguage.dismiss()
            }

//            val bottomSheet = BottomSheetProfileSetting()
//            val fragManager = supportFragmentManager
//            fragManager.let{bottomSheet.show(it, BottomSheetProfileSetting.WTP_TAG)}
//            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }

    }

    private fun blockUser(userId: String) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val User_id = sharedPreferences?.getString("user_id", "").toString()

        val block = RetrofitBuilder.ProfileApis.blockAccount(User_id, BlockAccount(userId))

        block.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message.toString(),Toast.LENGTH_SHORT).show()
                    val profile = Mesibo.getProfile(address)
                    profile.block(true)
                    profile.save()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun getUserPofile(userID: String, userId: String) {

        val getProfile = RetrofitBuilder.connectionApi.getVendorProfile(userID,userId)

        getProfile.enqueue(object : Callback<VendorProfileDataClass?> {
            override fun onResponse(
                call: Call<VendorProfileDataClass?>,
                response: Response<VendorProfileDataClass?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("response",response.toString())
                    try{
                        address = response.roleDetails.Mesibo_account.get(0).address

                        profilePic = response.roleDetails.Profile_pic

                        if (profilePic.isNotEmpty()){
                            Glide.with(applicationContext).load(response.roleDetails.Profile_pic)
                                .into(profile)
                        } else {
                            profile.setImageResource(R.drawable.svg_user)
                        }

                        name.text = response.roleDetails.Full_name
                        if (response.roleDetails.User_name.isNotEmpty()) {
                            profile_username.text = response.roleDetails.User_name
                        } else {
                            profile_username.text = response.roleDetails.Full_name
                        }
//                        bio.text = response
                        val getBio = response.roleDetails.vendorInfo.vendorDescription
                        try {
                            if (getBio != null) {
                                if (getBio.length > 50) {
                                    bio.text = Html.fromHtml(getBio.substring(0, 50) + "..." + "<font color='black'> <b>Read More</b></font>")
                                } else {
                                    bio.text = getBio
                                }
                            } else {
                                bio.text = "" // or any default text you prefer when caption is null
                            }

                            bio.setOnClickListener {
                                if (bio.text.toString().endsWith("Read More") && getBio != null) {
                                    bio.text = getBio
                                } else {
                                    if (getBio != null && getBio.length > 50) {
                                        bio.text = Html.fromHtml(getBio.substring(0, 50) + "..." + "<font color='black'> <b>Read More</b></font>")
                                    } else {
                                        bio.text = getBio
                                    }
                                }
                            }
                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }

                        websiteLink.text = response.roleDetails.vendorInfo.websiteLink
                        fullName = response.roleDetails.Full_name
                        userName = response.roleDetails.User_name

                        media.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
                        polls.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
                        status.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
                        services.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
                        seeStatus = response.connectionStatus
                        selected = 1

                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, name.text.toString(),seeStatus))
                        transaction.commit()

                        connCount.text = response.connectioncount.toString()
                        postCount.text = response.postcount.toString()
                        role = response.roleDetails.profileCompletionStatus
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

                        }else if (response.connectionStatus ==  "Confirm request"){
                            connStatus.text = "Accept"

                            rejectCard.visibility = View.VISIBLE
                            reject.text = "REJECT"
                        } else{

                            connStatus.text = response.connectionStatus
                        }
                    } catch (e : NullPointerException){
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("new", e.toString())
                    }
                } else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<VendorProfileDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
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

    override fun bottomSheetRemoveConnectionClick(removeConnection: String) {
        if (removeConnection == "Yes") {
            removeConnection(userId,user_id, applicationContext){
                connStatus.text = "CONNECT"
                card_message.visibility = View.GONE
                viewPP.visibility = View.GONE

                rejectCard.visibility = View.VISIBLE
                reject.text = "VIEW PROFESSIONAL PROFILE"
            }
        }
    }

}
