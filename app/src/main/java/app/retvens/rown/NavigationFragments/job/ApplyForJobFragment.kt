package app.retvens.rown.NavigationFragments.job

import AppliedJobs
import UserJobAppliedData
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.explorejob.AppliedJobAdapter
import app.retvens.rown.DataCollections.JobsCollection.AppliedJobData
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.Job
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class ApplyForJobFragment : Fragment() {

    lateinit var appliedRecyclerView: RecyclerView
    lateinit var appliedJobAdapter: AppliedJobAdapter
    lateinit var searchBar:EditText
    private var isLoading = false
    private var currentPage = 1
    private lateinit var progress: ProgressBar
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var jobList:ArrayList<AppliedJobs> = ArrayList()
    lateinit var empty : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_for_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appliedForAJob()

        empty = view.findViewById(R.id.empty)
        searchBar = view.findViewById(R.id.hots_search)
        progress = view.findViewById(R.id.progress)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)



        appliedRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isLoading = false
                currentPage++
                getData()
            }
        })
        appliedJobList()
    }

    private fun appliedForAJob() {
        appliedRecyclerView = requireView().findViewById(R.id.applied_recycler)
        appliedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        appliedJobAdapter = AppliedJobAdapter(requireContext(), jobList)
        appliedRecyclerView.adapter = appliedJobAdapter


    }

    private fun appliedJobList() {
        val sharedPreferences =
            context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = RetrofitBuilder.jobsApis.userAppliedJobs(currentPage,user_id)

       data.enqueue(object : Callback<List<UserJobAppliedData>?> {
           override fun onResponse(
               call: Call<List<UserJobAppliedData>?>,
               response: Response<List<UserJobAppliedData>?>
           ) {
               try {
                   if (response.isSuccessful && isAdded){
                       val response = response.body()!!
                       val original = response.toList()

                       response.forEach{userJobAppliedData ->
                           jobList.addAll(userJobAppliedData.jobs)
                           shimmerFrameLayout.visibility = View.GONE
                           appliedJobAdapter.notifyDataSetChanged()
                       }

                       searchBar.addTextChangedListener(object :TextWatcher{
                           override fun beforeTextChanged(
                               p0: CharSequence?,
                               p1: Int,
                               p2: Int,
                               p3: Int
                           ) {

                           }

                           override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                               val searchText = p0.toString().trim()
                               filterJobs(searchText)
                           }

                           override fun afterTextChanged(p0: Editable?) {

                           }

                       })

                   }else {
                       empty.text = "You didn't post a job yet"
                       empty.visibility = View.VISIBLE
                   }
               }catch (e:NullPointerException){
               }

           }

           override fun onFailure(call: Call<List<UserJobAppliedData>?>, t: Throwable) {
               if (isAdded) {
                   empty.visibility = View.VISIBLE
                   empty.text = "Try Again : Check your internet"
                   //shimmerFrameLayout.stopShimmer()
                   shimmerFrameLayout.visibility = View.GONE
               }
           }
       })
    }

    private fun getData() {
        val handler = Handler()

        progress.visibility = View.VISIBLE
        handler.postDelayed({
            appliedJobList()
            progress.visibility = View.GONE
            isLoading = false
        },
            2000)
    }

    // Update your filterJobs function like this
    private fun filterJobs(searchText: String) {
        val filteredList = ArrayList<AppliedJobs>()

        // If the search text is empty, show all jobs
        if (searchText.isEmpty()) {
            filteredList.addAll(jobList)
        } else {
            // Filter jobs based on the search text
            for (job in jobList) {
                if (job.jobTitle.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.ROOT)) ||
                    job.jobLocation.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.ROOT)) ||
                    job.companyName.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.ROOT))
                ) {
                    filteredList.add(job)
                }
            }
        }

        // Check if the filtered list is empty
        if (filteredList.isEmpty()) {
            // If no results found
            //println("No Results Found")
        }

        // Update the adapter with the filtered data
        appliedJobAdapter.filterList(filteredList)
    }

}