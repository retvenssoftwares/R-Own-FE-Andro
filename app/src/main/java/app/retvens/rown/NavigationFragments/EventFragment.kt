package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventCategoryActivity
import app.retvens.rown.NavigationFragments.eventForUsers.allEvents.SeeAllEventsActivity
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsActivity
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesData
import app.retvens.rown.R


class EventFragment : Fragment() {

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    lateinit var onGoingRecyclerView: RecyclerView
    lateinit var onGoingEventsAdapter: OnGoingEventsAdapter

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



        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.setHasFixedSize(true)
        getCategories()

        onGoingRecyclerView = view.findViewById(R.id.onGoingRecyclerView)
        onGoingRecyclerView.layoutManager = LinearLayoutManager(context)
        onGoingRecyclerView.setHasFixedSize(true)
        onGoingEvents()

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

    private fun onGoingEvents() {
        val blogs = listOf<OnGoingEventsData>(
            OnGoingEventsData("Title 1"),
            OnGoingEventsData("Title 2"),
            OnGoingEventsData("Title 3"),
            OnGoingEventsData("Title 23"),
        )

        onGoingEventsAdapter = OnGoingEventsAdapter(blogs, requireContext())
        onGoingRecyclerView.adapter = onGoingEventsAdapter
        onGoingEventsAdapter.notifyDataSetChanged()
    }

    private fun getCategories() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.setHasFixedSize(true)

        val blogs = listOf<EventCategoriesData>(
            EventCategoriesData("Title 1"),
            EventCategoriesData("Title 2"),
            EventCategoriesData("Title 3"),
            EventCategoriesData("Title 23"),
        )

        eventCategoriesAdapter = EventCategoriesAdapter(blogs, requireContext())
        categoryRecyclerView.adapter = eventCategoriesAdapter
        eventCategoriesAdapter.notifyDataSetChanged()
    }
}