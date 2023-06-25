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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFilterCommunity : BottomSheetDialogFragment(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener{

    var mListener: OnBottomSheetFilterCommunityClickListener ? = null
    fun setOnFilterClickListener(listener: OnBottomSheetFilterCommunityClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetFilterCommunity? {
        return BottomSheetFilterCommunity()
    }
    interface OnBottomSheetFilterCommunityClickListener{
        fun bottomSheetFilterCommunityClick(FilterCommunityFrBo : String)
    }

    companion object {
        const val FilterCommunity_TAG = "BottomSheetDailog"
    }

    lateinit var selectCommunityText :TextView
    lateinit var locationText :TextView

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_filter_communities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectCommunity= view.findViewById<RelativeLayout>(R.id.selectCommunity)
        selectCommunityText = view.findViewById(R.id.selectCommunityText)

        selectCommunity.setOnClickListener {
            openBottomCommunitySelection()
        }

        view.findViewById<CardView>(R.id.card_show_results).setOnClickListener {
            dismiss()
        }

        locationText = view.findViewById(R.id.filter_location_text)

        val fL = view.findViewById<ConstraintLayout>(R.id.filter_location)
        fL.setOnClickListener {
             val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
    }

    private fun openBottomCommunitySelection() {
        val dialogLanguage = Dialog(requireContext())
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_choose_community)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        dialogLanguage.findViewById<TextView>(R.id.open).setOnClickListener {
            selectCommunityText.text = "Open Community"
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.close).setOnClickListener {
            selectCommunityText.text = "Closed Community"
            dialogLanguage.dismiss()
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
        TODO("Not yet implemented")
    }

}