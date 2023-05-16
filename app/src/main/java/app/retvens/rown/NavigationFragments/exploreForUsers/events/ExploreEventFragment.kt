package app.retvens.rown.NavigationFragments.exploreForUsers.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R


class ExploreEventFragment : Fragment() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreEventAdapter : ExploreEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_events_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

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

        exploreEventAdapter = ExploreEventsAdapter(blogs, requireContext())
        exploreBlogsRecyclerView.adapter = exploreEventAdapter
        exploreEventAdapter.notifyDataSetChanged()

    }
}