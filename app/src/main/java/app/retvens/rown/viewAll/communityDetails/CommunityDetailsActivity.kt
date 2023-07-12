package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.FilterSelectedMembers
import app.retvens.rown.CreateCommunity.SelectMembers
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.DeleteCommunityDataClass
import app.retvens.rown.DataCollections.FeedCollection.Admin
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCommunityDetailsBinding
import app.retvens.rown.viewAll.viewAllCommunities.OpenCommunityDetailsActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.ArrayList

class CommunityDetailsActivity : AppCompatActivity(){
    lateinit var binding: ActivityCommunityDetailsBinding
    lateinit var image:String
    lateinit var name:String
    lateinit var description:String
    lateinit var groupId: String
    lateinit var location :String
    private var latitude:Double = 0.0
    var longitude:Double = 0.0
    private  var members:ArrayList<String> = ArrayList()
    private var admin:ArrayList<String> = ArrayList()
    private lateinit var googleMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private var isSwitchToCloseCommunity = true
    var isBusinessVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }


        val groupId = intent.getLongExtra("groupId",0)
        val grpID:String = groupId.toString()

        val sharedPreferences1 =getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()



        Log.e("top","not okkk")

        binding.communityDetailAddBtn.setOnClickListener {
            val intent = Intent(this,FilterSelectedMembers::class.java)
            intent.putStringArrayListExtra("members",members)
            intent.putExtra("groupId",grpID)
            startActivity(intent)
        }

        binding.communityDetailDeleteBtn.setOnClickListener {

            openBottomSelectionCommunityRemove(grpID)

        }

        binding.communityDetailEditBtn.setOnClickListener {
            val intent = Intent(this, CommunityEditActivity::class.java)
            intent.putExtra("image", image)
            intent.putExtra("name", name)
            intent.putExtra("desc",description)
            intent.putExtra("groupId",grpID)
            intent.putExtra("location",location)
            startActivity(intent)
        }

        replaceFragment(CommunityUsersFragment(grpID))

        binding.usersText.setOnClickListener {
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            replaceFragment(CommunityUsersFragment(grpID))
        }
        binding.mediaText.setOnClickListener {
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            replaceFragment(CommunityMediaFragment(grpID))
        }
        binding.switchToCommunity.setOnClickListener {

            val dialogL = Dialog(this)
            dialogL.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogL.setContentView(R.layout.switch_to_community_dialog)
            dialogL.setCancelable(true)

            dialogL.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialogL.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogL.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogL.window?.setGravity(Gravity.BOTTOM)
            dialogL.show()

            if (isSwitchToCloseCommunity){
                val yes = dialogL.findViewById<TextView>(R.id.text_yes)
                yes.setOnClickListener {
                    binding.switchToCommunity.text = "Switch to Open Community"
                    isSwitchToCloseCommunity = false
                    dialogL.dismiss()
                }
                val no = dialogL.findViewById<TextView>(R.id.text_no)
                no.setOnClickListener {
                    dialogL.dismiss()
                }
            } else {
                dialogL.findViewById<TextView>(R.id.ttt).text = "Anyone can request to join your community, do you really want to proceed ?"
                val yes = dialogL.findViewById<TextView>(R.id.text_yes)
                yes.setOnClickListener {
                    binding.switchToCommunity.text = "Switch to Close Community"
                    isSwitchToCloseCommunity = true
                    dialogL.dismiss()
                }
                val no = dialogL.findViewById<TextView>(R.id.text_no)
                no.text = "No, Keep it Closed"
                no.setOnClickListener {
                    dialogL.dismiss()
                }
            }
        }

//        mMapFragment = supportFragmentManager.findFragmentById(R.id.map_show) as SupportMapFragment
//        mMapFragment.getMapAsync(OnMapReadyCallback {
//            googleMap = it
//        })

        mMapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_show, mMapFragment)
            .commit()

        mMapFragment.getMapAsync(object : OnMapReadyCallback{
            override fun onMapReady(p0: GoogleMap) {

                googleMap = p0
                val location = LatLng( latitude, longitude)
                googleMap.addMarker(MarkerOptions().position(location).title("location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
            }

        })


        getCommunityDetails(grpID)


    }

    private fun deleteCommunity(grpID: String) {

        val delete = RetrofitBuilder.feedsApi.deleteCommunity(DeleteCommunityDataClass(grpID))

        delete.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
            Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getCommunityDetails(groupId: String?) {

        val getCommunities = RetrofitBuilder.feedsApi.getGroup(groupId!!)

        getCommunities.enqueue(object : Callback<GetCommunitiesData?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetCommunitiesData?>,
                response: Response<GetCommunitiesData?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("response",response.toString())
                    Glide.with(applicationContext).load(response.Profile_pic).into(binding.communityProfile)
                    binding.communityDetailName.text = response.group_name
                    binding.communityDetailMembers.text = "${response.Totalmember.toString()} members"
                    binding.communityDescription.text = response.description

                    image = response.Profile_pic
                    name = response.group_name
                    description = response.description
                    location = response.location
                    latitude = response.latitude.toDouble()
                    longitude = response.longitude.toDouble()

                    response.Members.forEach {
                        members.add(it.user_id)
                    }

                    val date = convertTimestampToFormattedDate(response.date_added)

                    binding.communityCreatedBy.text = "Created by ${response.creator_name} | $date"

                    response.Admin.forEach {
                        admin.add(it.user_id)
                    }

                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {

            }
        })

    }

    private fun openBottomSelectionCommunityRemove(groupId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_delete_group)
        bottomSheetDialog.setCancelable(true)

        // Set dialog window animations
        bottomSheetDialog.window?.attributes?.windowAnimations = R.style.DailogAnimation

        // Handle "Yes" button click
        bottomSheetDialog.findViewById<TextView>(R.id.yes)?.setOnClickListener {
            bottomSheetDialog.dismiss()
            deleteCommunity(groupId)
        }

        // Handle "No" button click
        bottomSheetDialog.findViewById<TextView>(R.id.not)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.community_fragment_container,fragment)
            transaction.commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToFormattedDate(timestamp: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

        try {
            val localDateTime = LocalDateTime.parse(timestamp, inputFormatter)
            return localDateTime.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
        }

        return ""
    }


}