package app.retvens.rown.NavigationFragments.exploreForUsers.services

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


class ExploreServicesFragment : Fragment() {


    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreServicesAdapter: ExploreServicesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        exploreBlogsRecyclerView = view.findViewById(R.id.explore_services_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

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

        exploreServicesAdapter = ExploreServicesAdapter(blogs, requireContext())
        exploreBlogsRecyclerView.adapter = exploreServicesAdapter
        exploreServicesAdapter.notifyDataSetChanged()

    }
}