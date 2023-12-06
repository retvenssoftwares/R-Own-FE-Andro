package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomshitStatus: BottomSheetDialogFragment() {

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
        return inflater.inflate(R.layout.bottomsheet_status, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            mListener?.bottomCTCClick("Select one -")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
            mListener?.bottomCTCClick("On Hold")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
            mListener?.bottomCTCClick("Scheduled")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
            mListener?.bottomCTCClick("Criteria Doesn't Match")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
            mListener?.bottomCTCClick("Hired")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
            mListener?.bottomCTCClick("Rejected")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.more_than_tf).setOnClickListener {
            mListener?.bottomCTCClick("Promoted to further round")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}