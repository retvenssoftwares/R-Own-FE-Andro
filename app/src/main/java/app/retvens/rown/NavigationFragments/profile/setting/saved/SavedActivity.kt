package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.NavigationFragments.exploreForUsers.ExplorePostsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobsFragment
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesFragment
import app.retvens.rown.R

class SavedActivity : AppCompatActivity() {

    private lateinit var explorePosts : CardView
    private lateinit var exploreJobs : CardView
    private lateinit var exploreBlogs : CardView
    private lateinit var exploreEvents : CardView
    private lateinit var exploreHotels : CardView
    private lateinit var exploreServices : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        explorePosts = findViewById(R.id.explorePosts)
        exploreJobs = findViewById(R.id.exploreJobs)
        exploreBlogs = findViewById(R.id.exploreBlogs)
        exploreEvents = findViewById(R.id.exploreEvents)
        exploreHotels = findViewById(R.id.exploreHotels)
        exploreServices = findViewById(R.id.exploreServices)

        val fragment: Fragment = SavedPostsFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.child_saved_fragments_container,fragment)
        transaction.commit()

        /*-----------------------Saved FOR Posts--------------------------------*/
        /*-----------------------Saved FOR Posts--------------------------------*/
        explorePosts.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))


            val fragment: Fragment = SavedPostsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

        /*-----------------------Saved FOR Jobs--------------------------------*/
        /*-----------------------Saved FOR Jobs--------------------------------*/
        exploreJobs.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))


            val fragment: Fragment = SavedJobsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

        /*-----------------------Saved FOR Blogs--------------------------------*/
        /*-----------------------Saved FOR Blogs--------------------------------*/
        exploreBlogs.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            val fragment: Fragment = SavedBlogsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

        /*-----------------------Saved FOR Events--------------------------------*/
        /*-----------------------Saved FOR Events--------------------------------*/
        exploreEvents.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            val fragment: Fragment = SavedEventsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

        /*-----------------------Saved FOR Hotels--------------------------------*/
        /*-----------------------Saved FOR Hotels--------------------------------*/
        exploreHotels.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#ADD134"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            val fragment: Fragment = SavedHotelsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

        /*-----------------------Saved FOR Services--------------------------------*/
        /*-----------------------Saved FOR Services--------------------------------*/
        exploreServices.setOnClickListener {
            explorePosts.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreBlogs.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreEvents.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreHotels.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreServices.setCardBackgroundColor(Color.parseColor("#ADD134"))

            val fragment: Fragment = SavedServicesFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.child_saved_fragments_container,fragment)
            transaction.commit()
        }

    }
}