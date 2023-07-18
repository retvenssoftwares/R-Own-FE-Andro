package app.retvens.rown.CreateCommunity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.databinding.ActivityCreateCommunityBinding
import app.retvens.rown.databinding.ActivityCreateCummVisibilitySettingBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCummVisibilitySetting : AppCompatActivity(), BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener {

    lateinit var binding : ActivityCreateCummVisibilitySettingBinding
    lateinit var latitudes: String
    lateinit var longitudes: String
    private val REQUEST_CHECK_SETTINGS = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    lateinit var task: ImageView
    var selectedLayout : Int = 1
    private  var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCummVisibilitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")

        binding.closedCummLayout.setOnClickListener {

            type = "close"

            selectedLayout = 1
            binding.layVisibility.visibility = View.VISIBLE

            binding.closedCummHintText.visibility = View.VISIBLE
            binding.openHintText.visibility = View.GONE
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        task = findViewById(R.id.autofetch)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        task.setOnClickListener {
            checkLocationSettings()
        }

        binding.openCummLayout.setOnClickListener {

            type = "Open Community"
            selectedLayout = 2
            binding.layVisibility.visibility = View.VISIBLE

            binding.openHintText.visibility = View.VISIBLE
            binding.closedCummHintText.visibility = View.GONE
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.etLocation.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.nextCumm.setOnClickListener {
            if (binding.etLocation.text.toString() == "Select Your Location"){
                binding.userLocationCountry.error = "Select Your Location"
            } else {
                val intent = Intent(this, SelectMembers::class.java)
                intent.putExtra("type", type)
                intent.putExtra("name", name)
                intent.putExtra("desc", description)
                intent.putExtra("location", binding.etLocation.text.toString())
                intent.putExtra("longitude", longitudes)
                intent.putExtra("latitude", latitudes)
                startActivity(intent)
                Log.e("location",binding.etLocation.text.toString())
            }
        }
    }

    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location services enabled, proceed to fetch location
            fetchLocation()
            Log.e("click","1")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("error",sendEx.toString())
                }
            }
        }
    }

    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
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
                    latitudes = latitude.toString()
                    longitudes = longitude.toString()
                    Log.e("latitude",latitude.toString())
                    Log.e("longitude",longitude.toString())

                    reverseGeocode(latitude, longitude)
                    Log.e("click","4")
                }else{
                    Log.e("click","5")
                }
            }.addOnFailureListener { exception: Exception ->
                Log.e("error",exception.toString())
                Toast.makeText(applicationContext,"Permission Required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reverseGeocode(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.Main) {
            val geocoder = Geocoder(this@CreateCummVisibilitySetting)
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.locality
                    val state = address.adminArea
                    val country = address.countryName

                    binding.etLocation.setText("$city,$state,$country")
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

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocation.setText(CountryStateCityFrBo)

    }

    override fun selectlocation(latitude: String, longitude: String) {
        latitudes = longitude
        longitudes = longitude
    }
}