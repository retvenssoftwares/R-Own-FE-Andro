package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
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
    private var click:Boolean = false
    private var click1:Boolean = false
    private var click2:Boolean = false
    private var click3:Boolean = false



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

        recyclerViewOwner = view.findViewById(R.id.listOfOwners)
        recyclerViewOwner.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOwner.setHasFixedSize(true)

        business.setOnClickListener {
            click = !click
            if (click){
                recyclerViewOwner.visibility = View.VISIBLE
            }else{
                recyclerViewOwner.visibility = View.GONE
            }

        }


        recyclerViewVendor = view.findViewById(R.id.listOfVendors)
        recyclerViewVendor.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewVendor.setHasFixedSize(true)

        vendor.setOnClickListener {
            click1 = !click1
            if (click1){
                recyclerViewVendor.visibility = View.VISIBLE
            }else{
                recyclerViewVendor.visibility = View.GONE
            }

        }


        recylerViewUser = view.findViewById(R.id.listOfNormalUser)
        recylerViewUser.layoutManager = LinearLayoutManager(requireContext())
        recylerViewUser.setHasFixedSize(true)

        others.setOnClickListener {
            click2 = !click2
            if (click2){
                recylerViewUser.visibility = View.VISIBLE
            }else{
                recylerViewUser.visibility = View.GONE
            }
        }

        recyclerViewExpert = view.findViewById(R.id.listOfExpert)
        recyclerViewExpert.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExpert.setHasFixedSize(true)

        hoteliers.setOnClickListener {
            click3 = !click3
            if (click3){
                recyclerViewExpert.visibility = View.VISIBLE
            }else{
                recyclerViewExpert.visibility = View.GONE
            }
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

    private fun openBottomSelectionCommunity() {
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

        getCommunities.enqueue(object : Callback<GetCommunitiesData?> {
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

                    userDetailsAdapter = UserDetailsAdapter(requireContext(), ownerList)
                    recyclerViewOwner.adapter = userDetailsAdapter
                    userDetailsAdapter.notifyDataSetChanged()

                    vendorDetailsAdapter = VendorDetailsAdapter(requireContext(), vendorList)
                    recyclerViewVendor.adapter = vendorDetailsAdapter
                    vendorDetailsAdapter.notifyDataSetChanged()

                    normalUserDetailsAdapter = NormalUserDetailsAdapter(requireContext(), userList)
                    recylerViewUser.adapter = normalUserDetailsAdapter
                    normalUserDetailsAdapter.notifyDataSetChanged()

                    expertDetailsAdapter = ExpertDetailsAdapter(requireContext(), expertList)
                    recyclerViewExpert.adapter = expertDetailsAdapter
                    expertDetailsAdapter.notifyDataSetChanged()



                } else{
                    Log.e("error",response.message().toString())
                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

}