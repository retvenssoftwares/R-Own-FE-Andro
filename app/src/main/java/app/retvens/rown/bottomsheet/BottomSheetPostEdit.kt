package app.retvens.rown.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetPostEdit(val PostId:String,val caption:String,val location:String) : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetPostEdit? {
        return BottomSheetPostEdit("","","")
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
        return inflater.inflate(R.layout.bottom_edit_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val profile= view.findViewById<LinearLayout>(R.id.profile)
//        profile.setOnClickListener {
//            mListener?.bottomSheetProfileSettingClick("profile")
//            dismiss()
//        }


        val edit= view.findViewById<LinearLayout>(R.id.edit)
        edit.setOnClickListener {
            val bottomSheet = BottomSheetEditYourPost(PostId,caption,location)
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditYourPost.Hotelier_TAG)}
            dismiss()
        }

        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            val bottomSheet = BottomSheetDeletePost(PostId)
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetDeletePost.Hotelier_TAG)}
            dismiss()
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}