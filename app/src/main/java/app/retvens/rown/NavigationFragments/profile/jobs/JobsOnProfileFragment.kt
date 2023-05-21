package app.retvens.rown.NavigationFragments.profile.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.R

class JobsOnProfileFragment : Fragment() {

    lateinit var exploreJobsRecyclerView: RecyclerView
    lateinit var exploreJobAdapter: ProfileJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs_on_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreJobsRecyclerView = view.findViewById(R.id.jobsExploreRecycler)
        exploreJobsRecyclerView.layoutManager = LinearLayoutManager(context)
        exploreJobsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreJobData>(
            ExploreJobData("Senior Inn"),
            ExploreJobData("Junior Inn 2"),
            ExploreJobData("Android"),
            ExploreJobData("Paradise Inn 23"),
            ExploreJobData("Paradise Inn"),
            ExploreJobData("Paradise Inn 2"),
            ExploreJobData("Senior Inn"),
            ExploreJobData("Junior Inn 2"),
            ExploreJobData("Android"),
            ExploreJobData("Paradise Inn 23"),
            ExploreJobData("Paradise Inn"),
            ExploreJobData("Paradise Inn 2"),
        )

        exploreJobAdapter = ProfileJobAdapter(blogs, requireContext())
        exploreJobsRecyclerView.adapter = exploreJobAdapter
        exploreJobAdapter.notifyDataSetChanged()
    }
}