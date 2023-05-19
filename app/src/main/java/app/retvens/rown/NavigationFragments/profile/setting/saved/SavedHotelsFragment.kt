package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsData
import app.retvens.rown.R


class SavedHotelsFragment : Fragment() {

    lateinit var savedHotelsRecyclerView : RecyclerView
    lateinit var savedHotelsAdapter: SavedHotelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_hotels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedHotelsRecyclerView = view.findViewById(R.id.savedHotelsRecyclerView)
        savedHotelsRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedHotelsRecyclerView.setHasFixedSize(true)

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

        savedHotelsAdapter = SavedHotelsAdapter(blogs, requireContext())
        savedHotelsRecyclerView.adapter = savedHotelsAdapter
        savedHotelsAdapter.notifyDataSetChanged()
    }
}