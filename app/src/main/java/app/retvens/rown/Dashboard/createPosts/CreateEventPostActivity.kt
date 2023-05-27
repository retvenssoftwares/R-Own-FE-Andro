package app.retvens.rown.Dashboard.createPosts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.*
import app.retvens.rown.databinding.ActivityCreateEventPostBinding
import com.bumptech.glide.Glide

class CreateEventPostActivity : AppCompatActivity(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener,
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener,
    BottomSheetUpcomingEvent.OnBottomJobTitleClickListener {
    lateinit var binding: ActivityCreateEventPostBinding
    var canSee : Int ?= 0

    lateinit var title:String
    lateinit var id:String
    lateinit var eventdate:String
    lateinit var eventimage:String
    lateinit var eventlocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(applicationContext).load(profilePic).into(binding.userCompleteProfile)
        binding.userCompleteName.setText(profileName)


        binding.canSee.setOnClickListener {
            canSee = 1
            val bottomSheet = BottomSheetSelectAudience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetSelectAudience.S_A_TAG)}
            bottomSheet.setOnSelectAudienceClickListener(this)
        }
        binding.canComment.setOnClickListener {
            canSee = 2
            val bottomSheet = BottomSheetSelectAudience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetSelectAudience.S_A_TAG)}
            bottomSheet.setOnSelectAudienceClickListener(this)
        }

        postUpdateEvent()
    }
    private fun postUpdateEvent() {
        binding.etLocationPostEvent.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.etEventVenuePost.setOnClickListener {

            val location = binding.etLocationPostEvent.text.toString()

            if (location.isEmpty()){
                binding.etLocationPostEvent.error = "select location first!!"
            }else{
                val bottomSheet = BottomSheetUpcomingEvent(location)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetUpcomingEvent.Job_Title_TAG)}
                bottomSheet.setOnJobTitleClickListener(this)

            }

        }
        binding.nextUpdateEvent.setOnClickListener {

            val venue = binding.etEventVenuePost.text.toString()

            if (venue.isEmpty()){
                binding.etEventVenuePost.error = "select event first!!"
            }else{
                val intent = Intent(this,CreatePostEventActivityChild::class.java)
                intent.putExtra("name",title)
                intent.putExtra("id",id)
                intent.putExtra("date",eventdate)
                intent.putExtra("image",eventimage)
                intent.putExtra("location",eventlocation)
                startActivity(intent)
            }

        }
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocationPostEvent.setText(CountryStateCityFrBo)
    }

    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            binding.canSeeText.text = audienceFrBo
        }else{
            binding.canCommentText.text = audienceFrBo
        }
    }

    override fun bottomJobTitleClick(name: String, image: String, eventId: String, date: String,location:String) {
        binding.etEventVenuePost.setText(name)
        title = name
        eventimage = image
        id = eventId
        eventdate = date
        eventlocation = location
    }


}