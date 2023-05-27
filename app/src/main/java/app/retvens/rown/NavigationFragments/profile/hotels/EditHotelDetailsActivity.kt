package app.retvens.rown.NavigationFragments.profile.hotels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.databinding.ActivityEditHotelDetailsBinding
import com.bumptech.glide.Glide

class EditHotelDetailsActivity : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding : ActivityEditHotelDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBackBtn.setOnClickListener {
            onBackPressed()
        }

        val hotelName = intent.getStringExtra("name")
        val hotelLogo = intent.getStringExtra("logo")
        val location = intent.getStringExtra("location")
        val Hoteldescription = intent.getStringExtra("hotelDescription").toString()

        binding.overviewEt.setText(Hoteldescription)
        binding.locationText.text = location

        binding.etNameEdit.setText(hotelName)
        Glide.with(this).load(hotelLogo).into(binding.img1)

        binding.hotelLocation.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.locationText.text = CountryStateCityFrBo
    }
}