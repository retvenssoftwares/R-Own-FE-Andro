package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.OwnerProfileDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.home.MainAdapter
import app.retvens.rown.NavigationFragments.profile.EditHotelOwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.EditHotelProfileActivity
import app.retvens.rown.NavigationFragments.profile.EditVendorsProfileActivity
import app.retvens.rown.NavigationFragments.profile.HotelOwnerDetailsActivity
import app.retvens.rown.NavigationFragments.profile.UserDetailsActivity
import app.retvens.rown.NavigationFragments.profile.setting.discoverPeople.DiscoverPeopleActivity
import app.retvens.rown.NavigationFragments.profile.events.EventsProfileFragment
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsFragmentProfile
import app.retvens.rown.NavigationFragments.profile.jobs.JobsOnProfileFragment
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.setting.profileSetting.OwnerSettingActivity
import app.retvens.rown.NavigationFragments.profile.setting.saved.SavedActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.NavigationFragments.profile.vendorsReview.ReviewsActivity
import app.retvens.rown.NavigationFragments.profile.viewConnections.ViewConnectionsActivity
import app.retvens.rown.NavigationFragments.profile.viewRequests.ViewRequestsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
import app.retvens.rown.bottomsheet.BottomSheetHotelierProfileSetting
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import app.retvens.rown.bottomsheet.BottomSheetVendorsProfileSetting
import app.retvens.rown.utils.serverCode
import app.retvens.rown.utils.showFullImage
import app.retvens.rown.viewAll.vendorsDetails.VendorDetailsActivity
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI


class ProfileFragmentForHotelOwner() : Fragment(), BottomSheetHotelierProfileSetting.OnBottomSheetHotelierProfileSettingClickListener {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var verification : ImageView
    lateinit var name : TextView
    lateinit var linkText : TextView
    var completion = ""
    lateinit var polls : TextView
    lateinit var jobs : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var hotels : TextView
    lateinit var events : TextView
    lateinit var username : TextView
    lateinit var bio : TextView
    lateinit var postCount:TextView
    lateinit var connCont:TextView
    lateinit var requestCont:TextView
    lateinit var link:TextView

    private lateinit var progressDialog: Dialog

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var layout : ConstraintLayout

    lateinit var viewPP: CardView
    var seeStatus = ""
    var profilePic = ""
    var verificationStatus = ""

    var selected = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_for_hotel_owner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile = view.findViewById(R.id.profile)
        verification = view.findViewById(R.id.verification)
        username = view.findViewById(R.id.profile_username)
        bio = view.findViewById(R.id.bio)
        name = view.findViewById(R.id.profile_name)
        link = view.findViewById(R.id.link)
        linkText = view.findViewById(R.id.linkText)

        polls = view.findViewById(R.id.polls)
        jobs = view.findViewById(R.id.jobs)
        media = view.findViewById(R.id.media)
        status = view.findViewById(R.id.status)
        hotels = view.findViewById(R.id.hotels)
        events = view.findViewById(R.id.events)

        postCount = view.findViewById(R.id.posts_count)
        connCont = view.findViewById(R.id.connections_count)
        requestCont = view.findViewById(R.id.requests_count)
        viewPP = view.findViewById(R.id.viewPP)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_container)
        layout = view.findViewById(R.id.layout)

        if (isAdded) {
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
//            progressDialog.show()
        }
        profile.setOnLongClickListener {
            showFullImage(profilePic, requireContext())
            true
        }


        val sharedPreferencesID = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesID?.getString("user_id", "").toString()

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        viewPP.setOnClickListener {
            if (completion == "100"){
                startActivity(Intent(requireContext(),HotelOwnerDetailsActivity::class.java))
            }else{
                Toast.makeText(requireContext(),"Complete Your Profile First!!", Toast.LENGTH_SHORT).show()
            }
        }
//RownKey-9feb
        linkText.setOnClickListener{
            val uri = Uri.parse("https://" + linkText.text.toString())
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            if (selected == 1) {
                getOwnerProfile(user_id,user_id)
                val childFragment: Fragment = MediaFragment(user_id, true, username.text.toString(),seeStatus)
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            } else if (selected == 2){
                val childFragment: Fragment = PollsFragment(user_id, true, "")
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            } else if (selected == 3){
                val childFragment: Fragment = StatusFragment(user_id, true, "", "Connected")
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            } else if (selected == 4){
                val childFragment: Fragment = HotelsFragmentProfile(user_id, true, "",completion)
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
            }
            refresh.isRefreshing = false
        }

        getOwnerProfile(user_id,user_id)

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "")

        if (profilePic!!.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(profile)
        } else{
            profile.setImageResource(R.drawable.svg_user)
        }
        name.text = profileName



        val childFragment: Fragment = MediaFragment(user_id, true, username.text.toString(),seeStatus)
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()

        view.findViewById<TextView>(R.id.requests_count).setOnClickListener {
            startActivity(Intent(context, ViewRequestsActivity::class.java))
        }

        view.findViewById<TextView>(R.id.connections_count).setOnClickListener {
            startActivity(Intent(context, ViewConnectionsActivity::class.java))
        }

        jobs.setOnClickListener {
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            val childFragment: Fragment = JobsOnProfileFragment(user_id)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            selected = 1

            val childFragment: Fragment = MediaFragment(user_id, true, username.text.toString(),seeStatus)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        polls.setOnClickListener {
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            selected = 2
            val childFragment: Fragment = PollsFragment(user_id, true, "")
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            selected = 3
            val childFragment: Fragment = StatusFragment(user_id, true, "", "Connected")
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        hotels.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))

            selected = 4
            val childFragment: Fragment = HotelsFragmentProfile(user_id, true, "",completion)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        events.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            hotels.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            jobs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            events.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

            val childFragment: Fragment = EventsProfileFragment(user_id, true)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }

        setting = view.findViewById(R.id.profile_setting)
        setting.setOnClickListener {
            val bottomSheet = BottomSheetHotelierProfileSetting()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetHotelierProfileSetting.Hotelier_TAG)}
            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }
        fetchProfile(user_id)
    }

    private fun getOwnerProfile(userId: String, userId1: String) {


        val getProfile = RetrofitBuilder.connectionApi.getconnOwnerProfile(userId,userId)

        getProfile.enqueue(object : Callback<OwnerProfileDataClass?> {
            override fun onResponse(
                call: Call<OwnerProfileDataClass?>,
                response: Response<OwnerProfileDataClass?>
            ) {
                serverCode = response.code()
//                progressDialog.dismiss()

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                layout.visibility = View.VISIBLE

                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    seeStatus = response.connectionStatus
                    profilePic = response.profiledata.Profile_pic
                    if (profilePic.isNotEmpty()) {
                        Glide.with(requireContext()).load(profilePic).into(profile)
                    } else {
                        profile.setImageResource(R.drawable.svg_user)
                    }


                    verificationStatus = response.profile.verificationStatus
                    if (verificationStatus != "false"){
                        verification.visibility = View.VISIBLE
                    }

                    connCont.text = response.connection_Count.toString()
                    requestCont.text = response.requests_count.toString()
                    postCount.text = response.post_count.toString()
                    username.text = response.profiledata.User_name.toString()
//                    bio.text = response.profiledata.userBio.toString()
                    val getBio = response.profiledata.userBio
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

                    if (response.profile.hotelOwnerInfo.websiteLink.isNotEmpty()) {
                        linkText.text = response.profile.hotelOwnerInfo.websiteLink
                    } else {
                        link.visibility = View.GONE
                    }
                }

            }

            override fun onFailure(call: Call<OwnerProfileDataClass?>, t: Throwable) {
                progressDialog.dismiss()
            }
        })



    }

    private fun fetchProfile(userId: String) {

        val getData = RetrofitBuilder.retrofitBuilder.fetchUser(userId)

        getData.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    completion = response.body()!!.profileCompletionStatus
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {

            }
        })

    }


    override fun bottomSheetHotelierProfileSettingClick(bottomSheetProfileSettingFrBo: String) {
        when (bottomSheetProfileSettingFrBo) {

            "settings" -> {
                startActivity(Intent(context, OwnerSettingActivity::class.java))
            }
            "profileDetails" -> {
                if (completion == "100"){
                    startActivity(Intent(requireContext(),HotelOwnerDetailsActivity::class.java))
                }else{
                    Toast.makeText(requireContext(),"Complete Your Profile First!!", Toast.LENGTH_SHORT).show()
                }

            }
            "edit" -> {
                if (completion == "100"){
                    startActivity(Intent(context, EditHotelOwnerProfileActivity::class.java))
                    activity?.finish()
                }else{
                    Toast.makeText(requireContext(),"Complete Your Profile First!!", Toast.LENGTH_SHORT).show()
                }
            }
            "editHotelier" -> {
                if (completion == "100"){
                    startActivity(Intent(context, EditHotelProfileActivity::class.java))
                    activity?.finish()
                }else{
                    Toast.makeText(requireContext(),"Complete Your Profile First!!", Toast.LENGTH_SHORT).show()
                }
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