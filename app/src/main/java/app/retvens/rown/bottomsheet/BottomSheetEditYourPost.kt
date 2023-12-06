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
import app.retvens.rown.DataCollections.FeedCollection.EditPostClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetEditYourPost(val postId:String,val captiontext:String,val locationtext:String) : BottomSheetDialogFragment(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    private lateinit var caption:TextInputEditText
    private lateinit var location:TextInputEditText
    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetEditYourPost? {
        return BottomSheetEditYourPost("","","")
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
        return inflater.inflate(R.layout.bottom_edit_your_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        caption = view.findViewById(R.id.edit_caption)
        location = view.findViewById(R.id.et_location_country)

        caption.setText(captiontext)
        location.setText(locationtext)

        view.findViewById<CardView>(R.id.save_changes).setOnClickListener {

                saveChanges()
        }

        val selectLocation = view.findViewById<TextInputLayout>(R.id.user_location_country)

        location.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)

        }

    }

    private fun saveChanges() {

        val saveChange = RetrofitBuilder.feedsApi.editPost(postId, EditPostClass(caption.text.toString(),location.text.toString()))

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

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        location.setText(CountryStateCityFrBo)
    }

    override fun selectlocation(latitude: String, longitude: String) {

    }
}