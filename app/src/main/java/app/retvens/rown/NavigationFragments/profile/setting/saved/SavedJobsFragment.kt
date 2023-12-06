package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.R


class SavedJobsFragment : Fragment() {

    lateinit var savedJobsRecyclerView: RecyclerView
    lateinit var savedJobAdapter: SavedJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        savedJobsRecyclerView = view.findViewById(R.id.savedJobsRecyclerView)
        savedJobsRecyclerView.layoutManager = LinearLayoutManager(context)
        //savedJobs //recyclerView. //recyclerView.setHasFixedSize(true)
//
//        val blogs = listOf<ExploreJobData>(
//            ExploreJobData("Senior Inn"),
//            ExploreJobData("Junior Inn 2"),
//            ExploreJobData("Android"),
//            ExploreJobData("Paradise Inn 23"),
//            ExploreJobData("Paradise Inn"),
//            ExploreJobData("Paradise Inn 2"),
//            ExploreJobData("Senior Inn"),
//            ExploreJobData("Junior Inn 2"),
//            ExploreJobData("Android"),
//            ExploreJobData("Paradise Inn 23"),
//            ExploreJobData("Paradise Inn"),
//            ExploreJobData("Paradise Inn 2"),
//        )

//        savedJobAdapter = SavedJobAdapter(blogs, requireContext())
//        savedJobsRecyclerView.adapter = savedJobAdapter
//        savedJobAdapter.notifyDataSetChanged()


    }
}