package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
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
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.ExpertDetailsAdapter
import app.retvens.rown.CreateCommunity.NormalUserDetailsAdapter
import app.retvens.rown.CreateCommunity.UserDetailsAdapter
import app.retvens.rown.CreateCommunity.VendorDetailsAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.FeedCollection.User
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllUsersActivity : AppCompatActivity() {

    private var ownerList:ArrayList<User> = ArrayList()
    private var vendorList:ArrayList<User> = ArrayList()
    private var userList:ArrayList<User> = ArrayList()
    private var expertList:ArrayList<User> = ArrayList()

    lateinit var expertDetailsAdapter: ExpertDetailsAdapter
    lateinit var userDetailsAdapter: UserDetailsAdapter
    lateinit var vendorDetailsAdapter: VendorDetailsAdapter
    lateinit var normalUserDetailsAdapter: NormalUserDetailsAdapter

    lateinit var progressDialog: Dialog
    lateinit var recycler: RecyclerView
    lateinit var topTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_users)

        findViewById<ImageButton>(R.id.community_backBtn).setOnClickListener { onBackPressed() }

        topTitle = findViewById(R.id.topTitle)
        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        val groupID = intent.getStringExtra("groupID")

        getCommunityDetails(groupID)

    }

    private fun getCommunityDetails(groupId: String?) {
        val user = intent.getStringExtra("user")
        topTitle.text = "$user"

        val getCommunities = RetrofitBuilder.feedsApi.getGroup(groupId!!)

        getCommunities.enqueue(object : Callback<GetCommunitiesData?>,
            UserDetailsAdapter.OnItemClickListener, VendorDetailsAdapter.OnItemClickListener,
            ExpertDetailsAdapter.OnItemClickListener, NormalUserDetailsAdapter.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetCommunitiesData?>,
                response: Response<GetCommunitiesData?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!

                    response.Members.forEach { member ->
                        when (member.Role) {
                            "Hotel Owner" -> ownerList.add(member)
                            "Business Vendor / Freelancer" -> vendorList.add(member)
                            "Normal User" -> userList.add(member)
                            "Hospitality Expert" -> expertList.add(member)
                        }
                    }

                    response.Admin.forEach { admin ->
                        when (admin.Role) {
                            "Hotel Owner" -> ownerList.add(admin)
                            "Business Vendor / Freelancer" -> vendorList.add(admin)
                            "Normal User" -> userList.add(admin)
                            "Hospitality Expert" -> expertList.add(admin)
                        }
                    }

                    if (user == "All businesses"){
                        userDetailsAdapter = UserDetailsAdapter(this@ViewAllUsersActivity, ownerList,response.Admin)
                        recycler.adapter = userDetailsAdapter
                        userDetailsAdapter.notifyDataSetChanged()
                        userDetailsAdapter.setOnItemClickListener(this)
                    } else if (user == "Vendors") {
                        vendorDetailsAdapter = VendorDetailsAdapter(this@ViewAllUsersActivity, vendorList,response.Admin)
                        recycler.adapter = vendorDetailsAdapter
                        vendorDetailsAdapter.notifyDataSetChanged()
                        vendorDetailsAdapter.setOnItemClickListener(this)
                    } else if (user == "Hoteliers") {
                        expertDetailsAdapter = ExpertDetailsAdapter(this@ViewAllUsersActivity, expertList,response.Admin)
                        recycler.adapter = expertDetailsAdapter
                        expertDetailsAdapter.notifyDataSetChanged()
                        expertDetailsAdapter.setOnItemClickListener(this)
                    } else if (user == "Others") {
                        normalUserDetailsAdapter = NormalUserDetailsAdapter(this@ViewAllUsersActivity, userList,response.Admin)
                        recycler.adapter = normalUserDetailsAdapter
                        normalUserDetailsAdapter.notifyDataSetChanged()
                        normalUserDetailsAdapter.setOnItemClickListener(this)
                    }


                } else{
                    Log.e("error",response.message().toString())
                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }

            override fun onItemClick(member: User,admin:String) {
                Log.e("1","working")
                openBottomSelectionCommunity(member.user_id,member.address)

            }
        })
    }

    private fun openBottomSelectionCommunity(userId:String,number:String) {
        val dialogLanguage = Dialog(this)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_remove_from_community)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()



        dialogLanguage.findViewById<TextView>(R.id.remove).setOnClickListener {
            openBottomSelectionCommunityRemove(dialogLanguage)
        }
        dialogLanguage.findViewById<TextView>(R.id.message).setOnClickListener {
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.view_profile).setOnClickListener {
            dialogLanguage.dismiss()
        }
    }

    private fun openBottomSelectionCommunityRemove(dialogL: Dialog) {
        val dialogLanguage = Dialog(this)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_remove_from_community_confermation)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        dialogLanguage.findViewById<TextView>(R.id.yes).setOnClickListener {
            dialogL.dismiss()
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.not).setOnClickListener {
            dialogLanguage.dismiss()
        }
    }

}