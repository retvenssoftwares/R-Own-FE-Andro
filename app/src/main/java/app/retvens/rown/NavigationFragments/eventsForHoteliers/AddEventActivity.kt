package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetVenueByLocation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddEventActivity : AppCompatActivity(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener,
    BottomSheetVenueByLocation.OnBottomVBLClickListener {

    lateinit var locationLayout: TextInputLayout
    lateinit var et_location_event : TextInputEditText
    lateinit var venueLayout: TextInputLayout
    lateinit var et_event_venue : TextInputEditText
    lateinit var next : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        findViewById<ImageButton>(R.id.faq_backBtn).setOnClickListener { onBackPressed() }

        locationLayout = findViewById(R.id.user_location_post_event)
        et_location_event = findViewById(R.id.et_location_event)

        venueLayout = findViewById(R.id.til_post_event)
        et_event_venue = findViewById(R.id.et_event_venue)
        next = findViewById(R.id.next_Event)

        et_event_venue.setOnClickListener {
            if(et_location_event.text.toString() == "Select Venue Location"){
                locationLayout.error = "Please select location"
            } else {
                locationLayout.isErrorEnabled = false
                val bottomSheet = BottomSheetVenueByLocation(et_location_event.text.toString())
                val fragManager = supportFragmentManager
                fragManager.let { bottomSheet.show(it, BottomSheetVenueByLocation.EC_TAG) }
                bottomSheet.setOnECclickListener(this)
            }
        }
        et_location_event.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        next.setOnClickListener {
            if (et_location_event.text.toString() == "Select Venue Location"){
                locationLayout.error = "Please select location"
            } else if (et_event_venue.text.toString() == "Select Venue"){
                venueLayout.error = "please select venue name"
            } else {
                val intent = Intent(this, CreateEventActivity::class.java)
                intent.putExtra("location", et_location_event.text.toString())
                intent.putExtra("venue", et_event_venue.text.toString())
                startActivity(intent)
            }
        }
        postUpdateEvent()
    }
    private fun postUpdateEvent() {
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        et_location_event.setText(CountryStateCityFrBo)
    }

    override fun bottomVBLCClick(eventC: String, NumericCodeFrBo: String) {
        et_event_venue.setText(eventC)
    }
}