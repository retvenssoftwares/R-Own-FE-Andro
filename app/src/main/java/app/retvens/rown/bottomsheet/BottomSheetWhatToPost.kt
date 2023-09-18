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


class BottomSheetWhatToPost : BottomSheetDialogFragment() {

    var mListener: OnBottomWhatToPostClickListener ? = null
    fun setOnWhatToPostClickListener(listener: OnBottomWhatToPostClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetWhatToPost? {
        return BottomSheetWhatToPost()
    }
    interface OnBottomWhatToPostClickListener{
        fun bottomWhatToPostClick(WhatToPostFrBo : String)
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
        return inflater.inflate(R.layout.bottom_sheet_what_to_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c_and_share= view.findViewById<LinearLayout>(R.id.c_and_share)
        c_and_share.setOnClickListener {
            mListener?.bottomWhatToPostClick("Click")
            dismiss()
        }
        val share_medias= view.findViewById<LinearLayout>(R.id.share_medias)
        share_medias.setOnClickListener {
            mListener?.bottomWhatToPostClick("Share")
            dismiss()
        }
        val update_event= view.findViewById<LinearLayout>(R.id.update_event)
        update_event.setOnClickListener {
            mListener?.bottomWhatToPostClick("Update")
            dismiss()
        }
        val check_in= view.findViewById<LinearLayout>(R.id.check_in)
        check_in.setOnClickListener {
            mListener?.bottomWhatToPostClick("Check")
            dismiss()
        }
        val poll= view.findViewById<LinearLayout>(R.id.poll)
        poll.setOnClickListener {
            mListener?.bottomWhatToPostClick("Poll")
            dismiss()
        }

        val status = view.findViewById<LinearLayout>(R.id.status)
        status.setOnClickListener{
            mListener?.bottomWhatToPostClick("Status")
            dismiss()
        }


    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}