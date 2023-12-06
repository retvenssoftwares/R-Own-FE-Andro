package app.retvens.rown.NavigationFragments.jobforvendors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout

class explore_employees_fragment : Fragment() {

    lateinit var matchesRecycler : RecyclerView

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_employees, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val listSuggeJobs = mutableListOf<SuggestedJobData>()
//        listSuggeJobs.add(SuggestedJobData("Arjun Gupta"))
//        listSuggeJobs.add(SuggestedJobData("Rahul Bhalerao"))
//        listSuggeJobs.add(SuggestedJobData("Aman Sharma"))
//        listSuggeJobs.add(SuggestedJobData("Shivam Tiwari"))
//        listSuggeJobs.add(SuggestedJobData("Rahul Bhalerao"))
//        listSuggeJobs.add(SuggestedJobData("Aman Sharma"))
//        listSuggeJobs.add(SuggestedJobData("Arjun Gupta"))
//        listSuggeJobs.add(SuggestedJobData("Rahul Bhalerao"))
//        listSuggeJobs.add(SuggestedJobData("Aman Sharma"))
//        listSuggeJobs.add(SuggestedJobData("Shivam Tiwari"))
//
//        matchesRecycler = view.findViewById(R.id.matches_employees_recycler)
//        matchesRecycler.layoutManager = LinearLayoutManager(context)
//        matchesRecycler. //recyclerView.setHasFixedSize(true)
//        val matchesJobAdapter = MatchesJobAdapter(listSuggeJobs, requireContext())
//        matchesRecycler.adapter = matchesJobAdapter
//        matchesJobAdapter.notifyDataSetChanged()

    }
}