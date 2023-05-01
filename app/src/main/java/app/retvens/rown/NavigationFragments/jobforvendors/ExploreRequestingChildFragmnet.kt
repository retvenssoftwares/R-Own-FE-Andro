package app.retvens.rown.NavigationFragments.jobforvendors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R


class ExploreRequestingChildFragmnet : Fragment() {

    lateinit var popularRecycler : RecyclerView
    lateinit var matchesRecycler : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_requesting_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularRecycler = view.findViewById(R.id.popular_fields_recycler)
        popularRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularRecycler.setHasFixedSize(true)

        val listSuggeJobs = mutableListOf<SuggestedJobData>()
        listSuggeJobs.add(SuggestedJobData("Arjun Gupta"))
        listSuggeJobs.add(SuggestedJobData("Rahul Bhalerao"))
        listSuggeJobs.add(SuggestedJobData("Aman Sharma"))
        listSuggeJobs.add(SuggestedJobData("Shivam Tiwari"))

        val popularFieldsAdapter = PopularFieldsAdapter(listSuggeJobs, requireContext())
        popularRecycler.adapter = popularFieldsAdapter
        popularFieldsAdapter.notifyDataSetChanged()

        matchesRecycler = view.findViewById(R.id.matches_recycler)
        matchesRecycler.layoutManager = LinearLayoutManager(context)
        matchesRecycler.setHasFixedSize(true)
        val matchesJobAdapter = MatchesJobAdapter(listSuggeJobs, requireContext())
        matchesRecycler.adapter = matchesJobAdapter
        matchesJobAdapter.notifyDataSetChanged()

    }
}