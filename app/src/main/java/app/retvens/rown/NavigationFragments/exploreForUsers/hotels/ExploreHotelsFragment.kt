package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R


class ExploreHotelsFragment : Fragment() {


    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreHotelAdapter : ExploreHotelsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_hotels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_hotels_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreHotelsData>(
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
        )

        exploreHotelAdapter = ExploreHotelsAdapter(blogs, requireContext())
        exploreBlogsRecyclerView.adapter = exploreHotelAdapter
        exploreHotelAdapter.notifyDataSetChanged()

    }
}