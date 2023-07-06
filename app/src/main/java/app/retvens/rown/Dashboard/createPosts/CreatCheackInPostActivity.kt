package app.retvens.rown.Dashboard.createPosts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.*
import app.retvens.rown.databinding.ActivityCreatCheackInPostBinding
import com.bumptech.glide.Glide

class CreatCheackInPostActivity : AppCompatActivity(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener,
    BottomSheetSelectAudience.OnBottomSelectAudienceClickListener,
    BottomSheetHotelByLocation.OnBottomCountryStateCityClickListener {
    lateinit var binding: ActivityCreatCheackInPostBinding
    var canSee : Int ?= 0
    var location:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatCheackInPostBinding.inflate(layoutInflater)
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

        checkIn()



    }
    private fun checkIn() {
        binding.etLocationPost.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
        binding.etVenuePost.setOnClickListener {
            if (location != "") {
                val bottomSheet = BottomSheetHotelByLocation(location)
                val fragManager = supportFragmentManager
                fragManager.let {
                    bottomSheet.show(
                        it,
                        BottomSheetHotelByLocation.CountryStateCity_TAG
                    )
                }
                bottomSheet.setOnCountryStateCityClickListener(this)
            }else{
                Toast.makeText(applicationContext,"Select Location First",Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextCheckIn.setOnClickListener {
            binding.whatDY.visibility = View.VISIBLE
            binding.checkIn.visibility = View.GONE
            binding.cardVenue.visibility = View.VISIBLE
        }
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocationPost.setText(CountryStateCityFrBo)
        location = CountryStateCityFrBo
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

    override fun bottomSelectAudienceClick(audienceFrBo: String) {
        if (canSee == 1){
            binding.canSeeText.text = audienceFrBo
        }else{
            binding.canCommentText.text = audienceFrBo
        }
    }




}