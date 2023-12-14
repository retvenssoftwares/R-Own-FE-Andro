package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.JobsCollection.HotelsList
import app.retvens.rown.DataCollections.ProfileCompletion.AddCompanyDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.location.CompanyAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetCompany : BottomSheetDialogFragment() {

    var mListener: OnBottomCompanyClickListener ? = null
    private lateinit var company:String
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
    lateinit var searchBar:EditText
    private lateinit var AddHotel:CardView

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

        AddHotel = view.findViewById<CardView>(R.id.addHotelCard)

        recyclerView = view.findViewById(R.id.company_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //recyclerView. //recyclerView.setHasFixedSize(true)

        AddHotel.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_addcompany)

            val name = dialog.findViewById<TextInputEditText>(R.id.companyName)
            val add = dialog.findViewById<Button>(R.id.AddBtn)

            dialog.window?.run {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.dialog_height))
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.windowAnimations = R.style.DailogAnimation
                setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            }

            add.setOnClickListener {
                val company = name.text.toString().trim()
                if (company.isEmpty()) {
                    name.error = "Enter Company Name"
                } else {
                    addCompany(company)
                    dialog.dismiss()
                }
            }

            dialog.show()
        }


        searchBar = view.findViewById(R.id.search_companyn)

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
                    adapter.setOnLocationClickListener(this)
                    val originalData = response.toList()
                    searchBar.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            // Ensure that originalData and adapter are not null before proceeding
                            if (originalData == null || adapter == null) {
                                return
                            }

                            val searchText = p0.toString().trim()
                            val filterData = originalData.filter { item ->
                                item.company_name != null && item.company_name.contains(searchText, ignoreCase = true)
                            }

                            if (filterData.isEmpty()) {
                                AddHotel.visibility = View.VISIBLE
                                adapter.updateData(emptyList())
                            } else {
                                AddHotel.visibility = View.INVISIBLE
                                adapter.updateData(filterData)
                            }
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })

                }
            }

            override fun onFailure(call: Call<List<CompanyDatacClass>?>, t: Throwable) {

            }

            override fun onStateDataClick(companyDatacClass: CompanyDatacClass) {
                mListener?.bottomLocationClick(companyDatacClass.company_name)
                dismiss()
            }


        })



    }

    private fun addCompany(name:String) {

        val addHotel = RetrofitBuilder.profileCompletion.addHotel(AddCompanyDataClass(name,"true"))

        addHotel.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    mListener?.bottomLocationClick(name)
                }else{

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

}