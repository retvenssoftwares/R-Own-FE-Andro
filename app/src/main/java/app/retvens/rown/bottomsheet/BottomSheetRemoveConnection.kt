package app.retvens.rown.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.DeletePost
import app.retvens.rown.DataCollections.FeedCollection.EditPostClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetRemoveConnection() : BottomSheetDialogFragment(){


    var mListener: OnBottomSheetRemoveConnectionClickListener ? = null
    fun setOnBottomSheetRemoveConnectionClickListener(listener: OnBottomSheetRemoveConnectionClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetRemoveConnection? {
        return BottomSheetRemoveConnection()
    }
    interface OnBottomSheetRemoveConnectionClickListener{
        fun bottomSheetRemoveConnectionClick(removeConnection : String)
    }

    companion object {
        const val Remove_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_delete_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deletePost = view.findViewById<CardView>(R.id.card_yes)
        val deleteNoPost = view.findViewById<CardView>(R.id.card_no)
        val deleteText = view.findViewById<TextView>(R.id.deleteText)

        deleteText.text = "Do you really want to      REMOVE this user?"

        deletePost.setOnClickListener {
            mListener?.bottomSheetRemoveConnectionClick("Yes")
            dismiss()
        }

        deleteNoPost.setOnClickListener {
            mListener?.bottomSheetRemoveConnectionClick("No")
            dismiss()
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}