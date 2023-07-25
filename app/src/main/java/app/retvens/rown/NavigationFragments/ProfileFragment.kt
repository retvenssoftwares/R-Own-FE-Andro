package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.NavigationFragments.profile.EditProfileActivity
import app.retvens.rown.NavigationFragments.profile.UserDetailsActivity
import app.retvens.rown.NavigationFragments.profile.setting.discoverPeople.DiscoverPeopleActivity
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.setting.profileSetting.OwnerSettingActivity
import app.retvens.rown.NavigationFragments.profile.setting.saved.SavedActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.NavigationFragments.profile.viewConnections.ViewConnectionsActivity
import app.retvens.rown.NavigationFragments.profile.viewRequests.ViewRequestsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import app.retvens.rown.utils.serverCode
import app.retvens.rown.utils.showFullImage
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(), BottomSheetProfileSetting.OnBottomSheetProfileSettingClickListener {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var verification : ImageView
    lateinit var name : TextView
    lateinit var bio : TextView
    lateinit var userName:TextView

    lateinit var polls : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var postCount:TextView
    lateinit var connCont:TextView
    lateinit var requestCont:TextView
    var seeStatus = ""
    var profilePic = ""
    var verificationStatus = ""

    var selected = 1

    private lateinit var progressDialog:Dialog

    lateinit var viewPP: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile = view.findViewById(R.id.profile)
        verification = view.findViewById(R.id.verification)
        name = view.findViewById(R.id.profile_name)
        userName = view.findViewById(R.id.profile_username)
        bio = view.findViewById(R.id.bio)

        polls = view.findViewById(R.id.polls)
        media = view.findViewById(R.id.media)
        status = view.findViewById(R.id.status)
        postCount = view.findViewById(R.id.posts_count)
        connCont = view.findViewById(R.id.connections_count)
        requestCont = view.findViewById(R.id.requests_count)

        viewPP = view.findViewById(R.id.viewPP)

        if (isAdded) {
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
        }
        profile.setOnLongClickListener {
            showFullImage(profilePic, requireContext())
            true
        }


        val sharedPreferencesId = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesId?.getString("user_id", "").toString()
        viewPP.setOnClickListener {
            startActivity(Intent(context,UserDetailsActivity::class.java))
        }

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            if (selected == 1) {
                getSelfUserProfile(user_id,user_id)
                val childFragment: Fragment = MediaFragment(user_id, true, userName.text.toString(),seeStatus)
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            } else if (selected == 2){
                val childFragment: Fragment = PollsFragment(user_id, true, "")
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            } else if (selected == 3){
                val childFragment: Fragment = StatusFragment(user_id, true, "")
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            }
            refresh.isRefreshing = false
        }

        getSelfUserProfile(user_id,user_id)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(profile)
        }
        name.text = profileName

        val childFragment: Fragment = MediaFragment(user_id, true, userName.text.toString(),seeStatus)
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()

        view.findViewById<TextView>(R.id.requests_count).setOnClickListener {
            startActivity(Intent(context, ViewRequestsActivity::class.java))
        }

        view.findViewById<TextView>(R.id.connections_count).setOnClickListener {
            startActivity(Intent(context, ViewConnectionsActivity::class.java))
        }

        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            selected = 2
            val childFragment: Fragment = PollsFragment(user_id, true, "")
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))


            selected = 1
            val childFragment: Fragment = MediaFragment(user_id, true, userName.text.toString(),seeStatus)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

            selected = 3
            val childFragment: Fragment = StatusFragment(user_id, true, "")
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }

        setting = view.findViewById(R.id.profile_setting)
        setting.setOnClickListener {
            val bottomSheet = BottomSheetProfileSetting()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetProfileSetting.WTP_TAG)}
            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }
    }

    private fun getSelfUserProfile(userId: String, userId1: String) {

        val getProfile = RetrofitBuilder.connectionApi.getconnProfile(userId,userId1)

        getProfile.enqueue(object : Callback<NormalUserDataClass?> {
            override fun onResponse(
                call: Call<NormalUserDataClass?>,
                response: Response<NormalUserDataClass?>
            ) {
                serverCode = response.code()
                progressDialog.dismiss()
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!

                    profilePic = response.data.profile.Profile_pic
                    seeStatus = response.data.connectionStatus
                    if (profilePic.isNotEmpty()) {
                        Glide.with(requireContext()).load(profilePic).into(profile)
                    } else{
                        profile.setImageResource(R.drawable.svg_user)
                    }

                    verificationStatus = response.data.profile.verificationStatus
                    if (verificationStatus != "false"){
                        verification.visibility = View.VISIBLE
                    }

                    postCount.text = response.data.postCountLength.toString()
                    connCont.text = response.data.connCountLength.toString()
                    requestCont.text = response.data.reqsCountLength.toString()
                    bio.text = response.data.profile.userBio
                    if (response.data.profile.User_name == ""){
                        userName.text = "Complete Your Profile"
                    } else {
                        userName.text = response.data.profile.User_name
                    }
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<NormalUserDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun bottomSheetProfileSettingClick(bottomSheetProfileSettingFrBo: String) {
        when (bottomSheetProfileSettingFrBo) {
            "profile" -> {

            }
            "settings" -> {
                startActivity(Intent(context, OwnerSettingActivity::class.java))
            }
            "profileDetails" -> {
                startActivity(Intent(context,UserDetailsActivity::class.java))
            }
            "edit" -> {
                startActivity(Intent(context, EditProfileActivity::class.java))
            }
            "saved" -> {
                startActivity(Intent(context, SavedActivity::class.java))
            }
            "discover" -> {
                startActivity(Intent(context, DiscoverPeopleActivity::class.java))
            }
        }
    }
}