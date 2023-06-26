package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.UserDetailsAdapter
import app.retvens.rown.CreateCommunity.VendorDetailsAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.FeedCollection.Member
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityUsersFragment(val groupID:String) : Fragment() {

    lateinit var recyclerViewOwner: RecyclerView
    lateinit var recyclerViewVendor: RecyclerView
    lateinit var userDetailsAdapter: UserDetailsAdapter
    lateinit var vendorDetailsAdapter: VendorDetailsAdapter
    private var ownerList:ArrayList<Member> = ArrayList()
    private var vendorList:ArrayList<Member> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val business = view.findViewById<CardView>(R.id.business)
        val vendor = view.findViewById<CardView>(R.id.vendors)
        recyclerViewOwner = view.findViewById(R.id.listOfOwners)

        recyclerViewOwner.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOwner.setHasFixedSize(true)

        business.setOnClickListener {
            recyclerViewOwner.visibility = View.VISIBLE

        }

        vendor.setOnClickListener {
            recyclerViewVendor.visibility = View.VISIBLE
        }

        recyclerViewVendor = view.findViewById(R.id.listOfVendors)
        recyclerViewVendor.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewVendor.setHasFixedSize(true)

        getCommunityDetails(groupID)

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
                if (response.isSuccessful){
                    val response = response.body()!!

                    response.Members.forEach {
                        if (it.Role == "Hotel Owner"){
                           ownerList.add(it)
                        }else if (it.Role == "Business Vendor / Freelancer"){
                            vendorList.add(it)
                        }


                        userDetailsAdapter = UserDetailsAdapter(requireContext(),ownerList)
                        recyclerViewOwner.adapter = userDetailsAdapter
                        userDetailsAdapter.notifyDataSetChanged()

                        vendorDetailsAdapter = VendorDetailsAdapter(requireContext(),vendorList)
                        recyclerViewVendor.adapter = vendorDetailsAdapter
                        vendorDetailsAdapter.notifyDataSetChanged()
                    }


                }else{
                    Log.e("error",response.message().toString())
                }

            }

            override fun onFailure(call: Call<GetCommunitiesData?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

}