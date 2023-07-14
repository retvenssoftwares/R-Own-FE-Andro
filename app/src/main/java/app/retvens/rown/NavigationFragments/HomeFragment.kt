package app.retvens.rown.NavigationFragments

//import com.karan.multipleviewrecyclerview.Banner
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.home.*
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
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

    lateinit var mainRecyclerView: RecyclerView
    lateinit var mList: ArrayList<DataItem>

    lateinit var recyclerCommunity: RecyclerView
    lateinit var communityArrayList: ArrayList<GetCommunitiesData>
    private var opemcommunity: ArrayList<GetCommunitiesData> = ArrayList()
    private var communitySize: ArrayList<Community> = ArrayList()
    lateinit var adapter: MainAdapter
    var profilepic: String = ""
    var userName: String = ""
    var profileName: String = ""
    var type: String = ""
    private lateinit var dialogRole: Dialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentList: ArrayList<GetComments>
    private var userProfilePic: String = ""
    private var UserName: String = ""
    private lateinit var cummunity: Community
    private var isLoading: Boolean = false
    lateinit var viewAllCommunity: ImageView
    private lateinit var progress: ProgressBar
    lateinit var shimmerFrameLayout2: ShimmerFrameLayout
    private lateinit var nestedScroll: NestedScrollView
    private var feedList: ArrayList<PostsDataClass> = ArrayList()
    private var commList: ArrayList<DataItem.CommunityRecyclerData> = ArrayList()
    private var blogList: ArrayList<AllBlogsData> = ArrayList()
    private var serviceList: ArrayList<ProfileServicesDataItem> = ArrayList()
    private var hotelList: ArrayList<HotelData> = ArrayList()
    lateinit var empty: TextView
    private var count: Int = 0
    private var postCounter: Int = 0
    private var pageCounter: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferencesPro =
            context?.getSharedPreferences("SaveConnectionNo", AppCompatActivity.MODE_PRIVATE)
        val toPo = sharedPreferencesPro?.getString("connectionNo", "0")

        if (connectionCount == "0" || toPo == "0" && isBS) {
            val bottomSheet = BottomSheet()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let { bottomSheet.show(it, BottomSheet.TAG) }
            isBS = false
        }

        viewAllCommunity = view.findViewById(R.id.view_all_bg)
        viewAllCommunity.setOnClickListener {
            startActivity(Intent(context, ViewAllCommmunitiesActivity::class.java))
        }

        nestedScroll = view.findViewById(R.id.homeFragment)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout2 = view.findViewById(R.id.shimmer_tasks_view_container)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        progress = view.findViewById(R.id.progress)


        val sharedPreferences =
            context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()
        val homeProfile = view.findViewById<ShapeableImageView>(R.id.home_profile)
        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(homeProfile)
        } else {
            getProfileInfo(requireContext())
            if (profileImage.isNotEmpty()) {
                Glide.with(requireContext()).load(profileImage).into(homeProfile)
            } else {
                homeProfile.setImageResource(R.drawable.svg_user)
            }
        }

        recyclerCommunity = view.findViewById(R.id.recyclerCommunity)
        recyclerCommunity.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerCommunity.setHasFixedSize(true)

        communityArrayList = arrayListOf<GetCommunitiesData>()
        commentList = ArrayList()


        val sharedPreferences1 =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        mList = ArrayList()

        refresh.setOnRefreshListener {

            Thread {
                getCommunities()
            }.start()
            postCounter = 0
            pageCounter = 1

            Thread {
                // Run whatever background code you want here.
                getPost(user_id)

                getAllConnections(user_id)
            }.start()

            adapter = MainAdapter(requireContext(), mList)
            mainRecyclerView.adapter = adapter

            refresh.isRefreshing = false
        }


        val btn = view.findViewById<ImageView>(R.id.community_btn)
        btn.setOnClickListener {
            startActivity(Intent(context, CreateCommunity::class.java))
        }

        mainRecyclerView = view.findViewById(R.id.mainRecyclerView)
        mainRecyclerView.setHasFixedSize(true)
        mainRecyclerView.layoutManager = context?.let { CustomLinearLayoutManager(it) }
        mainRecyclerView.isNestedScrollingEnabled = false

        getCommunities()
        getPost(user_id)
        getAllConnections(user_id)

        adapter = MainAdapter(requireContext(), mList)
        mainRecyclerView.adapter = adapter
        adapter.removePostsFromList(mList)
        adapter.notifyDataSetChanged()

        view.findViewById<RelativeLayout>(R.id.relative_create).setOnClickListener {
            startActivity(Intent(context, CreateTextPost::class.java))
        }

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


                    if(!isLoading && totalItem <= (scrollOutItems+currentItem)){
                            getData()
                    }
                }
            }

        })


//        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isLoading = true;
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0) {
//                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                    val currentItem = layoutManager.childCount
//                    val totalItem = layoutManager.itemCount
//                    val scrollOutItems = layoutManager.findFirstVisibleItemPosition()
//                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//                    if (isLoading && (lastVisibleItemPosition == totalItem - 2)) {
//                        isLoading = false
//                        getData()
//                    }
//                }
//
//
//            }
//        })


    }

    class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }


    private fun getData() {
        val sharedPreferences1 = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()
        val handler = Handler()
        when (pageCounter) {
            1 -> {
                Log.e("check", "1")

//                handler.postDelayed({
                    getHotels()
//                }, 200)

                getPost(user_id)
            }
            2 -> {
                Log.e("check", "2")
//                handler.postDelayed({
                    getOpenCommunites()
//                }, 200)
                getPost(user_id)
            }
            3 -> {
                Log.e("check", "3")
//                handler.postDelayed({
                    getAllBlogs()
//                }, 200)
                getPost(user_id)
            }
            else -> {
                Log.e("check", "4")
//                handler.postDelayed({
                    getServices()
//                }, 200)
                getPost(user_id)
                pageCounter = 0
            }
        }
    }

    private fun getOpenCommunites() {
        val sharedPreferences1 =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        val openCommunity = RetrofitBuilder.feedsApi.getOpenCommunities(user_id)

        openCommunity.enqueue(object : Callback<List<DataItem.CommunityRecyclerData>?> {
            override fun onResponse(
                call: Call<List<DataItem.CommunityRecyclerData>?>,
                response: Response<List<DataItem.CommunityRecyclerData>?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!


                    mList.addAll(
                        listOf(
                            DataItem(
                                DataItemType.COMMUNITY,
                                communityRecyclerDataList = response
                            )
                        )
                    )

                } else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<List<DataItem.CommunityRecyclerData>?>,
                t: Throwable
            ) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPost(userId: String) {


        val getData = RetrofitBuilder.feedsApi.getFeedPost(userId)

        getData.enqueue(object : Callback<List<PostsDataClass>?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout2.stopShimmer()
                        shimmerFrameLayout2.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                            val response = response.body()!!

                            try {

                                Log.e("response", response.toString())

                                Log.e("feedList", feedList.toString())
                                response.forEach {
                                    it.posts.forEach { item ->
                                        if (item.post_type == "share some media") {
                                            mList.add(DataItem(DataItemType.BANNER, banner = item))
                                            postCounter += 1
                                        }
                                        if (item.post_type == "Polls") {
                                            mList.add(DataItem(DataItemType.POLL, banner = item))
                                            postCounter += 1

                                        }
                                        if (item.post_type == "normal status") {
                                            mList.add(DataItem(DataItemType.Status, banner = item))
                                            postCounter += 1
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

                                        if (item.post_type == "Check-in") {
                                            Log.e("hotel", item.toString())
                                            mList.add(DataItem(DataItemType.CheckIn, banner = item))
                                            postCounter += 1
                                        }

                                        if (postCounter >= 10) {
                                            pageCounter += 1
                                            postCounter = 0
                                        }
                                    }

//                            blogList.addAll(it.blogs)
//                            serviceList.addAll(it.services)
//                            hotelList.addAll(it.hotels)
//                            commList.addAll(it.communities)
//
//
//                            try {
//                                mList.addAll(listOf(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = it.blogs)))
//                            }catch (e:Exception){
//                                Log.e("error",e.message.toString())
//                            }
//                            try {
//                                mList.addAll(listOf(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList = it.hotels)))
//                            }catch (e:Exception){
//                                Log.e("error",e.message.toString())
//                            }
//                            try {
//                                mList.addAll(listOf(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = it.services)))
//                            }catch (e:Exception){
//                                Log.e("error",e.message.toString())
//                            }
//                            try {
//                                Log.e("comm",commList.toString())
//                                mList.addAll(listOf(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = it.communities)))
//                            }catch (e:Exception){
//                                Log.e("error",e.message.toString())
//                            }
//
                                }
                                adapter.notifyDataSetChanged()

                                adapter.setOnItemClickListener(object :
                                    MainAdapter.OnItemClickListener {
                                    override fun onItemClick(dataItem: PostItem) {

                                        postLike(dataItem.post_id)
                                    }

                                    override fun onItemClickForComment(
                                        banner: PostItem,
                                        position: Int
                                    ) {
                                        val bottomSheet =
                                            BottomSheetComment(banner.post_id, banner.Profile_pic)
                                        val fragManager =
                                            (activity as FragmentActivity).supportFragmentManager
                                        fragManager.let {
                                            bottomSheet.show(
                                                it,
                                                BottomSheetLocation.LOCATION_TAG
                                            )
                                        }
                                    }

                                    override fun onItemsharePost(share: String) {

                                        val bottomSheet = BottomSheetSharePost(share)
                                        val fragManager =
                                            (activity as FragmentActivity).supportFragmentManager
                                        fragManager.let {
                                            bottomSheet.show(
                                                it,
                                                BottomSheetSharePost.Company_TAG
                                            )
                                        }
                                    }

                                })

                            } catch (e: NullPointerException) {
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
                        Toast.makeText(
                            requireContext(),
                            " -> ${response.code().toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                shimmerFrameLayout2.stopShimmer()
                shimmerFrameLayout2.visibility = View.GONE
                Log.e("error", t.message.toString())
            }
        })

    }


    private fun getCommunities() {

        val sharedPreferences =
            requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getCommunity = RetrofitBuilder.feedsApi.getCommunities(user_id)

        getCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
            override fun onResponse(
                call: Call<List<GetCommunitiesData>?>,
                response: Response<List<GetCommunitiesData>?>
            ) {
                if (response.isSuccessful && isAdded) {
                    val response = response.body()!!
                    Log.e("response", response.toString())
                    val viewAllCommunities = view?.findViewById<CardView>(R.id.view_allCommunity)
                    if (response.size <= 1) {
                        viewAllCommunities?.visibility = View.GONE
                    }

                    response.forEach { it ->

                        if (!communityArrayList.contains(it)) {
                            communityArrayList.add(it)
                            recyclerCommunity.adapter =
                                CommunityListAdapter(requireContext(), response)
                        }
                    }
                } else {
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


    private fun postLike(postId: String) {

        val sharedPreferences =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = LikesCollection(user_id)

        val like = RetrofitBuilder.feedsApi.postLike(postId, data)

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
                            response.message().toString(),
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


    private fun getAllConnections(userId: String) {

        val getConnections = RetrofitBuilder.connectionApi.getConnectionList(userId)

        getConnections.enqueue(object : Callback<List<ConnectionListDataClass>?> {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!

                    if (response == null) {
                        val bottomSheet = BottomSheet()
                        val fragManager = (activity as FragmentActivity).supportFragmentManager
                        fragManager.let { bottomSheet.show(it, BottomSheet.TAG) }
                    }
                }
            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {

            }
        })

    }

    private fun getAllBlogs() {
        val sharedPreferences =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
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

                            mList.addAll(
                                listOf(
                                    DataItem(
                                        DataItemType.BLOGS,
                                        blogsRecyclerDataList = response.body()
                                    )
                                )
                            )

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

        val sharedPreferences =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id, "1")
        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {

                if (isAdded) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {
                                mList.addAll(
                                    listOf(
                                        DataItem(
                                            DataItemType.HOTEL_SECTION,
                                            hotelSectionList = it.posts
                                        )
                                    )
                                )
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

                            mList.addAll(
                                listOf(
                                    DataItem(
                                        DataItemType.VENDORS,
                                        vendorsRecyclerDataList = it.vendors
                                    )
                                )
                            )
                            Log.e("services", data.toString())

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {

            }
        })
    }


}
