package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.media.CamcorderProfile.getAll
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventCategoryActivity
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.allEvents.SeeAllEventsActivity
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsActivity
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventFragment : Fragment() {

    lateinit var allRecyclerView: RecyclerView
    lateinit var allEventsAdapter: AllEventsAdapter

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    lateinit var onGoingRecyclerView: RecyclerView
    lateinit var onGoingEventsAdapter: OnGoingEventsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var shimmerFrameLayout2: ShimmerFrameLayout

    lateinit var empty : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_category)
        shimmerFrameLayout2 = view.findViewById(R.id.shimmer_tasks_view_container)


        allRecyclerView = view.findViewById(R.id.blogsRecyclerView)
        allRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        ///all //recyclerView. //recyclerView.setHasFixedSize(true)

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        //category //recyclerView. //recyclerView.setHasFixedSize(true)

        onGoingRecyclerView = view.findViewById(R.id.onGoingRecyclerView)
        onGoingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //onGoing //recyclerView. //recyclerView.setHasFixedSize(true)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            getAllEvents()
            getCategories()
            onGoingEvents()

            refresh.isRefreshing = false
        }

        view.findViewById<TextView>(R.id.viewAllItem).setOnClickListener {
            startActivity(Intent(context, SeeAllEventsActivity::class.java))
        }
        view.findViewById<TextView>(R.id.viewAllCategory).setOnClickListener {
            startActivity(Intent(context, AllEventCategoryActivity::class.java))
        }
        view.findViewById<TextView>(R.id.viewAllOnGoing).setOnClickListener {
            startActivity(Intent(context, OnGoingEventsActivity::class.java))
        }
    }

    private fun getAllEvents() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getAll = RetrofitBuilder.EventsApi.getAllEvents(user_id)
        getAll.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        allEventsAdapter = AllEventsAdapter(response.body()!!, requireContext())
                        allRecyclerView.adapter = allEventsAdapter
                        allEventsAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<List<OnGoingEventsData>?>, t: Throwable) {
                if (isAdded){
                    Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onGoingEvents() {
        val getOnGoingEvents = RetrofitBuilder.EventsApi.getOnGoingEvents()
        getOnGoingEvents.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout2.stopShimmer()
                        shimmerFrameLayout2.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                        onGoingEventsAdapter = OnGoingEventsAdapter(response.body()!!, requireContext())
                        onGoingRecyclerView.adapter = onGoingEventsAdapter
                        onGoingEventsAdapter.notifyDataSetChanged()
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

            override fun onFailure(call: Call<List<OnGoingEventsData>?>, t: Throwable) {
                    shimmerFrameLayout2.stopShimmer()
                    shimmerFrameLayout2.visibility = View.GONE
            }
        })
    }

    private fun getCategories() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        //category //recyclerView. //recyclerView.setHasFixedSize(true)

        val getEventCategories = RetrofitBuilder.EventsApi.getEventCategory()
        getEventCategories.enqueue(object : Callback<List<ViewAllCategoriesData>?> {
            override fun onResponse(
                call: Call<List<ViewAllCategoriesData>?>,
                response: Response<List<ViewAllCategoriesData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {

                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        eventCategoriesAdapter =
                            EventCategoriesAdapter(response.body()!!, requireContext())
                        categoryRecyclerView.adapter = eventCategoriesAdapter
                        eventCategoriesAdapter.notifyDataSetChanged()
                    } else{
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<ViewAllCategoriesData>?>, t: Throwable) {
                if (isAdded){
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE

                    Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}