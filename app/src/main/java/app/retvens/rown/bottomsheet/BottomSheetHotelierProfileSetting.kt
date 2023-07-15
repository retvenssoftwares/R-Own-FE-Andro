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


class BottomSheetHotelierProfileSetting : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetHotelierProfileSetting? {
        return BottomSheetHotelierProfileSetting()
    }
    interface OnBottomSheetHotelierProfileSettingClickListener{
        fun bottomSheetHotelierProfileSettingClick(bottomSheetHotelierProfileSettingFrBo : String)
    }

    companion object {
        const val Hotelier_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_profile_setting_for_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val profile= view.findViewById<LinearLayout>(R.id.profile)
//        profile.setOnClickListener {
//            mListener?.bottomSheetProfileSettingClick("profile")
//            dismiss()
//        }

        view.findViewById<View>(R.id.viewH).visibility = View.VISIBLE
        val settings= view.findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("settings")
            dismiss()
        }
        val profileDetails= view.findViewById<LinearLayout>(R.id.profileDetails)
        profileDetails.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("profileDetails")
            dismiss()
        }
        val edit= view.findViewById<LinearLayout>(R.id.edit)
        edit.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("edit")
            dismiss()
        }
      val editHotelier= view.findViewById<LinearLayout>(R.id.editHotelier)
        editHotelier.visibility = View.VISIBLE
        editHotelier.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("editHotelier")
            dismiss()
        }
        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("saved")
            dismiss()
        }
        val discover= view.findViewById<LinearLayout>(R.id.discover)
        discover.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("discover")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}