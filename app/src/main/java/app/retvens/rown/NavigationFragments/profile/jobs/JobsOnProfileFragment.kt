package app.retvens.rown.NavigationFragments.profile.jobs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.NavigationFragments.job.RecentJobAdapterOwner
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdaperHotelOwner
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsOnProfileFragment(val userId:String) : Fragment() {

    lateinit var exploreJobsRecyclerView: RecyclerView
    lateinit var exploreJobAdapter: ProfileJobAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView

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

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        getJobs(userId)

    }

    private fun getJobs(userId: String) {



        val getJob = RetrofitBuilder.jobsApis.getIndividualJobs(userId)

        getJob.enqueue(object : Callback<List<JobsData>?> {
            override fun onResponse(
                call: Call<List<JobsData>?>,
                response: Response<List<JobsData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        val response = response.body()!!
                        if (response.isNotEmpty()) {
                            exploreJobAdapter = ProfileJobAdapter(requireContext(), response)
                            exploreJobsRecyclerView.adapter = exploreJobAdapter
                            exploreJobAdapter.notifyDataSetChanged()
                        } else {
                            notPosted.visibility = View.VISIBLE
                         }
                    } else {
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<JobsData>?>, t: Throwable) {
                if (isAdded) {
                    empty.visibility = View.VISIBLE
                    empty.text = "Try Again : Check your internet"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                }
            }
        })
    }
}