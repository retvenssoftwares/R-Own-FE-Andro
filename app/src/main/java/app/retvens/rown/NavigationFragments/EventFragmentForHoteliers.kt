package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.CreateEventActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import com.google.android.material.textfield.TextInputEditText

class EventFragmentForHoteliers : Fragment(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    lateinit var et_location_event : TextInputEditText
    lateinit var et_event_venue : TextInputEditText
    lateinit var next : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_for_hoteliers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_location_event = view.findViewById(R.id.et_location_event)
        et_event_venue = view.findViewById(R.id.et_event_venue)
        next = view.findViewById(R.id.next_Event)
        postUpdateEvent()
    }
    private fun postUpdateEvent() {
        et_location_event.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        et_event_venue.setOnClickListener {
            Toast.makeText(context, "Will implement along with api", Toast.LENGTH_SHORT).show()
        }

        next.setOnClickListener {
            startActivity(Intent(context, CreateEventActivity::class.java))
        }
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        et_location_event.setText(CountryStateCityFrBo)
    }
}