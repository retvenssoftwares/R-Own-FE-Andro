package app.retvens.rown.bottomsheet

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.JobsCollection.HotelsDataClass
import app.retvens.rown.DataCollections.JobsCollection.HotelsList
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.location.CompanyAdapter
import app.retvens.rown.DataCollections.location.HotelsAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
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
    lateinit var searchBar: EditText
    private lateinit var hotelsAdapter: HotelsAdapter
    private lateinit var AddHotel: CardView


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
        searchBar = view.findViewById(R.id.search_companyn)
        AddHotel = view.findViewById(R.id.addHotelCard)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         //recyclerView. //recyclerView.setHasFixedSize(true)

        AddHotel.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_addjob)

            val name = dialog.findViewById<TextInputEditText>(R.id.companyName)
            val add = dialog.findViewById<Button>(R.id.AddBtn)

            dialog.window?.run {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.dialog_height))
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.windowAnimations = R.style.DailogAnimation
                setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            }

            add.setOnClickListener {
                val job = name.text.toString().trim()
                if (job.isEmpty()) {
                    name.error = "Enter Company Name"
                } else {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        Log.d("dfghjklghjkl", "onViewCreated: "+user_id)

        val getCompany = RetrofitBuilder.jobsApis.getHotelList(user_id)

        getCompany.enqueue(object : Callback<List<HotelsList>?>,
            HotelsAdapter.OnLocationClickListener {
            override fun onResponse(call: Call<List<HotelsList>?>, response: Response<List<HotelsList>?>
            ) {
                if (response.isSuccessful){

                    Log.d("sucesssss", "onResponse: "+response.body())

                    val response = response.body()!!
                    val originalData = response.toList()
                    hotelsAdapter = HotelsAdapter(requireContext(),response)
                    hotelsAdapter.notifyDataSetChanged()
                    recyclerView.adapter = hotelsAdapter


                    hotelsAdapter?.setOnLocationClickListener(this)

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
                                item.companyName.contains(p0.toString(),ignoreCase = true)
                            }

                            if (filterData!!.isEmpty()) {
                                AddHotel.visibility = View.VISIBLE
                                hotelsAdapter.updateData(emptyList())
                            } else {
                                AddHotel.visibility = View.INVISIBLE
                                hotelsAdapter.updateData(filterData)
                            }

                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }
                    })
                 }
              }

            override fun onFailure(call: Call<List<HotelsList>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("fail", "onFailure: "+t.message)

            }

            override fun onStateDataClick(hotelsList: HotelsList) {
                mListener?.bottomLocationClick(hotelsList.companyName,hotelsList.companyId)
                dismiss()
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}