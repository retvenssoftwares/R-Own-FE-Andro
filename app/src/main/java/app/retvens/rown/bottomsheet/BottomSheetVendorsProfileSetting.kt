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


class BottomSheetVendorsProfileSetting : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetVendorsProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetVendorsProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetVendorsProfileSetting? {
        return BottomSheetVendorsProfileSetting()
    }
    interface OnBottomSheetVendorsProfileSettingClickListener{
        fun bottomSheetVendorsProfileSettingClick(bottomSheetProfileSettingFrBo : String)
    }

    companion object {
        const val WTP_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_profile_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviews= view.findViewById<LinearLayout>(R.id.reviews)
        reviews.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("reviews")
            dismiss()
        }

        val settings= view.findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("settings")
            dismiss()
        }
        val edit= view.findViewById<LinearLayout>(R.id.edit)
        edit.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("edit")
            dismiss()
        }
        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("saved")
            dismiss()
        }
        val discover= view.findViewById<LinearLayout>(R.id.discover)
        discover.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("discover")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}