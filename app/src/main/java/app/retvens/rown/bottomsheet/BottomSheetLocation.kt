package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetLocation : BottomSheetDialogFragment() {

    var mListener: OnBottomLocationClickListener ? = null
    fun setOnLocationClickListener(listener: OnBottomLocationClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetLocation? {
        return BottomSheetLocation()
    }
    interface OnBottomLocationClickListener{
        fun bottomLocationClick(LocationFrBo : String)
    }

    companion object {
        const val LOCATION_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var locationFragmentAdapter : LocationFragmentAdapter

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        getUserLocation()

        recyclerView = view.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


    }

    private fun getUserLocation() {

        val getLocation = RetrofitBuilder.profileCompletion.getLocation()

        getLocation.enqueue(object : Callback<List<LocationDataClass>?>,
            LocationFragmentAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<LocationDataClass>?>,
                response: Response<List<LocationDataClass>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    locationFragmentAdapter = LocationFragmentAdapter(requireContext(),response)
                    locationFragmentAdapter.notifyDataSetChanged()
                    recyclerView.adapter = locationFragmentAdapter
                    locationFragmentAdapter.setOnJobClickListener(this)
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LocationDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onJobClick(job: LocationDataClass) {
                for (x in job.states){
                    for (y in x.name){
                        mListener?.bottomLocationClick(y.toString())
                    }
                }
                dismiss()
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}