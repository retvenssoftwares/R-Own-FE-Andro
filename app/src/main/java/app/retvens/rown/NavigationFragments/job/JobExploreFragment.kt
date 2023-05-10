package app.retvens.rown.NavigationFragments.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetJobFilter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobExploreFragment : Fragment(), BottomSheetJobFilter.OnBottomJobClickListener {

    lateinit var suggestedRecycler : RecyclerView
    lateinit var recentJobRecycler : RecyclerView
    lateinit var filter : ImageView


    lateinit var shimmerLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerLayout = view.findViewById(R.id.shimmer_layout_tasks)

        filter = requireView().findViewById(R.id.filter_user_jobs)
        filter.setOnClickListener {
            val bottomSheet = BottomSheetJobFilter()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobFilter.Job_TAG)}
            bottomSheet.setOnJobClickListener(this)
        }

        exploreAJob()

    }
    private fun exploreAJob() {

        suggestedRecycler = requireView().findViewById(R.id.suggested_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        suggestedRecycler.setHasFixedSize(true)

        val listSuggeJobs = mutableListOf<SuggestedJobData>()
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))

        getJobs()

        recentJobRecycler = requireView().findViewById(R.id.recent_job_recycler)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        recentJobRecycler.setHasFixedSize(true)

    }


    private fun getJobs() {
        val getJob = RetrofitBuilder.jobsApis.getJobs()

        getJob.enqueue(object : Callback<List<JobsData>?> {
            override fun onResponse(
                call: Call<List<JobsData>?>,
                response: Response<List<JobsData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    val suggestedJobAdapter = SuggestedJobAdapter(requireContext(),response)
                    suggestedRecycler.adapter = suggestedJobAdapter
                    suggestedJobAdapter.notifyDataSetChanged()

                    val recentJobAdapter = RecentJobAdapter(requireContext(), response)
                    recentJobRecycler.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                    recentJobRecycler.adapter = recentJobAdapter
                    recentJobAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JobsData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun bottomJobClick(jobFrBo: String) {

    }
}