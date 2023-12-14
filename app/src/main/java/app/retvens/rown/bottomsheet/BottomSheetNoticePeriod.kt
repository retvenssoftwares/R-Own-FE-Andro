package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetNoticePeriod : BottomSheetDialogFragment() {

    var mListener: OnBottomNoticeClickListener ? = null
    fun setOnNoticeClickListener(listener: OnBottomNoticeClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetNoticePeriod? {
        return BottomSheetNoticePeriod()
    }
    interface OnBottomNoticeClickListener{
        fun bottomNoticeClick(noticeFrBo : String)
    }

    companion object {
        const val Notice_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_notice_period, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
//            mListener?.bottomNoticeClick("Select one -")
//            dismiss()
//        }
        view.findViewById<RelativeLayout>(R.id.no).setOnClickListener {
            mListener?.bottomNoticeClick("No")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.one_month_less).setOnClickListener {
            mListener?.bottomNoticeClick("< 1 Month")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.one_month).setOnClickListener {
            mListener?.bottomNoticeClick(" 1 Month")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.two_month).setOnClickListener {
            mListener?.bottomNoticeClick(" 2 Month")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.three_month).setOnClickListener {
            mListener?.bottomNoticeClick(" 3 Month")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.more_three_month).setOnClickListener {
            mListener?.bottomNoticeClick("> 3 Month")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}