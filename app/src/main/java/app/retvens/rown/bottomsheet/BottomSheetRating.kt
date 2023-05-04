package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetRating : BottomSheetDialogFragment() {

    var mListener: OnBottomRatingClickListener ? = null
    fun setOnRatingClickListener(listener: OnBottomRatingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetRating? {
        return BottomSheetRating()
    }
    interface OnBottomRatingClickListener{
        fun bottomRatingClick(ratingFrBo : String)
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
            mListener?.bottomRatingClick("1 -3 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.fourStar).setOnClickListener {
            mListener?.bottomRatingClick("4 Star")
            dismiss()
        }

        view.findViewById<RelativeLayout>(R.id.fiveStar).setOnClickListener {
            mListener?.bottomRatingClick("5 Star")
            dismiss()
        }

        view.findViewById<RelativeLayout>(R.id.sixStar).setOnClickListener {
            mListener?.bottomRatingClick("6 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.sevenStar).setOnClickListener {
            mListener?.bottomRatingClick("7 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.eightStar).setOnClickListener {
            mListener?.bottomRatingClick("8 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.nineStar).setOnClickListener {
            mListener?.bottomRatingClick("9 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.tenStar).setOnClickListener {
            mListener?.bottomRatingClick("10 Star")
            dismiss()
        }
        view.findViewById<RelativeLayout>(R.id.elevenStar).setOnClickListener {
            mListener?.bottomRatingClick(">11 Star")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}