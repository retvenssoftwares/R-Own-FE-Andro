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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetJobFilter : BottomSheetDialogFragment(),
    BottomSheetJobType.OnBottomJobTypeClickListener, BottomSheetLocation.OnBottomLocationClickListener,
BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener, BottomSheetCTC.OnBottomCTCClickListener,
BottomSheetJobTitle.OnBottomJobTitleClickListener{

    var mListener: OnBottomJobClickListener ? = null
    fun setOnJobClickListener(listener: OnBottomJobClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobFilter? {
        return BottomSheetJobFilter()
    }
    interface OnBottomJobClickListener{
        fun bottomJobClick(category:String,type:String,location: String,salary:String)
    }

    companion object {
        const val Job_TAG = "BottomSheetDailog"
    }

    lateinit var jobTypeText :TextView
    lateinit var locationText :TextView
    lateinit var expectedSalary :TextView
    lateinit var selectJob:TextView

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_job_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expectedSalary = view.findViewById(R.id.expacted_salary_text)
        selectJob = view.findViewById(R.id.select_Job)

        view.findViewById<ConstraintLayout>(R.id.expacted_salary).setOnClickListener {
            val bottomSheet = BottomSheetCTC()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }

        val jt= view.findViewById<RelativeLayout>(R.id.filter_job_type)
        jobTypeText = view.findViewById(R.id.jType_text)
        locationText = view.findViewById(R.id.filter_location_text)
        jt.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }
        val fL = view.findViewById<RelativeLayout>(R.id.filter_location)
        fL.setOnClickListener {
             val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        view.findViewById<RelativeLayout>(R.id.select_jobCategories).setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        view.findViewById<CardView>(R.id.card_show_results).setOnClickListener {

            val job = selectJob.text.toString()
            val type = jobTypeText.text.toString()
            val location = locationText.text.toString()
            val salary = expectedSalary.text.toString()

            dismiss()

            mListener?.bottomJobClick(job,type,location,salary)

        }


    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        jobTypeText.text = jobTypeFrBo
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        locationText.text = CountryStateCityFrBo
    }

    override fun bottomLocationClick(LocationFrBo: String, NumericCodeFrBo: String) {

    }

    override fun bottomCTCClick(CTCFrBo: String) {
        expectedSalary.text = CTCFrBo
    }

    override fun bottomJobTitleClick(jobTitleFrBo: String) {
            selectJob.text = jobTitleFrBo
    }
}