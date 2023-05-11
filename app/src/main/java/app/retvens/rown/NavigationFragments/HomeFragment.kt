package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.Dashboard.createPosts.CreateTextPost
import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.NavigationFragments.home.MainAdapter
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
//import com.karan.multipleviewrecyclerview.Banner
import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.home.DataItemType
import app.retvens.rown.communityDetails.CommunityDetailsActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.karan.multipleviewrecyclerview.RecyclerItem


class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var mainRecyclerView : RecyclerView
    lateinit var mList : ArrayList<DataItem>

    lateinit var recyclerCommunity : RecyclerView
    lateinit var communityArrayList : ArrayList<Community>
    lateinit var adapter:MainAdapter
    var profilepic: String = ""
    var userName:String = ""
    var profileName:String = ""
    var type:String = ""
    private lateinit var dialogRole:Dialog
    private lateinit var recyclerView:RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentList:ArrayList<GetComments>
    private  var userProfilePic:String = ""
    private  var UserName:String = ""
    private lateinit var cummunity:Community

    lateinit var viewAllCommunity : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAllCommunity = view.findViewById(R.id.view_all_bg)
        viewAllCommunity.setOnClickListener{
            startActivity(Intent(context, CommunityDetailsActivity::class.java))
        }

        val sharedPreferencesPro = context?.getSharedPreferences("SaveProgress", AppCompatActivity.MODE_PRIVATE)
        val toPo = sharedPreferencesPro?.getString("progress", "50").toString()
        val progress :Int = toPo.toInt()
            if (progress < 100){
                val bottomSheet = BottomSheet()
                val fragManager = (activity as FragmentActivity).supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheet.TAG)}
            }

        view.findViewById<RelativeLayout>(R.id.relative_create).setOnClickListener {
            startActivity(Intent(context, CreateTextPost::class.java))
        }

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()
        val homeProfile = view.findViewById<ShapeableImageView>(R.id.home_profile)
        Glide.with(requireContext()).load(profilePic).into(homeProfile)

        recyclerCommunity = view.findViewById(R.id.recyclerCommunity)
        recyclerCommunity.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerCommunity.setHasFixedSize(true)

        communityArrayList = arrayListOf<Community>()
        commentList = ArrayList()

        cummunity = Community("","","")




        getCommunities()


        val btn = view.findViewById<ImageView>(R.id.community_btn)
        btn.setOnClickListener {
            startActivity(Intent(context, CreateCommunity::class.java))
        }

        mainRecyclerView = view.findViewById(R.id.mainRecyclerView)
        mainRecyclerView.setHasFixedSize(true)
        mainRecyclerView.layoutManager = LinearLayoutManager(context)

        mList = ArrayList()
        prepareData()
//        getPost()

        adapter = MainAdapter(requireContext(),mList)
        mainRecyclerView.adapter = adapter

    }

    private fun getCommunities() {
        val getCommunity = RetrofitBuilder.feedsApi.getCommunities()

        getCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
            override fun onResponse(
                call: Call<List<GetCommunitiesData>?>,
                response: Response<List<GetCommunitiesData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    response.forEach{ it ->

                        cummunity = Community(it.Profile_pic,it.group_name,"${response.size} members")
                        communityArrayList.add(cummunity)
                        recyclerCommunity.adapter = CommunityListAdapter(requireContext(),communityArrayList)

                    }
                }else{
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<GetCommunitiesData>?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun getPost() {

        val getPost = RetrofitBuilder.feedsApi.getPost()

       getPost.enqueue(object : Callback<List<DataItem.Banner>?> {
           override fun onResponse(
               call: Call<List<DataItem.Banner>?>,
               response: Response<List<DataItem.Banner>?>
           ) {
               if (response.isSuccessful){
                   val response = response.body()!!

                   Log.e("error",response.toString())

               }else{
                   Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
               }
           }

           override fun onFailure(call: Call<List<DataItem.Banner>?>, t: Throwable) {
               TODO("Not yet implemented")
           }
       })


    }


    private fun openCommentShit(postId:String) {
        dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.commentbottomshit)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        recyclerView = dialogRole.findViewById(R.id.comment_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getComment(postId)

    }

    private fun getComment(postId: String) {

    }


    private fun postLike(postId:String) {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = LikesCollection(user_id)

        val like = RetrofitBuilder.feedsApi.postLike(postId,data)

        like.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }


    private fun prepareData() {
        val communityList = ArrayList<DataItem.CommunityRecyclerData>()
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_post,"Vendor 4","78"))

        val createCommunityList = ArrayList<DataItem.CreateCommunityRecyclerData>()
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_post,"Vendor 4","78"))

        val blogsList = ArrayList<DataItem.BlogsRecyclerData>()
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_blog,"Tom", R.drawable.png_profile))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_post,"Vendor", R.drawable.png_profile_post))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_vendor,"Holland", R.drawable.png_profile))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_blog,"Jason", R.drawable.png_profile_post))

        val vendorsList = ArrayList<DataItem.VendorsRecyclerData>()
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_blog, R.drawable.png_profile,"Thailand in budget","Tom"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_post, R.drawable.png_profile_post,"Europe in budget","Vendor"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_vendor, R.drawable.png_profile,"Europe in budget","Holland"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_blog, R.drawable.png_profile_post,"China in budget","Jason"))


        val hotelSectionList = ArrayList<DataItem.HotelSectionRecyclerData>()
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_blog, "Paradise in"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_post, "Hotel in Oman"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_vendor, "Hotel 1"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_blog, "Hotel 2"))

        val hotelAwardsList = ArrayList<DataItem.AwardsRecyclerData>()
        hotelAwardsList.add(DataItem.AwardsRecyclerData(R.drawable.png_events))
        hotelAwardsList.add(DataItem.AwardsRecyclerData(R.drawable.png_awards))

//        mList.add(DataItem(DataItemType.CREATE_COMMUNITY, createCommunityRecyclerDataList = createCommunityList))
        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
        mList.add(DataItem(DataItemType.HOTEL_AWARDS, hotelAwardsList =  hotelAwardsList))
        mList.add(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = communityList))
        mList.add(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  hotelSectionList))
        mList.add(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = blogsList))
        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
    }
}
