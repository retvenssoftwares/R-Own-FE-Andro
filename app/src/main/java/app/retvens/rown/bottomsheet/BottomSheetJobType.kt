package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetJobType : BottomSheetDialogFragment() {

    var mListener: OnBottomJobTypeClickListener ? = null
    fun setOnJobTypeClickListener(listener: OnBottomJobTypeClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobType? {
        return BottomSheetJobType()
    }
    interface OnBottomJobTypeClickListener{
        fun bottomJobTypeClick(jobTypeFrBo : String)
    }

    companion object {
        const val Job_TYPE_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_job_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
//            mListener?.bottomJobTypeClick("Select one -")
//            dismiss()
//        }
        view.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
            mListener?.bottomJobTypeClick("Full-Time")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.partTime).setOnClickListener {
            mListener?.bottomJobTypeClick("Part-Time")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.selfE).setOnClickListener {
            mListener?.bottomJobTypeClick("Self-Employed")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.Freelancer).setOnClickListener {
            mListener?.bottomJobTypeClick("Freelancer")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.Internship).setOnClickListener {
            mListener?.bottomJobTypeClick("Internship")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.Trainee).setOnClickListener {
            mListener?.bottomJobTypeClick("Trainee")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}