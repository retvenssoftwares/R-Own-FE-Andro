package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCompany : BottomSheetDialogFragment() {

    var mListener: OnBottomCompanyClickListener ? = null
    fun setOnCompanyClickListener(listener: OnBottomCompanyClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetCompany? {
        return BottomSheetCompany()
    }
    interface OnBottomCompanyClickListener{
        fun bottomLocationClick(CompanyFrBo : String)
    }

    companion object {
        const val Company_TAG = "BottomSheetDailog"
    }


    lateinit var recyclerView : RecyclerView


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}