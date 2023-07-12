package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.ExpertDetailsAdapter
import app.retvens.rown.CreateCommunity.NormalUserDetailsAdapter
import app.retvens.rown.CreateCommunity.UserDetailsAdapter
import app.retvens.rown.CreateCommunity.VendorDetailsAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.FeedCollection.Member
import app.retvens.rown.DataCollections.FeedCollection.User
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityUsersFragment(val groupID:String) : Fragment() {

    lateinit var recyclerViewOwner: RecyclerView
    lateinit var recyclerViewVendor: RecyclerView
    lateinit var recylerViewUser:RecyclerView
    lateinit var recyclerViewExpert: RecyclerView
    lateinit var expertDetailsAdapter: ExpertDetailsAdapter
    lateinit var userDetailsAdapter: UserDetailsAdapter
    lateinit var vendorDetailsAdapter: VendorDetailsAdapter
    lateinit var normalUserDetailsAdapter: NormalUserDetailsAdapter
    private var ownerList:ArrayList<User> = ArrayList()
    private var vendorList:ArrayList<User> = ArrayList()
    private var userList:ArrayList<User> = ArrayList()
    private var expertList:ArrayList<User> = ArrayList()
    lateinit var progressDialog: Dialog

    lateinit var viewAllOwner : CardView
    lateinit var viewAllVendors : CardView
    lateinit var viewAllHoteliers : CardView
    lateinit var viewAllOthers : CardView

    lateinit var ownerLL : LinearLayout
    lateinit var vendorLL : LinearLayout
    lateinit var hoteliersLL : LinearLayout
    lateinit var othersLL : LinearLayout

    private var click:Boolean = true
    private var click1:Boolean = true
    private var click2:Boolean = true
    private var click3:Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        progressDialog = Dialog(requireContext())
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        val business = view.findViewById<CardView>(R.id.business)
        val vendor = view.findViewById<CardView>(R.id.vendors)
        val others = view.findViewById<CardView>(R.id.others)
        val hoteliers = view.findViewById<CardView>(R.id.hoteliers)

        viewAllOwner = view.findViewById(R.id.viewAllOwner)
        viewAllVendors = view.findViewById(R.id.viewAllVendors)
        viewAllHoteliers = view.findViewById(R.id.viewAllHoteliers)
        viewAllOthers = view.findViewById(R.id.viewAllOthers)

        ownerLL = view.findViewById(R.id.ownerLL)
        vendorLL = view.findViewById(R.id.vendorLL)
        hoteliersLL = view.findViewById(R.id.hoteliersLL)
        othersLL = view.findViewById(R.id.othersLL)

        viewAllOwner.setOnClickListener {
            val intent = Intent(requireContext(), ViewAllUsersActivity::class.java)
            intent.putExtra("groupID", groupID)
            intent.putExtra("user", "All businesses")
            startActivity(intent)
        }
        viewAllVendors.setOnClickListener {
            val intent = Intent(requireContext(), ViewAllUsersActivity::class.java)
            intent.putExtra("groupID", groupID)
            intent.putExtra("user", "Vendors")
            startActivity(intent)
        }
        viewAllHoteliers.setOnClickListener {
            val intent = Intent(requireContext(), ViewAllUsersActivity::class.java)
            intent.putExtra("groupID", groupID)
            intent.putExtra("user", "Hoteliers")
            startActivity(intent)
        }
        viewAllOthers.setOnClickListener {
            val intent = Intent(requireContext(), ViewAllUsersActivity::class.java)
            intent.putExtra("groupID", groupID)
            intent.putExtra("user", "Others")
            startActivity(intent)
        }

        recyclerViewOwner = view.findViewById(R.id.listOfOwners)
        recyclerViewOwner.layoutManager = LinearLayoutManager(requireContext())
//        recyclerViewOwner.setHasFixedSize(true)
        recyclerViewOwner.setItemViewCacheSize(3)

        business.setOnClickListener {
            if (click){
                ownerLL.visibility = View.VISIBLE
            }else{
                ownerLL.visibility = View.GONE
            }
            click = !click
        }


        recyclerViewVendor = view.findViewById(R.id.listOfVendors)
        recyclerViewVendor.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewVendor.setItemViewCacheSize(3)

        vendor.setOnClickListener {
            if (click1){
                vendorLL.visibility = View.VISIBLE
            }else{
                vendorLL.visibility = View.GONE
            }
            click1 = !click1
        }


        recylerViewUser = view.findViewById(R.id.listOfNormalUser)
        recylerViewUser.layoutManager = LinearLayoutManager(requireContext())
        recylerViewUser.setHasFixedSize(true)

        others.setOnClickListener {
            if (click2){
                othersLL.visibility = View.VISIBLE
            }else{
                othersLL.visibility = View.GONE
            }
            click2 = !click2
        }

        recyclerViewExpert = view.findViewById(R.id.listOfExpert)
        recyclerViewExpert.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExpert.setHasFixedSize(true)

        hoteliers.setOnClickListener {
            if (click3){
                hoteliersLL.visibility = View.VISIBLE
            }else{
                hoteliersLL.visibility = View.GONE
            }
            click3 = !click3
        }

        getCommunityDetails(groupID)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({

            progressDialog.dismiss()

            if (ownerList.size == 0){
                Log.e("owner",ownerList.size.toString())
                business.visibility = View.GONE
            }
            if (vendorList.size == 0){
                Log.e("vendor",vendorList.size.toString())
                vendor.visibility = View.GONE
            }
            if (expertList.size == 0){
                Log.e("expert",expertList.size.toString())
                hoteliers.visibility = View.GONE
            }
            if (userList.size == 0){
                Log.e("user",userList.size.toString())
                others.visibility = View.GONE
            }
        },1000)


    }

    private fun openBottomSelectionCommunity(userId:String,number:String) {
        val dialogLanguage = Dialog(requireContext())
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_remove_from_community)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
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
        val dialogLanguage = Dialog(requireContext())
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

    private fun getCommunityDetails(groupId: String?) {

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

                    if (ownerList.size > 2){
                        viewAllOwner.visibility = View.VISIBLE
                    }
                    if (vendorList.size > 2){
                        viewAllVendors.visibility = View.VISIBLE
                    }
                    if (expertList.size > 2){
                        viewAllHoteliers.visibility = View.VISIBLE
                    }
                    if (userList.size > 2){
                        viewAllOthers.visibility = View.VISIBLE
                    }
                    userDetailsAdapter = UserDetailsAdapter(requireContext(), ownerList)
                    recyclerViewOwner.adapter = userDetailsAdapter
                    userDetailsAdapter.reduceList()
                    userDetailsAdapter.notifyDataSetChanged()

                    vendorDetailsAdapter = VendorDetailsAdapter(requireContext(), vendorList)
                    recyclerViewVendor.adapter = vendorDetailsAdapter
                    vendorDetailsAdapter.reduceList()
                    vendorDetailsAdapter.notifyDataSetChanged()

                    normalUserDetailsAdapter = NormalUserDetailsAdapter(requireContext(), userList)
                    recylerViewUser.adapter = normalUserDetailsAdapter
                    normalUserDetailsAdapter.reduceList()
                    normalUserDetailsAdapter.notifyDataSetChanged()

                    expertDetailsAdapter = ExpertDetailsAdapter(requireContext(), expertList)
                    recyclerViewExpert.adapter = expertDetailsAdapter
                    expertDetailsAdapter.reduceList()
                    expertDetailsAdapter.notifyDataSetChanged()


                    userDetailsAdapter.setOnItemClickListener(this)
                    vendorDetailsAdapter.setOnItemClickListener(this)
                    expertDetailsAdapter.setOnItemClickListener(this)
                    normalUserDetailsAdapter.setOnItemClickListener(this)

                } else{
                    Log.e("error",response.message().toString())
                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }

            override fun onItemClick(member: User) {
                Log.e("1","working")
                openBottomSelectionCommunity(member.user_id,member.address)

            }


        })

    }

}