package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFilterVendors : BottomSheetDialogFragment(),
    BottomSheetJobDesignation.OnBottomJobDesignationClickListener, BottomSheetLocation.OnBottomLocationClickListener,
BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener{

    var mListener: OnBottomFilterClickListener ? = null
    fun setOnFilterClickListener(listener: OnBottomFilterClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetFilterVendors? {
        return BottomSheetFilterVendors()
    }
    interface OnBottomFilterClickListener{
        fun bottomFilterVendorsClick(FilterVendorsFrBo : String)
    }

    companion object {
        const val FilterVendors_TAG = "BottomSheetDailog"
    }

    lateinit var selectServiceText :TextView
    lateinit var locationText :TextView

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_filter_vendors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectService= view.findViewById<RelativeLayout>(R.id.selectService)
        selectServiceText = view.findViewById(R.id.selectServiceText)
        locationText = view.findViewById(R.id.filter_location_text)
        selectService.setOnClickListener {
            val bottomSheet = BottomSheetJobDesignation()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobDesignation.Job_Designation_TAG)}
            bottomSheet.setOnJobDesignationClickListener(this)
        }
        val fL = view.findViewById<RelativeLayout>(R.id.filter_location)
        fL.setOnClickListener {
             val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        locationText.text = CountryStateCityFrBo
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }

    override fun bottomLocationClick(LocationFrBo: String, NumericCodeFrBo: String) {

    }

    override fun bottomJobDesignationClick(jobDesignationFrBo: String) {
        selectServiceText.text = jobDesignationFrBo
    }
}