package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserDataClass
import app.retvens.rown.NavigationFragments.profile.EditProfileActivity
import app.retvens.rown.NavigationFragments.profile.setting.discoverPeople.DiscoverPeopleActivity
import app.retvens.rown.NavigationFragments.profile.media.MediaFragment
import app.retvens.rown.NavigationFragments.profile.polls.PollsFragment
import app.retvens.rown.NavigationFragments.profile.status.StatusFragment
import app.retvens.rown.NavigationFragments.profile.viewConnections.ViewConnectionsActivity
import app.retvens.rown.NavigationFragments.profile.viewRequests.ViewRequestsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(), BottomSheetProfileSetting.OnBottomSheetProfileSettingClickListener {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var name : TextView
    lateinit var userName:TextView

    lateinit var polls : TextView
    lateinit var media : TextView
    lateinit var status : TextView
    lateinit var postCount:TextView
    lateinit var connCont:TextView
    lateinit var requestCont:TextView


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
        name = view.findViewById(R.id.profile_name)

        polls = view.findViewById(R.id.polls)
        media = view.findViewById(R.id.media)
        status = view.findViewById(R.id.status)
        userName = view.findViewById(R.id.userName)
        postCount = view.findViewById(R.id.posts_count)
        connCont = view.findViewById(R.id.connections_count)
        requestCont = view.findViewById(R.id.requests_count)



        val sharedPreferencesId = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesId?.getString("user_id", "").toString()

        getSelfUserProfile(user_id,user_id)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(requireContext()).load(profilePic).into(profile)
        name.text = profileName

        val childFragment: Fragment = MediaFragment(user_id)
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

            val childFragment: Fragment = PollsFragment(user_id)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        media.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))


            val childFragment: Fragment = MediaFragment(user_id)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_profile_fragments_container, childFragment).commit()
        }
        status.setOnClickListener {
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
            status.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

            val childFragment: Fragment = StatusFragment(user_id)
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
                if (response.isSuccessful){
                    val response = response.body()!!

                    postCount.text = response.data.postCountLength.toString()
                    connCont.text = response.data.connCountLength.toString()
                    requestCont.text = response.data.reqsCountLength.toString()
                }else{
                    Toast.makeText(requireContext(),response.code(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NormalUserDataClass?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun bottomSheetProfileSettingClick(bottomSheetProfileSettingFrBo: String) {
        when (bottomSheetProfileSettingFrBo) {
            "profile" -> {

            }
            "settings" -> {

            }
            "edit" -> {
                startActivity(Intent(context, EditProfileActivity::class.java))
            }
            "saved" -> {

            }
            "discover" -> {
                startActivity(Intent(context, DiscoverPeopleActivity::class.java))
            }
        }
    }
}