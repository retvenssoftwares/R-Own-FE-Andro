package app.retvens.rown.NavigationFragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.Dashboard.createPosts.CreateTextPost
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.home.MainAdapter
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.R
//import com.karan.multipleviewrecyclerview.Banner
import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.home.DataItemType
import app.retvens.rown.bottomsheet.*
import app.retvens.rown.utils.connectionCount
import app.retvens.rown.utils.getProfileInfo
import app.retvens.rown.utils.isBS
import app.retvens.rown.utils.profileImage
import app.retvens.rown.viewAll.communityDetails.ViewAllCommmunitiesActivity
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
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
    lateinit var communityArrayList : ArrayList<GetCommunitiesData>
    private var opemcommunity:ArrayList<GetCommunitiesData> = ArrayList()
    private var communitySize:ArrayList<Community> = ArrayList()
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
    private  var isLoading:Boolean = false
    lateinit var viewAllCommunity : ImageView
    private lateinit var progress:ProgressBar
    lateinit var shimmerFrameLayout2: ShimmerFrameLayout
    private lateinit var nestedScroll:NestedScrollView
    lateinit var empty : TextView
    private  var count:Int = 0
    private  var postCounter:Int = 0
    private  var pageCounter:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferencesPro = context?.getSharedPreferences("SaveConnectionNo", AppCompatActivity.MODE_PRIVATE)
        val toPo = sharedPreferencesPro?.getString("connectionNo", "0")

        if (connectionCount == "0" || toPo == "0" && isBS){
            val bottomSheet = BottomSheet()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheet.TAG)}
            isBS = false
        }

        viewAllCommunity = view.findViewById(R.id.view_all_bg)
        viewAllCommunity.setOnClickListener{
            startActivity(Intent(context, ViewAllCommmunitiesActivity::class.java))
        }

        nestedScroll = view.findViewById(R.id.homeFragment)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout2 = view.findViewById(R.id.shimmer_tasks_view_container)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        progress = view.findViewById(R.id.progress)


        view.findViewById<RelativeLayout>(R.id.relative_create).setOnClickListener {
            startActivity(Intent(context, CreateTextPost::class.java))
        }

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()
        val homeProfile = view.findViewById<ShapeableImageView>(R.id.home_profile)
        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(homeProfile)
        } else {
            getProfileInfo(requireContext())
            if (profileImage.isNotEmpty()) {
                Glide.with(requireContext()).load(profileImage).into(homeProfile)
            }  else {
                homeProfile.setImageResource(R.drawable.svg_user)
            }
        }

        recyclerCommunity = view.findViewById(R.id.recyclerCommunity)
        recyclerCommunity.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerCommunity.setHasFixedSize(true)

        communityArrayList = arrayListOf<GetCommunitiesData>()
        commentList = ArrayList()


        val sharedPreferences1 =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        mList = ArrayList()

        refresh.setOnRefreshListener {

            Thread{
                getCommunities()
            }.start()
            postCounter = 0
            pageCounter = 1

            Thread {
                // Run whatever background code you want here.
                getPost(user_id)

                getAllConnections(user_id)
            }.start()

            adapter = MainAdapter(requireContext(),mList)
            mainRecyclerView.adapter = adapter

            refresh.isRefreshing = false
        }


        val btn = view.findViewById<ImageView>(R.id.community_btn)
        btn.setOnClickListener {
            startActivity(Intent(context, CreateCommunity::class.java))
        }

        mainRecyclerView = view.findViewById(R.id.mainRecyclerView)
        mainRecyclerView.setHasFixedSize(true)
        mainRecyclerView.layoutManager = LinearLayoutManager(context)



            getCommunities()
            getPost(user_id)
            getAllConnections(user_id)
        prepareData()

        adapter = MainAdapter(requireContext(),mList)
        mainRecyclerView.adapter = adapter
        adapter.removePostsFromList(mList)
        adapter.notifyDataSetChanged()


        val layoutManager = mainRecyclerView.layoutManager as LinearLayoutManager
        nestedScroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))  {
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
//                    Toast.makeText(requireContext(), postCounter.toString(), Toast.LENGTH_SHORT).show()

                    if(!isLoading && totalItem <= (scrollOutItems+currentItem)){
                        Log.e("working","okk")
                        progress.setVisibility(View.VISIBLE);
                        getData()

                    }
                }
            }

        })


    }

    private fun getOpenCommunites() {
        val sharedPreferences1 =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        val openCommunity = RetrofitBuilder.feedsApi.getOpenCommunities(user_id)

        openCommunity.enqueue(object : Callback<List<DataItem.CommunityRecyclerData>?> {
            override fun onResponse(
                call: Call<List<DataItem.CommunityRecyclerData>?>,
                response: Response<List<DataItem.CommunityRecyclerData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach { it ->
                        if (!mList.contains(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = response))) {
                            mList.add(
                                DataItem(
                                    DataItemType.COMMUNITY,
                                    communityRecyclerDataList = response
                                )
                            )
                        }
                    }
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DataItem.CommunityRecyclerData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getData() {

        val sharedPreferences1 =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()
        if (pageCounter >= 1) {

            val handler = Handler()

            Log.e("check","1")
            if (pageCounter == 1) {
                Log.e("check","1")
                handler.postDelayed({
                    getHotels()
                    getPost(user_id)
                    progress.setVisibility(View.GONE)
                }, 3000)

            }else if (pageCounter == 2) {
                Log.e("check","2")
                handler.postDelayed({
                    getOpenCommunites()
                    getPost(user_id)
                    progress.setVisibility(View.GONE)
                }, 3000)

            }else if (pageCounter == 3) {
                Log.e("check","3")
                handler.postDelayed({
                    getAllBlogs()
                    getPost(user_id)
                    progress.setVisibility(View.GONE)
                }, 3000)

            }else if (pageCounter == 4) {
                Log.e("check","4")
                getServices()
                handler.postDelayed({
                    getPost(user_id)
                    progress.setVisibility(View.GONE)
                }, 3000)

                pageCounter = 1
            }


//            handler.postDelayed({
//                getPost(user_id)
//                progress.setVisibility(View.GONE)
//            }, 3000)
        }
    }

    private fun getPost(userId: String) {


        val getData = RetrofitBuilder.feedsApi.getPost(userId)

        getData.enqueue(object : Callback<List<PostsDataClass>?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {                if (isAdded){
                if (response.isSuccessful){
                    shimmerFrameLayout2.stopShimmer()
                    shimmerFrameLayout2.visibility = View.GONE

                    if (response.body()!!.isNotEmpty()) {
                    val response = response.body()!!

                    try {

                    Log.e("response",response.toString())

                    val postList = ArrayList<DataItem.Banner>()

                    response.forEach { it ->


                            it.posts.forEach { item ->

                                if (item.post_type == "share some media"){
                                    if (mList.contains(DataItem(DataItemType.BANNER, banner = item))){
//                                        mList.shuffle()
//                                        adapter.notifyDataSetChanged()
                                    } else {
                                        mList.add(0, DataItem(DataItemType.BANNER, banner = item))
                                        postCounter += 1
                                    }
                                }
                                if (item.post_type == "Polls"){
                                    if (mList.contains(DataItem(DataItemType.POLL, banner = item))){
//                                        mList.shuffle()
//                                        adapter.notifyDataSetChanged()
                                    } else {
                                        mList.add(0, DataItem(DataItemType.POLL, banner = item))
                                        postCounter += 1
                                    }
                                }
                                if (item.post_type == "normal status"){
                                    if (mList.contains(DataItem(DataItemType.Status, banner = item))){
//                                        mList.shuffle()
//                                        adapter.notifyDataSetChanged()
                                    } else {
                                        mList.add(0, DataItem(DataItemType.Status, banner = item))
                                        postCounter += 1
                                    }
                                }

//                                if (item.post_type == "Update about an event"){
//                                    if (mList.contains(DataItem(DataItemType.Event, banner = item))){
//                                        mList.shuffle()
//                                        adapter.notifyDataSetChanged()
//                                    } else {
//                                        mList.add(0, DataItem(DataItemType.Event, banner = item))
//                                        postCounter += 1
//                                    }
//                                }

                                if (item.post_type == "Check-in"){
                                    if (mList.contains(DataItem(DataItemType.CheckIn, banner = item))){
//                                        mList.shuffle()
//                                        adapter.notifyDataSetChanged()
                                    } else {
                                        mList.add(0, DataItem(DataItemType.CheckIn, banner = item))
                                        postCounter += 1
                                    }
                                }


                                if (postCounter >= 10) {
                                    pageCounter += 1
                                    postCounter = 0
                                }
                            }

//




                    }


                    adapter.notifyDataSetChanged()

                    adapter.setOnItemClickListener(object : MainAdapter.OnItemClickListener{
                        override fun onItemClick(dataItem: PostItem) {

                            postLike(dataItem.post_id)
                        }

                        override fun onItemClickForComment(banner: PostItem, position: Int) {
                            val bottomSheet = BottomSheetComment(banner.post_id,banner.Profile_pic)
                            val fragManager = (activity as FragmentActivity).supportFragmentManager
                            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
                        }

                        override fun onItemsharePost(share: String) {

                            val bottomSheet = BottomSheetSharePost(share)
                            val fragManager = (activity as FragmentActivity).supportFragmentManager
                            fragManager.let{bottomSheet.show(it, BottomSheetSharePost.Company_TAG)}
                        }

                    })

                    }catch (e: NullPointerException){
                        Log.e("Exception", "NullPointerException occurred: ${e.message}")
                    }

                } else {
                        empty.text = "No upcoming events"
                        empty.visibility = View.VISIBLE
                    }
                } else {
                    empty.visibility = View.VISIBLE
                    empty.text = response.code().toString()
                    shimmerFrameLayout2.stopShimmer()
                    shimmerFrameLayout2.visibility = View.GONE
                    Toast.makeText(requireContext(), " -> ${response.code().toString()}", Toast.LENGTH_SHORT).show()
                }
            }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                shimmerFrameLayout2.stopShimmer()
                shimmerFrameLayout2.visibility = View.GONE
                Log.e("error",t.message.toString())
            }
        })

    }


    private fun getCommunities() {

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getCommunity = RetrofitBuilder.feedsApi.getCommunities(user_id)

        getCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
            override fun onResponse(
                call: Call<List<GetCommunitiesData>?>,
                response: Response<List<GetCommunitiesData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    Log.e("response",response.toString())
                    val viewAllCommunities = view?.findViewById<CardView>(R.id.view_allCommunity)
                    if (response.size <= 1){
                        viewAllCommunities?.visibility = View.GONE
                    }

                    response.forEach{ it ->

                        if (!communityArrayList.contains(it)) {
                            communityArrayList.add(it)
                            recyclerCommunity.adapter = CommunityListAdapter(requireContext(), response)
                        }
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
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
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


    private fun getAllConnections(userId: String) {

        val getConnections = RetrofitBuilder.connectionApi.getConnectionList(userId)

      getConnections.enqueue(object : Callback<List<ConnectionListDataClass>?> {
          override fun onResponse(
              call: Call<List<ConnectionListDataClass>?>,
              response: Response<List<ConnectionListDataClass>?>
          ) {
              if (response.isSuccessful) {
                  val response = response.body()!!

                  if (response == null){
                      val bottomSheet = BottomSheet()
                      val fragManager = (activity as FragmentActivity).supportFragmentManager
                      fragManager.let{bottomSheet.show(it, BottomSheet.TAG)}
                  }
              }
          }

          override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {

          }
      })

    }

    private fun getAllBlogs() {
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val allBlogs = RetrofitBuilder.viewAllApi.getAllBlogs(user_id)
        allBlogs.enqueue(object : Callback<List<AllBlogsData>?> {
            override fun onResponse(
                call: Call<List<AllBlogsData>?>,
                response: Response<List<AllBlogsData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {

                        if (response.body()!!.isNotEmpty()) {
                            val blogsList = ArrayList<DataItem.BlogsRecyclerData>()
                            if (mList.contains(DataItem(
                                    DataItemType.BLOGS,
                                    blogsRecyclerDataList = response.body()
                                ))){
                                mList.shuffle()
                                adapter.notifyDataSetChanged()
                            } else {
                                mList.add(
                                    DataItem(
                                        DataItemType.BLOGS,
                                        blogsRecyclerDataList = response.body()
                                    )
                                )
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<AllBlogsData>?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, "All Blogs ${t.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }

    private fun getHotels() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id,"1")
        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {

                if (isAdded){
                    if (response.isSuccessful){
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {
                                if (!mList.contains(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  it.posts))){
                                    mList.add(
                                        DataItem(
                                            DataItemType.HOTEL_SECTION,
                                            hotelSectionList = it.posts
                                        )
                                    )
                                } else {
                                    mList.shuffle()
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    } else {
                    }
                }
            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, "All Blogs ${t.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }


    private fun getServices() {
        val serv = RetrofitBuilder.exploreApis.getExploreService("1")
        serv.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        if (data.isNotEmpty()) {
                            data.forEach {
                                if (mList.contains(DataItem(
                                        DataItemType.VENDORS,
                                        vendorsRecyclerDataList = it.vendors
                                    ))){
                                    mList.shuffle()
                                    adapter.notifyDataSetChanged()
                                } else {
                                    mList.add(
                                        DataItem(
                                            DataItemType.VENDORS,
                                            vendorsRecyclerDataList = it.vendors
                                        )
                                    )
                                    Log.e("services", data.toString())
                                }
                            }
                        }
                    }
            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {

            }
        })
    }
    private fun prepareData() {
//        val communityList = ArrayList<DataItem.CommunityRecyclerData>()
//        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12", "Join"))
//        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34", "Request"))
//        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3", "Join"))
//        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_post,"Vendor 4","78", "Request"))

        val createCommunityList = ArrayList<DataItem.CreateCommunityRecyclerData>()
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12", "0"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34", "0"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3", "1"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_post,"Vendor 4","78", "1"))

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
//        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
//        mList.add(DataItem(DataItemType.HOTEL_AWARDS, hotelAwardsList =  hotelAwardsList))
//        mList.add(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = communityList))
//        mList.add(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  hotelSectionList))

//        mList.add(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = blogsList))
//        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
    }

}
