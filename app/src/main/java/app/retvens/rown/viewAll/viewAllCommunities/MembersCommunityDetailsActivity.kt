package app.retvens.rown.viewAll.viewAllCommunities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.AddMemberData
import app.retvens.rown.DataCollections.FeedCollection.AddUserDataClass
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.DataCollections.removeMember
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOpenCommunityDetailsBinding
import app.retvens.rown.viewAll.communityDetails.CommunityMediaFragment
import app.retvens.rown.viewAll.communityDetails.CommunityUsersFragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MembersCommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityOpenCommunityDetailsBinding
    private  var number:ArrayList<String> = ArrayList()
    private  var userId:ArrayList<String> = ArrayList()
    var isBusinessVisible = true
    private var latitude:Double = 0.0
    var longitude:Double = 0.0
    private lateinit var googleMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var progressDialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getLongExtra("groupId",0)
        val grpID:String = groupId.toString()

        val sharedPreferences1 = getSharedPreferences("savePhoneNo", AppCompatActivity.MODE_PRIVATE)
        val phone = sharedPreferences1?.getString("savePhoneNumber", "").toString()

        val sharedPreferences2 = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences2?.getString("user_id", "").toString()

        number.add(phone)
        userId.add(user_id)

        binding.switchToCommunity.text = "Leave Community"

        binding.joinCommunity.setOnClickListener {
                removeUser(user_id,grpID)
        }

        binding.lll6.visibility = View.VISIBLE
        binding.mapShow.visibility = View.VISIBLE

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



        binding.communityDetailBackBtn.setOnClickListener {
            onBackPressed()
        }

        getCommunityDetails(groupId.toString())
    }

    private fun removeUser(userId: String, groupId: String) {

        Log.e("userid",userId)
        Log.e("groupId",groupId)

        val remove = RetrofitBuilder.retrofitBuilder.removeMember(groupId, removeMember(userId))

        remove.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MembersCommunityDetailsActivity,DashBoardActivity::class.java))
                }else{
                    Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
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
                if (response.isSuccessful) {
                    val response = response.body()!!
                    Log.e("response", response.toString())
                    Glide.with(applicationContext).load(response.Profile_pic)
                        .into(binding.communityProfile)
                    binding.communityDetailName.text = response.group_name
                    binding.communityDetailMembers.text =
                        "${response.Totalmember.toString()} members"
                    binding.communityDescription.text = response.description
                    latitude = response.latitude.toDouble()
                    longitude = response.longitude.toDouble()
                    val date = convertTimestampToFormattedDate(response.date_added)

                    binding.communityCreatedBy.text = "Created by ${response.creator_name} | $date"


                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {

            }
        })

        mMapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_show, mMapFragment)
            .commit()

        mMapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                googleMap = p0
                val location = LatLng( latitude, longitude)
                Log.e("lat",latitude.toString())
                Log.e("long",longitude.toString())
                googleMap.addMarker(MarkerOptions().position(location).title("location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
            }
         })
       }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.community_fragment_container,fragment)
            transaction.commit()
        }
      }

    private fun addMembers() {


// Remove any non-digit characters from each phone number
        val cleanedNumbers = number!!.map { it.replace(Regex("[^\\d]"), "+") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")

        val groupId = intent.getLongExtra("groupId",0)


        val data = AddMemberData(groupId.toString(), formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.result.toString(), Toast.LENGTH_SHORT).show()
                    addCommunityMember()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addCommunityMember() {
        val members = userId
        val groupId = intent.getLongExtra("groupId",0)
        for (x in members!!) {

            val data = AddUserDataClass(x)

            val send = RetrofitBuilder.feedsApi.addUser(groupId.toString(), data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
//                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT)
//                        .show()
                        val intent = Intent(applicationContext, MesiboMessagingActivity::class.java)
                        intent.putExtra(MesiboUI.GROUP_ID, groupId.toLong())
                        startActivity(intent)
                        progressDialog.dismiss()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })

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