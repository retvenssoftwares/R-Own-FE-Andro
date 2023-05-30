package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.VendorServicesAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.PostVendorSerivces
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ProfileCompletion.VendorServicesData
import app.retvens.rown.NavigationFragments.profile.services.BottomServiceNameAdapter
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
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

    private lateinit var vendorServicesAdapter: VendorServicesAdapter
    private  var selectedServices:ArrayList<String> = ArrayList()
    private var vendorId:String = ""
    private lateinit var searchBar: EditText

    lateinit var addService: CardView

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = view.findViewById(R.id.search_country)

        recyclerView = view.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getBottomServices()
//        getUserLocation()
//        getVendor()

        addService = view.findViewById(R.id.addService)
        addService.setOnClickListener {
            selectedServices.forEach {
                setServices(it)
            }
//            dismiss()
        }
    }

    private fun setServices(s: String) {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val setSErv = RetrofitBuilder.profileCompletion.postServices(PostVendorSerivces(user_id, s))
        setSErv.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded){
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
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

                    searchBar.addTextChangedListener(object : TextWatcher {
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

  private fun getBottomServices() {
        val data = RetrofitBuilder.profileCompletion.getServices()

        data.enqueue(object : Callback<List<VendorServicesData>?>,
            VendorServicesAdapter.OnJobClickListener {
            override fun onResponse(
                call: Call<List<VendorServicesData>?>,
                response: Response<List<VendorServicesData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    val originalData = response.toList()
                    vendorServicesAdapter = VendorServicesAdapter(requireContext(),response)
                    vendorServicesAdapter.notifyDataSetChanged()
                    recyclerView.adapter = vendorServicesAdapter

                    vendorServicesAdapter.setOnJobClickListener(this)

                    searchBar.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            val filterData = originalData.filter { item ->
                                item.service_name.contains(p0.toString(),ignoreCase = true)
                            }

                            vendorServicesAdapter.updateData(filterData)
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<VendorServicesData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onJobClick(job: VendorServicesData) {
                val index = selectedServices.indexOf(job.serviceId)
                if (index == -1) {
                    selectedServices.add(job.serviceId)
                } else {
                    selectedServices.removeAt(index)
                }

                Log.e("services",selectedServices.toString())
//                servicesET.setText(selectedServices.toString())
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}