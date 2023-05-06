package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetGoingBack : BottomSheetDialogFragment() {

    var mListener: OnBottomGoingBackClickListener ? = null
    fun setOnGoingBackClickListener(listener: OnBottomGoingBackClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetGoingBack? {
        return BottomSheetGoingBack()
    }
    interface OnBottomGoingBackClickListener{
        fun bottomGoingBackClick(GoingBackFrBo : String)
    }

    companion object {
        const val GoingBack_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_going_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keep= view.findViewById<ConstraintLayout>(R.id.keep_edit)
        keep.setOnClickListener {
            mListener?.bottomGoingBackClick("Keep")
            dismiss()
        }
        val discard= view.findViewById<ConstraintLayout>(R.id.discard)
        discard.setOnClickListener {
            mListener?.bottomGoingBackClick("Discard")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}