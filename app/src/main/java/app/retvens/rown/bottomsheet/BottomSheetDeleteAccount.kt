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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.DeleteAccount
import app.retvens.rown.DataCollections.FeedCollection.DeletePost
import app.retvens.rown.DataCollections.FeedCollection.EditPostClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.authentication.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetDeleteAccount() : BottomSheetDialogFragment(){


    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetDeleteAccount? {
        return BottomSheetDeleteAccount()
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
        val text = view.findViewById<TextView>(R.id.deleteText)
        text.text = "Do you really want to      delete this Account?"

        deletePost.setOnClickListener {
            saveChanges()
        }

        deleteNoPost.setOnClickListener {
            dismiss()
        }

    }

    private fun saveChanges() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
       val delete = RetrofitBuilder.ProfileApis.deleteAccount(DeleteAccount(user_id))

        delete.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),"Account Deleted",Toast.LENGTH_SHORT).show()
                    dismiss()
                    startActivity(Intent(requireContext(),LoginActivity::class.java))
                }else{
                    Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })


    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}