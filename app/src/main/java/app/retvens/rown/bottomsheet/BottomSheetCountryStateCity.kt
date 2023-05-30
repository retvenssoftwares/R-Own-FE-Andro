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
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.location.CityAdapter
import app.retvens.rown.DataCollections.location.CityData
import app.retvens.rown.DataCollections.location.StateAdapter
import app.retvens.rown.DataCollections.location.StateData
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetCountryStateCity : BottomSheetDialogFragment(),
    BottomSheetLocation.OnBottomLocationClickListener {

    var mListener: OnBottomCountryStateCityClickListener ? = null
    fun setOnCountryStateCityClickListener(listener: OnBottomCountryStateCityClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetCountryStateCity? {
        return BottomSheetCountryStateCity()
    }
    interface OnBottomCountryStateCityClickListener{
        fun bottomCountryStateCityClick(CountryStateCityFrBo : String)
    }

    companion object {
        const val CountryStateCity_TAG = "BottomSheetDailog"
    }

    lateinit var recyclerView : RecyclerView
    lateinit var locationFragmentAdapter : StateAdapter
    lateinit var locationCityAdapter : CityAdapter

    lateinit var numericCode : String
    lateinit var cCode : String

    lateinit var locationStateLayout : TextInputLayout
    lateinit var locationCityLayout : TextInputLayout

    lateinit var locationCountryET : TextInputEditText
    lateinit var locationStateET : TextInputEditText
    lateinit var locationCityET : TextInputEditText

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_country_state_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationStateLayout = view.findViewById(R.id.bottom_location_state)
        locationCityLayout = view.findViewById(R.id.bottom_location_city)

        locationCountryET = view.findViewById(R.id.et_location_country_bottom)
        locationStateET = view.findViewById(R.id.et_location_state_bottom)
        locationCityET = view.findViewById(R.id.et_location_city_bottom)

        locationCountryET.setOnClickListener {
            val bottomSheet = BottomSheetLocation()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            bottomSheet.setOnLocationClickListener(this)
        }

        locationStateET.setOnClickListener {
            openBottomSheet("state")
        }

        locationCityET.setOnClickListener {
            openBottomSheet("city")
        }

        view.findViewById<CardView>(R.id.card_location_done).setOnClickListener {
            val country = locationCountryET.text.toString()
            val state = locationStateET.text.toString()
            val city = locationCityET.text.toString()
            val location = "$city,$state,$country"



            mListener?.bottomCountryStateCityClick(location)
            dismiss()
        }
    }

    private fun openBottomSheet(s:String) {
        val dialogLanguage = Dialog(requireContext())
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_sheet_location)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()
        val recyclerViewD = dialogLanguage.findViewById<RecyclerView>(R.id.location_recycler)
        val search = dialogLanguage.findViewById<EditText>(R.id.search_country)
        recyclerViewD.setHasFixedSize(true)
        recyclerViewD.layoutManager = LinearLayoutManager(requireContext())
        if (s=="state") {
            getUserState(recyclerViewD, dialogLanguage, search)
        } else {
            getUserCity(recyclerViewD, dialogLanguage, search)
        }
    }
    private fun getUserState(
        recyclerViewD: RecyclerView,
        dialogLanguage: Dialog,
        search: EditText
    ) {


        val getLocation = RetrofitBuilder.profileCompletion.getStates(numericCode)

        getLocation.enqueue(object : Callback<List<StateData>?>,
            StateAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<StateData>?>,
                response: Response<List<StateData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    locationFragmentAdapter = StateAdapter(requireContext(),response)
                    locationFragmentAdapter.notifyDataSetChanged()
                    recyclerViewD.adapter = locationFragmentAdapter
                    locationFragmentAdapter.setOnLocationClickListener(this)

                    search.addTextChangedListener(object : TextWatcher {
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
                                searchUser.name.contains(s.toString(),ignoreCase = true)
                            }
                            locationFragmentAdapter.searchLocation(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(), "No States Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StateData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onStateDataClick(StateData: String, code: String) {
                locationStateET.setText(StateData)
                cCode = code
                locationCityLayout.visibility = View.VISIBLE
                dialogLanguage.dismiss()
            }
        })
    }

    private fun getUserCity(
        recyclerViewD: RecyclerView,
        dialogLanguage: Dialog,
        search: EditText
    ) {

        val getLocation = RetrofitBuilder.profileCompletion.getCities(numericCode,cCode)

        getLocation.enqueue(object : Callback<List<CityData>?>,
            CityAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<CityData>?>,
                response: Response<List<CityData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    locationCityAdapter = CityAdapter(requireContext(),response)
                    locationCityAdapter.notifyDataSetChanged()
                    recyclerViewD.adapter = locationCityAdapter
                    locationCityAdapter.setOnLocationClickListener(this)

                    search.addTextChangedListener(object : TextWatcher {
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
                                searchUser.name.contains(s.toString(), true)
                            }
                            locationCityAdapter.searchLocation(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(), "Reload : ${ response.code().toString() }", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CityData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onStateDataClick(cityData: String) {
                locationCityET.setText(cityData)
                dialogLanguage.dismiss()
            }

        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun bottomLocationClick(LocationFrBo: String, NumericCodeFrBo : String) {
        locationCountryET.setText(LocationFrBo)
        numericCode = NumericCodeFrBo
        Log.d("codeNumeric", NumericCodeFrBo)
        locationStateLayout.visibility = View.VISIBLE
    }

}