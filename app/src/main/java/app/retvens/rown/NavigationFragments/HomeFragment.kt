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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.NavigationFragments.home.Post
import app.retvens.rown.NavigationFragments.home.PostAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet


class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
}