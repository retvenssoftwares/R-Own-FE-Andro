package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.Dashboard.createPosts.CreatCheackInPostActivity
import app.retvens.rown.Dashboard.createPosts.CreateEventPostActivity
import app.retvens.rown.Dashboard.createPosts.CreatePollActivity
import app.retvens.rown.Dashboard.createPosts.CreatePostActivity
import app.retvens.rown.NavigationFragments.profile.EditProfileActivity
import app.retvens.rown.NavigationFragments.profile.ViewRequestsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.bottomsheet.BottomSheetProfileSetting
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ProfileFragment : Fragment(), BottomSheetProfileSetting.OnBottomSheetProfileSettingClickListener {

    private lateinit var setting : ImageView
    lateinit var profile : ShapeableImageView
    lateinit var name : TextView

    lateinit var polls : TextView
    lateinit var media : TextView

    lateinit var layoutPoll : LinearLayout

    var isPoll = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPoll = view.findViewById(R.id.layout_poll)

        profile = view.findViewById(R.id.profile)
        name = view.findViewById(R.id.profile_name)

        polls = view.findViewById(R.id.polls)
        media = view.findViewById(R.id.media)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(requireContext()).load(profilePic).into(profile)
        name.text = profileName

        view.findViewById<TextView>(R.id.requests_count).setOnClickListener {
            startActivity(Intent(context, ViewRequestsActivity::class.java))
        }

        polls.setOnClickListener {
            layoutPoll.visibility = View.VISIBLE
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
        }
        media.setOnClickListener {
            layoutPoll.visibility = View.GONE
            media.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            polls.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_5))
        }

        setting = view.findViewById(R.id.profile_setting)
        setting.setOnClickListener {
            val bottomSheet = BottomSheetProfileSetting()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetProfileSetting.WTP_TAG)}
            bottomSheet.setOnBottomSheetProfileSettingClickListener(this)
        }
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

            }
        }
    }
}