package app.retvens.rown.NavigationFragments.profile.profileForViewers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.BlockAccount
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.UserDetailsActivity
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.settingForViewers.AboutProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ReportProfileActivity
import app.retvens.rown.NavigationFragments.profile.settingForViewers.ShareQRActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import app.retvens.rown.bottomsheet.BottomSheetRemoveConnection
import app.retvens.rown.bottomsheet.BottomSheetSharePost
import app.retvens.rown.utils.acceptRequest
import app.retvens.rown.utils.rejectConnRequest
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import app.retvens.rown.utils.showFullImage
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.mesibo.api.Mesibo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList
import java.util.Queue

class UserProfileActivity : AppCompatActivity(), BottomSheetRemoveConnection.OnBottomSheetRemoveConnectionClickListener {

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
    lateinit var reject:TextView
    lateinit var viewPP: CardView
    lateinit var rejectCard: CardView
    lateinit var card_message: CardView
    val apiRequestQueue: Queue<() -> Unit> = LinkedList()
    var isApiCallInProgress = false

    var created = ""
    var location = ""
    var verification = ""
    var nameProfile = ""
    var profilePic = ""
    var connStat =  ""
    var address =  ""
    var username = ""
    var userrole = ""
    var seeStatus = ""
    var userID = ""
    var user_id = ""

    var selected = 1

    private lateinit var progressDialog:Dialog

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var layout : ConstraintLayout

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
        viewPP = findViewById(R.id.viewPP)
        reject = findViewById(R.id.reject)
        rejectCard = findViewById(R.id.openReview)

        polls = findViewById(R.id.polls)
        media = findViewById(R.id.media)
        status = findViewById(R.id.status)

        postCount = findViewById(R.id.posts_count)
        connCount = findViewById(R.id.connections_count)
        connStatus = findViewById(R.id.connStatus)
        card_message = findViewById(R.id.card_message)

        val refresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)

        shimmerFrameLayout = findViewById(R.id.shimmer_container)
        layout = findViewById(R.id.layout)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
//        progressDialog.show()

        userID = intent.getStringExtra("userId").toString()
//        connStat = intent.getStringExtra("status").toString()
//        val address = intent.getStringExtra("address").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences?.getString("user_id", "").toString()

        if (user_id == userID){
            connStatus.visibility = View.GONE
            card_message.visibility = View.GONE
        }

        refresh.setOnRefreshListener {
            if (selected == 1) {
                getUserPofile(userID, user_id)
            } else if (selected == 2){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container,PollsFragment(userID, false, nameProfile))
                transaction.commit()
            } else if (selected == 3){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container,StatusFragment(userID, false, nameProfile, seeStatus))
                transaction.commit()
            }
            refresh.isRefreshing = false
        }

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
                val bottomSheet = BottomSheetRemoveConnection()
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetRemoveConnection.Remove_TAG)}
                bottomSheet.setOnBottomSheetRemoveConnectionClickListener(this)
            } else if (connStatus.text == "CONNECT") {
                connStatus.text = "Requested"
                viewPP.visibility = View.GONE

                rejectCard.visibility = View.VISIBLE
                reject.text = "VIEW PROFESSIONAL PROFILE"
                sendConnectionRequest(userID, applicationContext){
                    isApiCallInProgress = false
                    // Process the next request in the queue
                    processApiRequests()
                }

            } else  if (connStatus.text == "Requested") {
                connStatus.text = "CONNECT"
                viewPP.visibility = View.GONE

                rejectCard.visibility = View.VISIBLE
                reject.text = "VIEW PROFESSIONAL PROFILE"
                removeConnRequest(userID, applicationContext){

                }

            } else if (connStatus.text == "Accept") {
                connStatus.text = "Remove"

                rejectCard.visibility = View.GONE
                card_message.visibility = View.VISIBLE
                viewPP.visibility = View.VISIBLE
                acceptRequest(userID, applicationContext){

                }

            }

            processApiRequests()
        }

        rejectCard.setOnClickListener {
            if (reject.text == "REJECT") {
                rejectCard.visibility = View.GONE
                connStatus.text = "CONNECT"
                rejectConnRequest(userID, applicationContext) {

                }
            } else {
                val intent = Intent(this, UserDetailsActivity::class.java)
                intent.putExtra("viewer", "viewer")
                intent.putExtra("userID", userID)
                startActivity(intent)
            }
        }

        viewPP.setOnClickListener {
                val intent = Intent(this, UserDetailsActivity::class.java)
                intent.putExtra("viewer", "viewer")
                intent.putExtra("userID", userID)
                startActivity(intent)
        }

        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 2
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,PollsFragment(userID, false, nameProfile))
            transaction.commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))

            selected = 1
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, nameProfile,seeStatus))
            transaction.commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            selected = 3
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container,StatusFragment(userID, false, nameProfile, seeStatus))
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

                val share = encodeData("Profile",userID,verification,nameProfile,username,"Normal User",profilePic)

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
                blockUser(userID)
                dialogLanguage.dismiss()
            }

//            val bottomSheet = BottomSheetProfileSetting()
//            val fragManager = supportFragmentManager
//            fragManager.let{bottomSheet.show(it, BottomSheetProfileSetting.WTP_TAG)}
//            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }

    }

    fun processApiRequests() {
        if (!isApiCallInProgress && apiRequestQueue.isNotEmpty()) {
            isApiCallInProgress = true
            val apiRequest = apiRequestQueue.poll()
            apiRequest?.invoke()
        }
    }



    private fun blockUser(userID: String) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val User_id = sharedPreferences?.getString("user_id", "").toString()

        val block = RetrofitBuilder.ProfileApis.blockAccount(User_id, BlockAccount(userID))

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

        val getProfile = RetrofitBuilder.connectionApi.getconnProfile(userID,userId)

        getProfile.enqueue(object : Callback<NormalUserDataClass?> {
            override fun onResponse(
                call: Call<NormalUserDataClass?>,
                response: Response<NormalUserDataClass?>
            ) {
//                progressDialog.dismiss()

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                layout.visibility = View.VISIBLE

                if (response.isSuccessful){
                    val response = response.body()!!
                    profilePic = response.data.profile.Profile_pic


                    if (profilePic.isNotEmpty()){
                        Glide.with(applicationContext).load(response.data.profile.Profile_pic).into(profile)
                    } else {
                        profile.setImageResource(R.drawable.svg_user)
                    }

                    if (response.data.profile.User_name.isNotEmpty()) {
                        profile_username.text = response.data.profile.User_name
                    } else{
                        profile_username.text = response.data.profile.Full_name
                    }
                    name.text = response.data.profile.Full_name
                    nameProfile = response.data.profile.Full_name.toString()
//                    bio.text = response.data.profile.userBio
                    val getBio = response.data.profile.userBio
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

                    username = response.data.profile.User_name

                    media.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
                    polls.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
                    status.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
                    seeStatus = response.data.connectionStatus
                    selected = 1
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.child_profile_fragments_container,MediaFragment(userID, false, nameProfile,seeStatus))
                    transaction.commit()

                    connCount.text = response.data.connCountLength.toString()
                    postCount.text = response.data.postCountLength.toString()

                    try {
                        address = response.data.profile.Mesibo_account.get(0).address
                    } catch (e:Exception){

                    }

                    created = response.data.profile.Created_On
                    location = response.data.profile.location
                    verification = response.data.profile.verificationStatus
                    if (verification != "false"){
                        verificationS.visibility = View.VISIBLE
                    }
//                    Toast.makeText(applicationContext,response.data.connectionStatus,Toast.LENGTH_SHORT).show()

                    if (response.data.connectionStatus == "Connected"){
                        connStatus.text = "Remove"
                        card_message.visibility = View.VISIBLE
                        viewPP.visibility = View.VISIBLE
                        connStat = "Connected"
                    }else if (response.data.connectionStatus == "Not connected"){
                        connStatus.text = "CONNECT"
                        connStat = "Not Connected"

                        rejectCard.visibility = View.VISIBLE
                        reject.text = "VIEW PROFESSIONAL PROFILE"
                    }else if (response.data.connectionStatus ==  "Confirm request"){
                        connStatus.text = "Accept"
                        connStat = "Confirm request"

                        viewPP.visibility = View.VISIBLE

                        rejectCard.visibility = View.VISIBLE
                        reject.text = "REJECT"
                    } else{
                        connStat = response.data.connectionStatus
                        connStatus.text = response.data.connectionStatus

                        rejectCard.visibility = View.VISIBLE
                        reject.text = "VIEW PROFESSIONAL PROFILE"
                    }
                }
            }

            override fun onFailure(call: Call<NormalUserDataClass?>, t: Throwable) {
                progressDialog.dismiss()
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
            removeConnection(userID,user_id, applicationContext){
                connStatus.text = "CONNECT"
                card_message.visibility = View.GONE
                viewPP.visibility = View.GONE

                rejectCard.visibility = View.VISIBLE
                reject.text = "VIEW PROFESSIONAL PROFILE"
            }
        }
    }
}