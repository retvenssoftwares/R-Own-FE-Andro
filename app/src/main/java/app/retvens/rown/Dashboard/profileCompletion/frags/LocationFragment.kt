package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.utils.saveProgress
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocationFragment : Fragment(), BackHandler, BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    private lateinit var etLocationCountry : TextInputEditText
    lateinit var etLocationState : TextInputEditText
    lateinit var userLocationCountry : TextInputLayout
    private lateinit var userLocationState : TextInputLayout
    lateinit var userLocationCity : TextInputLayout
    private lateinit var etLocationCity : TextInputEditText
    private lateinit var locationFragmentAdapter: LocationFragmentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialogRole:Dialog
    private lateinit var profile:ShapeableImageView
    private lateinit var name:TextView

    lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userLocationCountry = view.findViewById(R.id.user_location_country)
        userLocationState = view.findViewById(R.id.user_location_state)
        userLocationCity = view.findViewById(R.id.user_location_city)
        etLocationCountry = view.findViewById(R.id.et_location_country)
        etLocationState = view.findViewById(R.id.et_location_state)
        etLocationCity = view.findViewById(R.id.et_location_city)

        etLocationCountry.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }
        etLocationState.setOnClickListener {
//            openStateLocationSheet()
        }

        view.findViewById<CardView>(R.id.card_location_next).setOnClickListener {
            if(etLocationCountry.text.toString() == "Select Your Location"){
                userLocationCountry.error = "Select Your Location"
            } else {
                userLocationCountry.isErrorEnabled = false
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog.show()
                setLocation(context)
            }
        }
        profile = view.findViewById(R.id.user_complete_profile1)
        name = view.findViewById(R.id.user_complete_name)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        Glide.with(requireContext()).load(profilePic).into(profile)
        name.setText("Hi $profileName")
    }


    private fun setLocation(context: Context?) {

        val country = etLocationCountry.text.toString()
        val state = etLocationState.text.toString()
        val city = etLocationCity.text.toString()
        val location = "$city, $state, $country"

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        val update = RetrofitBuilder.profileCompletion.setLocation(user_id,location)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val onboardingPrefs = requireContext().getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
                    val editor = onboardingPrefs.edit()
                    editor.putBoolean("LocationFragment", false)
                    editor.apply()

                    progressDialog.dismiss()
                    val response = response.body()!!
//                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                    saveProgress(requireContext(), "70")
                    val fragment = BasicInformationFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()

                }else{
                    if (isAdded) {
                        progressDialog.dismiss()
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, t.localizedMessage!!.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }
        })


    }

    private fun openStateLocationSheet() {

        dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_location)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()
        getUserLocation()

        recyclerView = dialogRole.findViewById(R.id.location_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun getUserLocation() {

        val getLocation = RetrofitBuilder.profileCompletion.getCountries()

        getLocation.enqueue(object : Callback<List<CountryData>?>,
            LocationFragmentAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<CountryData>?>,
                response: Response<List<CountryData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    locationFragmentAdapter = LocationFragmentAdapter(requireContext(),response)
                    locationFragmentAdapter.notifyDataSetChanged()
                    recyclerView.adapter = locationFragmentAdapter
                    locationFragmentAdapter.setOnLocationClickListener(this)
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CountryData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onCountryClick(country: String, code: String) {
                etLocationState.setText(country)
                dialogRole.dismiss()
                userLocationCity.visibility = View.VISIBLE
            }
            /*

                        override fun onLocationClick(job: LocationDataClass) {
                           for (x in job.states){
                               for (y in x.name){
                                   etLocationCountry.setText(y.toString())
                               }
                           }
                            dialogRole.dismiss()
                        }
            */

        })

    }

    override fun handleBackPressed(): Boolean {

        val fragment = UsernameFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        etLocationCountry.setText(CountryStateCityFrBo)
    }

}