package app.retvens.rown.bottomsheet

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.Dashboard.profileCompletion.frags.HotelOwnerChainFragment
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetRating : BottomSheetDialogFragment() {

    var mListener: OnBottomRatingClickListener ? = null
//    private var OnBottomRatingClickListener: String? = ""
    fun setOnRatingClickListener(listener: OnBottomRatingClickListener?){
        mListener = listener
    }

    interface OnBottomRatingClickListener{
        fun BottomRatongClick(ratingFrBo : String)
    }

    companion object {
        const val RATING_TAG = "BottomSheetRatingDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RelativeLayout>(R.id.oneStar).setOnClickListener {
            mListener?.BottomRatongClick("1 -3 Star")
        }

    }
}