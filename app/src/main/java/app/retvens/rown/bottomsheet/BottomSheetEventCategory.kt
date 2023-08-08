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
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetEventCategory : BottomSheetDialogFragment() {

    var mListener: OnBottomEcClickListener ? = null
    fun setOnECclickListener(listener: OnBottomEcClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetEventCategory? {
        return BottomSheetEventCategory()
    }
    interface OnBottomEcClickListener{
        fun bottomEcClick(eventC : String, NumericCodeFrBo : String)
    }

    companion object {
        const val EC_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var becA : BottomEventsCategoriesAdapter

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var ssearchLocation : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_event_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ssearchLocation = view.findViewById(R.id.search_country)

        recyclerView = view.findViewById(R.id.location_recycler)
         //recyclerView. //recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getUserLocation()
    }

    private fun getUserLocation() {

        val getLocation = RetrofitBuilder.EventsApi.getBottomSheetEventCategories()
        getLocation.enqueue(object : Callback<List<BottomEventCategoriesDataItem>?>, BottomEventsCategoriesAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<BottomEventCategoriesDataItem>?>,
                response: Response<List<BottomEventCategoriesDataItem>?>
            ) {

                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    becA = BottomEventsCategoriesAdapter(requireContext(),response)
                    becA.notifyDataSetChanged()
                    recyclerView.adapter = becA
                    becA.setOnLocationClickListener(this)

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
                                searchUser.category_name.contains(s.toString(),ignoreCase = true)
                            }
                            becA.search(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BottomEventCategoriesDataItem>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onCountryClick(country: String, code: String) {
                mListener?.bottomEcClick(country, code)
                dismiss()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}