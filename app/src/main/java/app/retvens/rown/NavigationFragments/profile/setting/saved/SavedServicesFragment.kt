package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesData
import app.retvens.rown.R

class SavedServicesFragment : Fragment() {

    private lateinit var savedServicesRecyclerView: RecyclerView
    lateinit var savedServicesAdapter: SavedServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedServicesRecyclerView = view.findViewById(R.id.savedServicesRecyclerView)
        savedServicesRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedServicesRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreServicesData>(
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
        )

        savedServicesAdapter = SavedServicesAdapter(blogs, requireContext())
        savedServicesRecyclerView.adapter = savedServicesAdapter
        savedServicesAdapter.notifyDataSetChanged()

    }
}