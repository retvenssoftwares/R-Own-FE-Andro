package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetJobDepartment : BottomSheetDialogFragment() {

    var mListener: OnBottomJobDepartmentClickListener ? = null
    fun setOnJobDepartmentClickListener(listener: OnBottomJobDepartmentClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobDepartment? {
        return BottomSheetJobDepartment()
    }
    interface OnBottomJobDepartmentClickListener{
        fun bottomJobDepartmentClick(jobDepartFrBo : String)
    }

    companion object {
        const val Job_D_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_department, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jt= view.findViewById<LinearLayout>(R.id.ll_ss)
        jt.setOnClickListener {
            mListener?.bottomJobDepartmentClick("Software and Sales")
            dismiss()
        }
        val jtm= view.findViewById<LinearLayout>(R.id.ll_sm)
        jtm.setOnClickListener {
            mListener?.bottomJobDepartmentClick("Sales and Marketing")
            dismiss()
        }
        val jtmd= view.findViewById<LinearLayout>(R.id.ll_smo)
        jtmd.setOnClickListener {
            mListener?.bottomJobDepartmentClick("Online Sales")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}