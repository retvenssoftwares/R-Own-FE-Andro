package app.retvens.rown.NavigationFragments.profile.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R


class EventsProfileFragment : Fragment() {

    lateinit var eventRecyclerView: RecyclerView

    lateinit var eventsProfileAdapter: EventsProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreEventsData>(
            ExploreEventsData("Radiohead Concert"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Neck Deep Concert"),
            ExploreEventsData("Title 23"),
            ExploreEventsData("Title 1"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Title 3"),
            ExploreEventsData("Title 23"),
            ExploreEventsData("Title 1"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Title 3"),
            ExploreEventsData("Title 23"),
        )

        eventsProfileAdapter = EventsProfileAdapter(blogs, requireContext())
        eventRecyclerView.adapter = eventsProfileAdapter
        eventsProfileAdapter.notifyDataSetChanged()


    }
}