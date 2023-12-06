package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.JobsCollection.HotelsDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.location.CompanyAdapter
import app.retvens.rown.DataCollections.location.HotelsAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetHotels : BottomSheetDialogFragment() {

    var mListener: OnBottomCompanyClickListener ? = null
    fun setOnCompanyClickListener(listener: OnBottomCompanyClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetHotels? {
        return BottomSheetHotels()
    }
    interface OnBottomCompanyClickListener{
        fun bottomLocationClick(hotelName : String,hotelId:String)
    }

    companion object {
        const val Company_TAG = "BottomSheetDailog"
    }


    lateinit var recyclerView : RecyclerView


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.company_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         //recyclerView. //recyclerView.setHasFixedSize(true)

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getCompany = RetrofitBuilder.jobsApis.getHotelList(user_id)

        getCompany.enqueue(object : Callback<List<HotelsDataClass>?>,
            HotelsAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<HotelsDataClass>?>,
                response: Response<List<HotelsDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    val hotelAdapter = HotelsAdapter(requireContext(),response)
                    recyclerView.adapter = hotelAdapter
                    hotelAdapter.setOnLocationClickListener(this)
                }

            }

            override fun onFailure(call: Call<List<HotelsDataClass>?>, t: Throwable) {

            }

            override fun onStateDataClick(companyDataClass: HotelsDataClass) {
                mListener?.bottomLocationClick(companyDataClass.hotelName,companyDataClass.hotel_id)
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}