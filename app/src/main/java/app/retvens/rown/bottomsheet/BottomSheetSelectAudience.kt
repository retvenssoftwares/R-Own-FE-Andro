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


class BottomSheetSelectAudience : BottomSheetDialogFragment() {

    var mListener: OnBottomSelectAudienceClickListener ? = null
    fun setOnSelectAudienceClickListener(listener: OnBottomSelectAudienceClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetSelectAudience? {
        return BottomSheetSelectAudience()
    }
    interface OnBottomSelectAudienceClickListener{
        fun bottomSelectAudienceClick(audienceFrBo : String)
    }

    companion object {
        const val S_A_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_audience, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val any= view.findViewById<ConstraintLayout>(R.id.c_anyOne)
        any.setOnClickListener {
            mListener?.bottomSelectAudienceClick("Anyone")
            dismiss()
        }
        val conn= view.findViewById<ConstraintLayout>(R.id.c_connection)
        conn.setOnClickListener {
            mListener?.bottomSelectAudienceClick("Connections")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}