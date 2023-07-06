package app.retvens.rown.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.BottomSheetHotelAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetHotelDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetHotelByLocation(val location:String) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetHotelAdapter: BottomSheetHotelAdapter

    companion object {
        const val CountryStateCity_TAG = "BottomSheetDailog"
    }

    var mListener: OnBottomCountryStateCityClickListener ? = null
    fun setOnCountryStateCityClickListener(listener: OnBottomCountryStateCityClickListener?){
        mListener = listener
    }

    interface OnBottomCountryStateCityClickListener{
        fun bottomCountryStateCityClick(CountryStateCityFrBo : String)
    }

    fun newInstance(): BottomSheetWhatToPost? {
        return BottomSheetWhatToPost()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_hotel_by_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            getHotel()

        recyclerView = view.findViewById(R.id.hotel_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

    }

    private fun getHotel() {

        val getHotel = RetrofitBuilder.feedsApi.fetchbyLocation(location)

        getHotel.enqueue(object : Callback<List<GetHotelDataClass>?> {
            override fun onResponse(
                call: Call<List<GetHotelDataClass>?>,
                response: Response<List<GetHotelDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    bottomSheetHotelAdapter = BottomSheetHotelAdapter(requireContext(),response,location)
                    recyclerView.adapter = bottomSheetHotelAdapter
                    bottomSheetHotelAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(),response.code(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetHotelDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }
}