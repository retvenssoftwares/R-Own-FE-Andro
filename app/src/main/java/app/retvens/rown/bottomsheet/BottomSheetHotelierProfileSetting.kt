package app.retvens.rown.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.UserName
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetHotelierProfileSetting : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    var completion = ""
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetHotelierProfileSetting? {
        return BottomSheetHotelierProfileSetting()
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
        return inflater.inflate(R.layout.bottom_profile_setting_for_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val profile= view.findViewById<LinearLayout>(R.id.profile)
//        profile.setOnClickListener {
//            mListener?.bottomSheetProfileSettingClick("profile")
//            dismiss()
//        }

        val sharedPreferencesId = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesId?.getString("user_id", "").toString()
        fetchProfile(user_id)


        view.findViewById<View>(R.id.viewH).visibility = View.VISIBLE
        val settings= view.findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("settings")
            dismiss()
        }
        val profileDetails= view.findViewById<LinearLayout>(R.id.profileDetails)
        profileDetails.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("profileDetails")
            dismiss()
        }
        val edit= view.findViewById<LinearLayout>(R.id.edit)
        val edittext = view.findViewById<TextView>(R.id.edit_text)
        val handler = Handler()
        handler.postDelayed({
            if (completion == "100"){
                edit.setOnClickListener {
                    mListener?.bottomSheetHotelierProfileSettingClick("edit")
                    dismiss()
                }
            }else{
                edittext.text = "Complete Your Profile"
                edit.setOnClickListener {
                    startActivity(Intent(context, UserName::class.java))
                }
            }
        },1000)

      val editHotelier= view.findViewById<LinearLayout>(R.id.editHotelier)
        editHotelier.visibility = View.VISIBLE
        editHotelier.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("editHotelier")
            dismiss()
        }
        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("saved")
            dismiss()
        }
        val discover= view.findViewById<LinearLayout>(R.id.discover)
        discover.setOnClickListener {
            mListener?.bottomSheetHotelierProfileSettingClick("discover")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun fetchProfile(userId: String) {

        val getData = RetrofitBuilder.retrofitBuilder.fetchUser(userId)

        getData.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    completion = response.body()!!.profileCompletionStatus
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {

            }
        })

    }
}