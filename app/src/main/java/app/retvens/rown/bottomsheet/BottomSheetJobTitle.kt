package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetJobTitle : BottomSheetDialogFragment() {

    var mListener: OnBottomJobTitleClickListener ? = null
    fun setOnJobTitleClickListener(listener: OnBottomJobTitleClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobTitle? {
        return BottomSheetJobTitle()
    }
    interface OnBottomJobTitleClickListener{
        fun bottomJobTitleClick(jobTitleFrBo : String)
    }

    companion object {
        const val Job_Title_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_job_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.jobs_recycler).setOnClickListener {
            mListener?.bottomJobTitleClick("Selected recycler")
            dismiss()
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}