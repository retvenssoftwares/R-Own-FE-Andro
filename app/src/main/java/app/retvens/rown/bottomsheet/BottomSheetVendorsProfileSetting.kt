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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.UserName
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.BlockUserActivity
import app.retvens.rown.NavigationFragments.profile.EditVendorInfoActivity
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetVendorsProfileSetting : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetVendorsProfileSettingClickListener ? = null
    var completion = ""
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetVendorsProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetVendorsProfileSetting? {
        return BottomSheetVendorsProfileSetting()
    }
    interface OnBottomSheetVendorsProfileSettingClickListener{
        fun bottomSheetVendorsProfileSettingClick(bottomSheetProfileSettingFrBo : String)
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
        return inflater.inflate(R.layout.bottom_profile_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviews= view.findViewById<LinearLayout>(R.id.reviews)
        reviews.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("reviews")
            dismiss()
        }

        val sharedPreferencesId = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesId?.getString("user_id", "").toString()
        fetchProfile(user_id)

        val vendorView = view.findViewById<View>(R.id.vendorView)
        vendorView.visibility = View.VISIBLE
        val editVendor = view.findViewById<LinearLayout>(R.id.editVendor)
        editVendor.visibility = View.VISIBLE
        editVendor.setOnClickListener {
            if (completion == "100") {
                startActivity(Intent(requireContext(), EditVendorInfoActivity::class.java))
                dismiss()
            }else{
                Toast.makeText(requireContext(),"Complete Your Profile First",Toast.LENGTH_SHORT).show()
            }

        }

        val settings= view.findViewById<LinearLayout>(R.id.settings)
        settings.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("settings")
            dismiss()
        }
        val edit= view.findViewById<LinearLayout>(R.id.edit)
        val edittext = view.findViewById<TextView>(R.id.edit_text)
        val handler = Handler()
        handler.postDelayed({
            if (completion == "100"){
                edit.setOnClickListener {
                    mListener?.bottomSheetVendorsProfileSettingClick("edit")
                    dismiss()
                }
            }else{
                edittext.text = "Complete Your Profile"
                edit.setOnClickListener {
                    startActivity(Intent(context, UserName::class.java))
                }
            }
        },1000)
        val saved= view.findViewById<LinearLayout>(R.id.saved)
        saved.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("saved")
            dismiss()
        }
        val discover= view.findViewById<LinearLayout>(R.id.discover)
        discover.setOnClickListener {
            mListener?.bottomSheetVendorsProfileSettingClick("discover")
            dismiss()
        }

        val block= view.findViewById<LinearLayout>(R.id.block)
        block.setOnClickListener {
            startActivity(Intent(requireContext(), BlockUserActivity::class.java))
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