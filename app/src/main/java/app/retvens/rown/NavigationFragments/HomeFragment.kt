package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.Dashboard.MainAdapter
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.NavigationFragments.home.Post
import app.retvens.rown.NavigationFragments.home.PostAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet
//import com.karan.multipleviewrecyclerview.Banner
import com.karan.multipleviewrecyclerview.DataItem
import com.karan.multipleviewrecyclerview.DataItemType
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
    lateinit var communityArrayList : ArrayList<Community>

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

        val adapter = MainAdapter(mList)
        mainRecyclerView.adapter = adapter


        recyclerPost = view.findViewById(R.id.recyclerPost)
        recyclerPost.layoutManager = LinearLayoutManager(context)
        recyclerPost.setHasFixedSize(true)

        recyclerCommunity = view.findViewById(R.id.recyclerCommunity)
        recyclerCommunity.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerCommunity.setHasFixedSize(true)

        communityArrayList = arrayListOf<Community>()
        postsArrayList = arrayListOf<Post>()

        val post1 = Post(R.drawable.png_profile,
            "Walker",
            R.drawable.png_profile_post,
            R.drawable.png_post,
            "Paul","Traveller","","2d","arjungupta08","Every gives you a reason!")

        val post2 = Post(R.drawable.png_profile,
            "D'souza",
            R.drawable.png_profile_post,
            R.drawable.png_posts,
            "John","Blogger","Blogger community","3Hr","d_s","Every Stay Gives you a reason to smile!")
        postsArrayList.add(post1)
        postsArrayList.add(post2)
        postsArrayList.add(post1)

        recyclerPost.adapter = PostAdapter(requireContext(),postsArrayList)

        val cummunity = Community(R.drawable.png_profile_post,"Food community","70 members")
        val cummunity1 = Community(R.drawable.png_profile_post,"Travellers community","40 members")
        communityArrayList.add(cummunity)
        communityArrayList.add(cummunity1)
        communityArrayList.add(cummunity)
        communityArrayList.add(cummunity1)

        recyclerCommunity.adapter = CommunityListAdapter(requireContext(),communityArrayList)

        val btn = view.findViewById<ImageView>(R.id.community_btn)

        btn.setOnClickListener {
            startActivity(Intent(context,CreateCommunity::class.java))
        }

/*        val gesture = GestureDetector(
            activity,
            object : SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.i("tg", "onFling has been called!")
                    val SWIPE_MIN_DISTANCE = 120
                    val SWIPE_MAX_OFF_PATH = 250
                    val SWIPE_THRESHOLD_VELOCITY = 200
                    try {
                        if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) return false
                        if (e1.x - e2.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            val transaction: FragmentTransaction =
                                fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.fragment_container, ChatFragment())
                            transaction.addToBackStack(null)
                            transaction.commit()
                            Toast.makeText(context,"Chats",Toast.LENGTH_SHORT).show()
                        } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            Toast.makeText(context,"LtR",Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                        Log.d("tag",e.toString())
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })

        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return gesture.onTouchEvent(event)
            }
        })*/
    }

    private fun prepareData() {
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

        mList.add(DataItem(DataItemType.COMMUNITY, createCommunityRecyclerDataList = createCommunityList))
        mList.add(DataItem(DataItemType.BANNER, banner =  DataItem.Banner(R.drawable.png_post, R.drawable.png_profile, "Jason Stathon")))
        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_r_logo, "John")))
        mList.add(DataItem(DataItemType.HOTEL_AWARDS, hotelAwardsList =  hotelAwardsList))
        mList.add(DataItem(DataItemType.HOTEL_SECTION, hotelSectionList =  hotelSectionList))
        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_profile, "Tom")))
        mList.add(DataItem(DataItemType.BLOGS, blogsRecyclerDataList = blogsList))
        mList.add(DataItem(DataItemType.BANNER, banner = DataItem.Banner(R.drawable.png_posts, R.drawable.png_profile, "Chrish Hamswirth")))
        mList.add(DataItem(DataItemType.VENDORS, vendorsRecyclerDataList = vendorsList))
    }
}