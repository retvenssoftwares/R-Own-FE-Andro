package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddReview : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetAddReviewClickListener ? = null
    fun setOnReviewClickListener(listener: OnBottomSheetAddReviewClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetAddReview? {
        return BottomSheetAddReview()
    }
    interface OnBottomSheetAddReviewClickListener{
        fun bottomReviewClick(reviewFrBo : String)
    }

    companion object {
        const val Review_TAG = "BottomSheetDailog"
    }


    lateinit var recyclerView : RecyclerView


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recyclerView = view.findViewById(R.id.recycler_review)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        recyclerView.setHasFixedSize(true)

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}