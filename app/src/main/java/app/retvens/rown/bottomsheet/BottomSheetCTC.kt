package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetCTC : BottomSheetDialogFragment() {

    var mListener: OnBottomCTCClickListener ? = null
    fun setOnCTCClickListener(listener: OnBottomCTCClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetCTC? {
        return BottomSheetCTC()
    }
    interface OnBottomCTCClickListener{
        fun bottomCTCClick(CTCFrBo : String)
    }

    companion object {
        const val CTC_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_expected_ctc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
//            mListener?.bottomCTCClick("Select one -")
//            dismiss()
//        }
        view.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
            mListener?.bottomCTCClick("1-3 Lakhs/p.a")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
            mListener?.bottomCTCClick("3-6 Lakhs/p.a")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
            mListener?.bottomCTCClick("6-10 Lakhs/p.a")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
            mListener?.bottomCTCClick(" 10-15 Lakhs/p.a")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
            mListener?.bottomCTCClick("15-25 Lakhs/p.a")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.more_than_tf).setOnClickListener {
            mListener?.bottomCTCClick(">25 Lakhs/p.a")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}