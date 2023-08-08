package app.retvens.rown.NavigationFragments.job

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    lateinit var searchBar:EditText
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

        searchBar = view.findViewById(R.id.hots_search)
    }

    private fun appliedForAJob() {
        appliedRecyclerView = requireView().findViewById(R.id.applied_recycler)
        appliedRecyclerView.layoutManager = LinearLayoutManager(context)
       // applied //recyclerView. //recyclerView.setHasFixedSize(true)



        appliedJobList()
    }

    private fun appliedJobList() {
        val sharedPreferences = requireContext()?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = RetrofitBuilder.jobsApis.appliedJobs("$user_id")

       data.enqueue(object : Callback<List<AppliedJobData>?> {
           override fun onResponse(
               call: Call<List<AppliedJobData>?>,
               response: Response<List<AppliedJobData>?>
           ) {
               if (response.isSuccessful && isAdded){
                   val response = response.body()!!
                   val original = response.toList()
                   shimmerLayout.visibility = View.GONE
                   val appliedJobAdapter = AppliedJobAdapter(requireContext(), response)
                   appliedRecyclerView.adapter = appliedJobAdapter
                   appliedJobAdapter.notifyDataSetChanged()

                   searchBar.addTextChangedListener(object :TextWatcher{
                       override fun beforeTextChanged(
                           p0: CharSequence?,
                           p1: Int,
                           p2: Int,
                           p3: Int
                       ) {

                       }

                       override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                           val filterData = original.filter { item ->
                               item.jobData.jobTitle.contains(p0.toString(),ignoreCase = true)
                           }

                           appliedJobAdapter.updateData(filterData)
                       }

                       override fun afterTextChanged(p0: Editable?) {

                       }

                   })

               }else {
                   if (isAdded) {
                       Toast.makeText(
                           requireContext(),
                           response.code().toString(),
                           Toast.LENGTH_SHORT
                       ).show()
                   }
               }
           }

           override fun onFailure(call: Call<List<AppliedJobData>?>, t: Throwable) {
           }
       })
    }

}