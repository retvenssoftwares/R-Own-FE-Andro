package app.retvens.rown.NavigationFragments.profile.setting.saved

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


class SavedEventsFragment : Fragment() {

    lateinit var savedEventsRecyclerView: RecyclerView
    lateinit var savedEventsAdapter: SavedEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedEventsRecyclerView = view.findViewById(R.id.savedEventsRecyclerView)
        savedEventsRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedEventsRecyclerView.setHasFixedSize(true)

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

        savedEventsAdapter = SavedEventsAdapter(blogs, requireContext())
        savedEventsRecyclerView.adapter = savedEventsAdapter
        savedEventsAdapter.notifyDataSetChanged()

    }
}