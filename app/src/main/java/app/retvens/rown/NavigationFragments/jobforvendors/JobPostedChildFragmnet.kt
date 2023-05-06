package app.retvens.rown.NavigationFragments.jobforvendors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.job.RecentJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R

class JobPostedChildFragmnet : Fragment() {

    lateinit var suggestedRecycler : RecyclerView
    lateinit var recentJobRecycler : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_posted_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suggestedRecycler = view.findViewById(R.id.suggested_hotelier_job_recycler)

        suggestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        suggestedRecycler.setHasFixedSize(true)

        val listSuggeJobs = mutableListOf<SuggestedJobData>()
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))

        val suggestedJobAdapter = SuggestedJobAdapter(requireContext(), emptyList())
        suggestedRecycler.adapter = suggestedJobAdapter
        suggestedJobAdapter.notifyDataSetChanged()


        recentJobRecycler = view.findViewById(R.id.recent_job_recycler_hotelier)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        recentJobRecycler.setHasFixedSize(true)

        val recentJobAdapter = RecentJobAdapter(requireContext(), emptyList())
        recentJobRecycler.adapter = recentJobAdapter
        recentJobAdapter.notifyDataSetChanged()
    }
}