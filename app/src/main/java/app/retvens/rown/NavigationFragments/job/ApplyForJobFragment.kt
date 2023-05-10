package app.retvens.rown.NavigationFragments.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.explorejob.AppliedJobAdapter
import app.retvens.rown.DataCollections.JobsCollection.AppliedJobData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyForJobFragment : Fragment() {

    lateinit var appliedRecyclerView: RecyclerView

    lateinit var shimmerLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_for_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerLayout = view.findViewById(R.id.shimmer_layout_tasks)
        appliedForAJob()
    }

    private fun appliedForAJob() {
        appliedRecyclerView = requireView().findViewById(R.id.applied_recycler)
        appliedRecyclerView.layoutManager = LinearLayoutManager(context)
        appliedRecyclerView.setHasFixedSize(true)




        appliedJobList()
    }

    private fun appliedJobList() {
        val sharedPreferences = requireContext()?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = RetrofitBuilder.jobsApis.appliedJobs("$user_id")

        data.enqueue(object : Callback<AppliedJobData?> {
            override fun onResponse(
                call: Call<AppliedJobData?>,
                response: Response<AppliedJobData?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    shimmerLayout.visibility = View.GONE
                    val appliedJobAdapter = AppliedJobAdapter(requireContext(), response)
                    appliedRecyclerView.adapter = appliedJobAdapter
                    appliedJobAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(),"ok", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AppliedJobData?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}