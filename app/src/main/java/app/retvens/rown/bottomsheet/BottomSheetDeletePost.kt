package app.retvens.rown.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
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


class BottomSheetDeletePost(val PostId:String) : BottomSheetDialogFragment(){


    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetDeletePost? {
        return BottomSheetDeletePost("")
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
        return inflater.inflate(R.layout.fragment_bottom_delete_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deletePost = view.findViewById<CardView>(R.id.card_yes)
        val deleteNoPost = view.findViewById<CardView>(R.id.card_no)

        deletePost.setOnClickListener {
            saveChanges()
        }

        deleteNoPost.setOnClickListener {
            dismiss()
        }

    }

    private fun saveChanges() {

        val saveChange = RetrofitBuilder.feedsApi.deletePost(PostId, DeletePost("0"))

        saveChange.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {

            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}