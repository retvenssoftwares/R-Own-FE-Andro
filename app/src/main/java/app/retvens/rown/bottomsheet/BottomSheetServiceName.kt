package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.NavigationFragments.eventsForHoteliers.BottomEventCategoriesDataItem
import app.retvens.rown.NavigationFragments.eventsForHoteliers.BottomEventsCategoriesAdapter
import app.retvens.rown.NavigationFragments.profile.services.BottomServiceNameAdapter
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetServiceName : BottomSheetDialogFragment() {

    var mListener: OnBottomSNClickListener ? = null
    fun setOnSNclickListener(listener: OnBottomSNClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetServiceName? {
        return BottomSheetServiceName()
    }
    interface OnBottomSNClickListener{
        fun bottomSNClick(serviceName : String, id : String)
    }

    companion object {
        const val SN_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var bottomServiceNameAdapter: BottomServiceNameAdapter

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var ssearchLocation : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ssearchLocation = view.findViewById(R.id.search_country)

        recyclerView = view.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getUserLocation()
    }

    private fun getUserLocation() {

        val getLocation = RetrofitBuilder.ProfileApis.getProfileServiceName()
        getLocation.enqueue(object : Callback<List<ProfileServicesDataItem>?>, BottomServiceNameAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<ProfileServicesDataItem>?>,
                response: Response<List<ProfileServicesDataItem>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    bottomServiceNameAdapter = BottomServiceNameAdapter(requireContext(),response)
                    bottomServiceNameAdapter.notifyDataSetChanged()
                    recyclerView.adapter = bottomServiceNameAdapter
                    bottomServiceNameAdapter.setOnLocationClickListener(this)

                    ssearchLocation.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }
                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val original = response.toList()
                            val filter = original.filter { searchUser ->
                                searchUser.service_name.contains(s.toString(),ignoreCase = true)
                            }
                            bottomServiceNameAdapter.search(filter)
                        }
                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProfileServicesDataItem>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onCountryClick(service: String, id: String) {
                mListener?.bottomSNClick(service, id)
                dismiss()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}