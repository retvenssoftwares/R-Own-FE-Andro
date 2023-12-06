package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetJobDesignation : BottomSheetDialogFragment() {

    var mListener: OnBottomJobDesignationClickListener ? = null
    fun setOnJobDesignationClickListener(listener: OnBottomJobDesignationClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobDesignation? {
        return BottomSheetJobDesignation()
    }
    interface OnBottomJobDesignationClickListener{
        fun bottomJobDesignationClick(jobDesignationFrBo : String)
    }

    companion object {
        const val Job_Designation_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_designation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jt= view.findViewById<LinearLayout>(R.id.hotel_owner)
        jt.setOnClickListener {
            mListener?.bottomJobDesignationClick("Assistance")
            dismiss()
        }
        val jtm= view.findViewById<LinearLayout>(R.id.hos_expert)
        jtm.setOnClickListener {
            mListener?.bottomJobDesignationClick("Associate")
            dismiss()
        }
        val jtmd= view.findViewById<LinearLayout>(R.id.vendor)
        jtmd.setOnClickListener {
            mListener?.bottomJobDesignationClick("Administrative Assistant")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}