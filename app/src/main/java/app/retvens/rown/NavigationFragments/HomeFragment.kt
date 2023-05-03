package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.DataCollections.FeedCollection.FetchPostDataClass
import app.retvens.rown.DataCollections.FeedCollection.Media
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.home.MainAdapter
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.NavigationFragments.home.Post
import app.retvens.rown.NavigationFragments.home.PostAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
//import com.karan.multipleviewrecyclerview.Banner
import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.home.DataItemType
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
    lateinit var recyclerPost : RecyclerView
    lateinit var recyclerCommunity : RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var communityListAdapter: CommunityListAdapter
    lateinit var postsArrayList : ArrayList<Post>
    lateinit var blogsList : ArrayList<DataItem.BlogsRecyclerData>
    lateinit var  communityList : ArrayList<DataItem.CommunityRecyclerData>
    lateinit var  vendorsList : ArrayList<DataItem.VendorsRecyclerData>
    lateinit var  hotelAwardsList : ArrayList<DataItem.AwardsRecyclerData>
    lateinit var  hotelSectionList : ArrayList<DataItem.HotelSectionRecyclerData>

    lateinit var adapter:MainAdapter
    var profilepic: String = ""
    var userName:String = ""
    var profileName:String = ""
    var type:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = BottomSheet()
        val fragManager = (activity as FragmentActivity).supportFragmentManager
        fragManager.let{bottomSheet.show(it, BottomSheet.TAG)}

        mainRecyclerView = view.findViewById(R.id.mainRecyclerView)
        mainRecyclerView.setHasFixedSize(true)
        mainRecyclerView.layoutManager = LinearLayoutManager(context)

        mList = ArrayList()
        prepareData()
        getPost()

        adapter = MainAdapter(requireContext(),mList)
        mainRecyclerView.adapter = adapter


        recyclerPost = view.findViewById(R.id.recyclerPost)
        recyclerPost.layoutManager = LinearLayoutManager(context)
        recyclerPost.setHasFixedSize(true)

        recyclerCommunity = view.findViewById(R.id.recyclerCommunity)
        recyclerCommunity.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerCommunity.setHasFixedSize(true)

//        communityArrayList = arrayListOf<Community>()
//        postsArrayList = arrayListOf<Post>()




        val cummunity = Community(R.drawable.png_profile_post,"Food community","70 members")
        val cummunity1 = Community(R.drawable.png_profile_post,"Travellers community","40 members")
//        communityArrayList.add(cummunity)
//        communityArrayList.add(cummunity1)
//        communityArrayList.add(cummunity)
//        communityArrayList.add(cummunity1)
//
//        recyclerCommunity.adapter = CommunityListAdapter(requireContext(),communityArrayList)

        val btn = view.findViewById<ImageView>(R.id.community_btn)

        btn.setOnClickListener {
            startActivity(Intent(context,CreateCommunity::class.java))
        }

    }

    private fun getPost() {
        val getPost = RetrofitBuilder.feedsApi.getPost()

        getPost.enqueue(object : Callback<List<FetchPostDataClass>?> {
            override fun onResponse(
                call: Call<List<FetchPostDataClass>?>,
                response: Response<List<FetchPostDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    response.forEach{      item ->
                        var post:String = ""
                        for ( x in item.media){
                            post = x.post
                        }
                        getPostProfile(item.user_id)

                        mList.add(DataItem(DataItemType.BANNER, banner =  DataItem.Banner(item._id,item.user_id,item.caption,item.likes,
                        listOf(),item.location,item.hashtags,
                            listOf<DataItem.Media>(DataItem.Media(post,"","")),
                            item.post_id,0,item.display_status,item.saved_post,
                        profilepic,profileName,"",userName)))

//                        Toast.makeText(requireContext(),profileName,Toast.LENGTH_SHORT).show()

                        adapter = MainAdapter(requireContext(),mList)
                        mainRecyclerView.adapter = adapter

                        adapter.notifyDataSetChanged()
                    }
//                    mList.add(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = communityList))
                    mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
                    mList.add(DataItem(DataItemType.HOTEL_AWARDS, hotelAwardsList =  hotelAwardsList))
                    response.forEach{      item ->
                        var post:String = ""
                        for ( x in item.media){
                            post = x.post
                        }
                        getPostProfile(item.user_id)

                        mList.add(DataItem(DataItemType.BANNER, banner =  DataItem.Banner(item._id,item.user_id,item.caption,item.likes,
                            listOf(),item.location,item.hashtags,
                            listOf<DataItem.Media>(DataItem.Media(post,"","")),
                            item.post_id,0,item.display_status,item.saved_post,
                            profilepic,profileName,"",userName)))

//                        Toast.makeText(requireContext(),profileName,Toast.LENGTH_SHORT).show()

                    }
                    mList.add(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = blogsList))
                    mList.add(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  hotelSectionList))

                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<FetchPostDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun getPostProfile(userId:String) {

        val data = RetrofitBuilder.retrofitBuilder.fetchUser("Oo7PCzo0-")

        data.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    profileName = response.Full_name
                    profilepic = response.Profile_pic
                    userName = response.User_name
                    Toast.makeText(requireContext(),profileName,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun prepareData() {
        communityList = ArrayList<DataItem.CommunityRecyclerData>()
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3"))
        communityList.add(DataItem.CommunityRecyclerData(R.drawable.png_post,"Vendor 4","78"))

        val createCommunityList = ArrayList<DataItem.CreateCommunityRecyclerData>()
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 1","12"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_blog,"Vendor 2","34"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_vendor,"Vendor 3","3"))
        createCommunityList.add(DataItem.CreateCommunityRecyclerData(R.drawable.png_post,"Vendor 4","78"))

        blogsList = ArrayList<DataItem.BlogsRecyclerData>()
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_blog,"Tom", R.drawable.png_profile))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_post,"Vendor", R.drawable.png_profile_post))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_vendor,"Holland", R.drawable.png_profile))
        blogsList.add(DataItem.BlogsRecyclerData(R.drawable.png_blog,"Jason", R.drawable.png_profile_post))

        vendorsList = ArrayList<DataItem.VendorsRecyclerData>()
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_blog, R.drawable.png_profile,"Thailand in budget","Tom"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_post, R.drawable.png_profile_post,"Europe in budget","Vendor"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_vendor, R.drawable.png_profile,"Europe in budget","Holland"))
        vendorsList.add(DataItem.VendorsRecyclerData(R.drawable.png_blog, R.drawable.png_profile_post,"China in budget","Jason"))


        hotelSectionList = ArrayList<DataItem.HotelSectionRecyclerData>()
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_blog, "Paradise in"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_post, "Hotel in Oman"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_vendor, "Hotel 1"))
        hotelSectionList.add(DataItem.HotelSectionRecyclerData(R.drawable.png_blog, "Hotel 2"))

        hotelAwardsList = ArrayList<DataItem.AwardsRecyclerData>()
        hotelAwardsList.add(DataItem.AwardsRecyclerData(R.drawable.png_events))
        hotelAwardsList.add(DataItem.AwardsRecyclerData(R.drawable.png_awards))

        mList.add(DataItem(DataItemType.CREATE_COMMUNITY, createCommunityRecyclerDataList = createCommunityList))
//        mList.add(DataItem(DataItemType.BANNER, banner =  DataItem.Banner(R.drawable.png_post, R.drawable.png_profile, "Jason Stathon")))
//        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
//        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_r_logo, "John")))
//        mList.add(DataItem(DataItemType.HOTEL_AWARDS, hotelAwardsList =  hotelAwardsList))
//        mList.add(DataItem(DataItemType.COMMUNITY, communityRecyclerDataList = communityList))
//        mList.add(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  hotelSectionList))
//        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_profile, "Tom")))
//        mList.add(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = blogsList))
//        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_profile, "Chrish Hamswirth")))
//        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
    }
}