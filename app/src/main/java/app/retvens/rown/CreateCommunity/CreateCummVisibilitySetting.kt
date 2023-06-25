package app.retvens.rown.CreateCommunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.databinding.ActivityCreateCommunityBinding
import app.retvens.rown.databinding.ActivityCreateCummVisibilitySettingBinding

class CreateCummVisibilitySetting : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    lateinit var binding : ActivityCreateCummVisibilitySettingBinding
    lateinit var latitudes: String
    lateinit var longitudes: String
    var selectedLayout : Int = 1
    private  var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCummVisibilitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")

        binding.closedCummLayout.setOnClickListener {

            type = "close"

            selectedLayout = 1
            binding.layVisibility.visibility = View.VISIBLE

            binding.closedCummHintText.visibility = View.VISIBLE
            binding.openHintText.visibility = View.GONE
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.openCummLayout.setOnClickListener {

            type = "Open Community"
            selectedLayout = 2
            binding.layVisibility.visibility = View.VISIBLE

            binding.openHintText.visibility = View.VISIBLE
            binding.closedCummHintText.visibility = View.GONE
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.etLocation.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.nextCumm.setOnClickListener {
            if (binding.etLocation.text.toString() == "Select Your Location"){
                binding.userLocationCountry.error = "Select Your Location"
            } else {
                val intent = Intent(this, SelectMembers::class.java)
                intent.putExtra("type", type)
                intent.putExtra("name", name)
                intent.putExtra("desc", description)
                intent.putExtra("location", binding.etLocation.text.toString())
                intent.putExtra("longitude", longitudes)
                intent.putExtra("latitude", latitudes)
                startActivity(intent)
                Log.e("location",binding.etLocation.text.toString())
            }
        }
    }
    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocation.setText(CountryStateCityFrBo)

    }

    override fun selectlocation(latitude: String, longitude: String) {
        latitudes = longitude
        longitudes = longitude
    }
}