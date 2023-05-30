package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.NavigationFragments.eventsForHoteliers.BottomVenueByLocationAdapter
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetVenueByLocation(val location : String) : BottomSheetDialogFragment() {

    var mListener: OnBottomVBLClickListener ? = null
    fun setOnECclickListener(listener: OnBottomVBLClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetVenueByLocation? {
        return BottomSheetVenueByLocation("Indore,Madhya Pradesh,India")
    }
    interface OnBottomVBLClickListener{
        fun bottomVBLCClick(eventC : String, NumericCodeFrBo : String)
    }

    companion object {
        const val EC_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var becA : BottomVenueByLocationAdapter

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var ssearchLocation : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_venue_by_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ssearchLocation = view.findViewById(R.id.search_country)

        recyclerView = view.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getUserVenueLocation()
    }

    private fun getUserVenueLocation() {

        val getLocation = RetrofitBuilder.EventsApi.getBottomSheetVenueByLocation(location)
        getLocation.enqueue(object : Callback<List<HotelsName>?>, BottomVenueByLocationAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<HotelsName>?>,
                response: Response<List<HotelsName>?>
            ) {

                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    becA = BottomVenueByLocationAdapter(requireContext(),response)
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
                                searchUser.hotelName.contains(s.toString(),ignoreCase = true)
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

            override fun onFailure(call: Call<List<HotelsName>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onCountryClick(country: String, code: String) {
                mListener?.bottomVBLCClick(country, code)
                dismiss()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}