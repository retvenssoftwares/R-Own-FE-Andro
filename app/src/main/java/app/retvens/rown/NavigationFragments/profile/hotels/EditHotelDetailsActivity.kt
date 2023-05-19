package app.retvens.rown.NavigationFragments.profile.hotels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.databinding.ActivityEditHotelDetailsBinding

class EditHotelDetailsActivity : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {
    lateinit var binding : ActivityEditHotelDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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