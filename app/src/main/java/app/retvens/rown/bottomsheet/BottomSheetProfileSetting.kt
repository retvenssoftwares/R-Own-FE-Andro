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


class BottomSheetProfileSetting : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetProfileSetting? {
        return BottomSheetProfileSetting()
    }
    interface OnBottomSheetProfileSettingClickListener{
        fun bottomSheetProfileSettingClick(bottomSheetProfileSettingFrBo : String)
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
        val profile= view.findViewById<LinearLayout>(R.id.profile)
        profile.setOnClickListener {
            mListener?.bottomSheetProfileSettingClick("profile")
            dismiss()
        }
        val settings= view.findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            mListener?.bottomSheetProfileSettingClick("settings")
            dismiss()
        }
        val edit= view.findViewById<LinearLayout>(R.id.edit)
        edit.setOnClickListener {
            mListener?.bottomSheetProfileSettingClick("edit")
            dismiss()
        }
        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            mListener?.bottomSheetProfileSettingClick("saved")
            dismiss()
        }
        val discover= view.findViewById<LinearLayout>(R.id.discover)
        discover.setOnClickListener {
            mListener?.bottomSheetProfileSettingClick("discover")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}