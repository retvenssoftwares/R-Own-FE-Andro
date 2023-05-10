package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetCountryStateCity : BottomSheetDialogFragment(),
    BottomSheetLocation.OnBottomLocationClickListener {

    var mListener: OnBottomCountryStateCityClickListener ? = null
    fun setOnCountryStateCityClickListener(listener: OnBottomCountryStateCityClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetCountryStateCity? {
        return BottomSheetCountryStateCity()
    }
    interface OnBottomCountryStateCityClickListener{
        fun bottomCountryStateCityClick(CountryStateCityFrBo : String)
    }

    companion object {
        const val CountryStateCity_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var locationFragmentAdapter : LocationFragmentAdapter


    lateinit var locationStateLayout : TextInputLayout
    lateinit var locationCityLayout : TextInputLayout

    lateinit var locationCountryET : TextInputEditText
    lateinit var locationStateET : TextInputEditText
    lateinit var locationCityET : TextInputEditText

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_country_state_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationStateLayout = view.findViewById(R.id.bottom_location_state)
        locationCityLayout = view.findViewById(R.id.bottom_location_city)

        locationCountryET = view.findViewById(R.id.et_location_country_bottom)
        locationStateET = view.findViewById(R.id.et_location_state_bottom)
        locationCityET = view.findViewById(R.id.et_location_city_bottom)

        locationCountryET.setOnClickListener {
            val bottomSheet = BottomSheetLocation()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            bottomSheet.setOnLocationClickListener(this)
        }

        view.findViewById<CardView>(R.id.card_location_done).setOnClickListener {
            val country = locationCountryET.text.toString()
            val state = locationStateET.text.toString()
            val city = locationCityET.text.toString()
            val location = "$city, $state, $country"

            mListener?.bottomCountryStateCityClick(location)
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun bottomLocationClick(LocationFrBo: String) {
        locationCountryET.setText(LocationFrBo)
        locationStateLayout.visibility = View.VISIBLE
    }

}