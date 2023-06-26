package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCommunityDetailsBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailsBinding
    lateinit var image:String
    lateinit var name:String
    lateinit var description:String
    lateinit var groupId: String
    lateinit var location :String
    private var isSwitchToCloseCommunity = true
    var isBusinessVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }



        val groupId = intent.getLongExtra("groupId",0)

        val grpID:String = groupId.toString()



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
            replaceFragment(CommunityMediaFragment())
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

        getCommunityDetails(grpID)
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
                    location = ""

                    val date = convertTimestampToFormattedDate(response.date_added)

                    binding.communityCreatedBy.text = "Created by ${response.creator_name} | $date"


                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToFormattedDate(timestamp: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

        try {
            val instant = Instant.parse(timestamp)
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))
            return zonedDateTime.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
        }

        return ""
    }
}