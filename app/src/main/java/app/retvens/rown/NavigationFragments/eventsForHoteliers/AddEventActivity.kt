package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import com.google.android.material.textfield.TextInputEditText

class AddEventActivity : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    lateinit var et_location_event : TextInputEditText
    lateinit var et_event_venue : TextInputEditText
    lateinit var next : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        et_location_event = findViewById(R.id.et_location_event)
        et_event_venue = findViewById(R.id.et_event_venue)
        next = findViewById(R.id.next_Event)
        postUpdateEvent()
    }
    private fun postUpdateEvent() {
        et_location_event.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        et_event_venue.setOnClickListener {
            Toast.makeText(this, "Will implement along with api", Toast.LENGTH_SHORT).show()
        }

        next.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        et_location_event.setText(CountryStateCityFrBo)
    }
}