package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.job.RecentJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val viewAll = view.findViewById<TextView>(R.id.view_all_recent)

        viewAll.setOnClickListener {
            startActivity(Intent(requireContext(),JobsPostedByUser::class.java))
        }

        suggestedRecycler = view.findViewById(R.id.suggested_hotelier_job_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        suggestedRecycler.setHasFixedSize(true)



        recentJobRecycler = view.findViewById(R.id.recent_job_recycler_hotelier)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        recentJobRecycler.setHasFixedSize(true)

        getJobs()
    }

    private fun getJobs() {

        val getJob = RetrofitBuilder.jobsApis.getJobs()

        getJob.enqueue(object : Callback<List<JobsData>?> {
            override fun onResponse(
                call: Call<List<JobsData>?>,
                response: Response<List<JobsData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    val suggestedJobAdapter = SuggestedJobAdapter(requireContext(),response)
                    suggestedRecycler.adapter = suggestedJobAdapter
                    suggestedJobAdapter.notifyDataSetChanged()

                    val recentJobAdapter = RecentJobAdapter(requireContext(), response)
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
}