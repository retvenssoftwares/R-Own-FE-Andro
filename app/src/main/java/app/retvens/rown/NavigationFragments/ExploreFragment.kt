package app.retvens.rown.NavigationFragments


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.ExplorePostsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesFragment
import app.retvens.rown.NavigationFragments.job.ApplyForJobFragment
import app.retvens.rown.NavigationFragments.job.JobExploreFragment
import app.retvens.rown.NavigationFragments.job.RequestForJobFragment
import app.retvens.rown.R

class ExploreFragment : Fragment() {

    private lateinit var explorePosts : CardView
    private lateinit var exploreJobs : CardView
    private lateinit var exploreBlogs : CardView
    private lateinit var exploreEvents : CardView
    private lateinit var exploreHotels : CardView
    private lateinit var exploreServices : CardView
    private lateinit var explorePeople : CardView


    var selectedFrag = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        explorePosts = view.findViewById(R.id.explorePosts)
        exploreJobs = view.findViewById(R.id.exploreJobs)
        exploreBlogs = view.findViewById(R.id.exploreBlogs)
        exploreEvents = view.findViewById(R.id.exploreEvents)
        exploreHotels = view.findViewById(R.id.exploreHotels)
        exploreServices = view.findViewById(R.id.exploreServices)
        explorePeople = view.findViewById(R.id.explorePeoples)

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        val childFragment: Fragment = ExplorePostsFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_events_fragments_container, childFragment).commit()

        /*-----------------------EXPLORE FOR Posts--------------------------------*/
        /*-----------------------EXPLORE FOR Posts--------------------------------*/
        explorePosts.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 0
            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            view.dispatchKeyEvent(event)
            val childFragment: Fragment = ExplorePostsFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        /*-----------------------EXPLORE FOR Jobs--------------------------------*/
        /*-----------------------EXPLORE FOR Jobs--------------------------------*/
        exploreJobs.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#ADD134"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            view.dispatchKeyEvent(event)
            selectedFrag = 1
            val childFragment: Fragment = ExploreJobsFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        /*-----------------------EXPLORE FOR Blogs--------------------------------*/
        /*-----------------------EXPLORE FOR Blogs--------------------------------*/
        exploreBlogs.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            view.dispatchKeyEvent(event)
            selectedFrag = 2
            val childFragment: Fragment = ExploreBlogsFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        /*-----------------------EXPLORE FOR Events--------------------------------*/
        /*-----------------------EXPLORE FOR Events--------------------------------*/
        exploreEvents.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 3
            val childFragment: Fragment = ExploreEventFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        /*-----------------------EXPLORE FOR Hotels--------------------------------*/
        /*-----------------------EXPLORE FOR Hotels--------------------------------*/
        exploreHotels.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 4
            val childFragment: Fragment = ExploreHotelsFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        /*-----------------------EXPLORE FOR Services--------------------------------*/
        /*-----------------------EXPLORE FOR Services--------------------------------*/
        exploreServices.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#ADD134"))

            selectedFrag = 5
            val childFragment: Fragment = ExploreServicesFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        explorePeople.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            explorePeople.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 6
            val childFragment: Fragment = ExplorePeopleFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
        }

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            if (selectedFrag == 0) {
                val childFragment: Fragment = ExplorePostsFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            } else if (selectedFrag == 1) {
                val childFragment: Fragment = ExploreJobsFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            } else if (selectedFrag == 2) {
                val childFragment: Fragment = ExploreBlogsFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            } else if (selectedFrag == 3) {
                val childFragment: Fragment = ExploreEventFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            } else if (selectedFrag == 4) {
                val childFragment: Fragment = ExploreHotelsFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            }else if (selectedFrag == 5) {
                val childFragment: Fragment = ExploreServicesFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            }else if (selectedFrag == 6) {
                val childFragment: Fragment = ExplorePeopleFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_events_fragments_container, childFragment).commit()
            }
            refresh.isRefreshing = false
        }

    }
}