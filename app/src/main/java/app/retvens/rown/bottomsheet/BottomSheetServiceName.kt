package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
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
import app.retvens.rown.utils.setupFullHeight
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        fun bottomSNClick(serviceName : String)
    }

    companion object {
        const val SN_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var bottomServiceNameAdapter: BottomServiceNameAdapter

    private lateinit var vendorServicesAdapter: VendorServicesAdapter
    private  var selectedServices:ArrayList<String> = ArrayList()
    private  var selectedServicesName:ArrayList<String> = ArrayList()
    private var vendorId:String = ""
    private lateinit var searchBar: EditText

    lateinit var addService: CardView

    lateinit var progressDialog: Dialog

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_services, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = view.findViewById(R.id.search_country)

        recyclerView = view.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getBottomServices()
        addService = view.findViewById(R.id.addService)
        addService.setOnClickListener {
            mListener?.bottomSNClick(selectedServicesName.toString())
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()

            selectedServices.forEach {
                setServices(it)
            }
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
                progressDialog.dismiss()
                dismiss()
                }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded){
                    progressDialog.dismiss()
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
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
                if (selectedServices.contains(job.serviceId)){
                    selectedServicesName.remove(job.service_name)
                    selectedServices.remove(job.serviceId)
                } else {
                    selectedServices.add(job.serviceId)
                    selectedServicesName.add(job.service_name)
                }
                Log.e("services",selectedServices.toString())
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}