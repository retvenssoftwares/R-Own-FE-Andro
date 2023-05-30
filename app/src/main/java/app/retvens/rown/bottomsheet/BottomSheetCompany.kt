package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.location.CompanyAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetCompany : BottomSheetDialogFragment() {

    var mListener: OnBottomCompanyClickListener ? = null
    fun setOnCompanyClickListener(listener: OnBottomCompanyClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetCompany? {
        return BottomSheetCompany()
    }
    interface OnBottomCompanyClickListener{
        fun bottomLocationClick(CompanyFrBo : String)
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
        recyclerView.setHasFixedSize(true)

        val getCompany = RetrofitBuilder.profileCompletion.getCompany()

        getCompany.enqueue(object : Callback<List<CompanyDatacClass>?>, CompanyAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<CompanyDatacClass>?>,
                response: Response<List<CompanyDatacClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    val adapter = CompanyAdapter(requireContext(),response)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<CompanyDatacClass>?>, t: Throwable) {

            }

            override fun onStateDataClick(companyDatacClass: String) {
                dismiss()
                mListener?.bottomLocationClick(companyDatacClass)
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}