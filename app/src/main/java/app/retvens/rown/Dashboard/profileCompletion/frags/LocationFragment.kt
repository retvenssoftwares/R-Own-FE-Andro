package app.retvens.rown.Dashboard.profileCompletion.frags

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var progressDialog : Dialog
    lateinit var task:CardView
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

        task = view.findViewById<CardView>(R.id.autofetch)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsClient = LocationServices.getSettingsClient(requireContext())

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        task.setOnClickListener {
            checkLocationSettings()
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

        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(profile)
        }else {
            profile.setImageResource(R.drawable.svg_user)
        }
        name.setText("Hi $profileName")
    }

    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location services enabled, proceed to fetch location
            fetchLocation()
            Log.e("click","1")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("error",sendEx.toString())
                }
            }
        }
    }

    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            Log.e("click","2")
        } else {
            Log.e("click","3")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.e("latitude",latitude.toString())
                    Log.e("longitude",longitude.toString())

                    reverseGeocode(latitude, longitude)
                    Log.e("click","4")
                }else{
                    Log.e("click","5")
                }
            }.addOnFailureListener { exception: Exception ->
                Log.e("error",exception.toString())
                Toast.makeText(requireContext(),"Permission Required",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reverseGeocode(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.Main) {
            val geocoder = Geocoder(requireContext())
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.locality
                    val state = address.adminArea
                    val country = address.countryName

                    etLocationCountry.setText("$city,$state,$country")
                }
            } catch (e: Exception) {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                // Location services enabled, proceed to fetch location
                fetchLocation()
            } else {
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
            }
        }
    }


    private fun setLocation(context: Context?) {

        val location = etLocationCountry.text.toString()

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        val update = RetrofitBuilder.profileCompletion.setLocation(user_id,location)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    progressDialog.dismiss()
                    profileComStatus(context!!, "70")
                    profileCompletionStatus = "70"
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

    override fun selectlocation(latitude: String, longitude: String) {
        TODO("Not yet implemented")
    }

}