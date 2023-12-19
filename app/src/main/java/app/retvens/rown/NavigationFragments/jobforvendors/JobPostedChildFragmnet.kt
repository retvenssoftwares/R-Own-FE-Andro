package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.job.*
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetJobFilter
import app.retvens.rown.utils.role
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobPostedChildFragmnet : Fragment(), BottomSheetJobFilter.OnBottomJobClickListener {

    lateinit var suggestedRecycler : RecyclerView
    lateinit var recentJobRecycler : RecyclerView
    lateinit var searchBar:EditText

    lateinit var shimmerLayout: LinearLayout
    lateinit var nothing :ImageView
    lateinit var filter :ImageView
    lateinit var relative_jobs :RelativeLayout



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
        searchBar = view.findViewById(R.id.jobs_search_hoteliers)
        nothing = view.findViewById<ImageView>(R.id.nothing)
        filter = view.findViewById<ImageView>(R.id.filter)

        shimmerLayout = view.findViewById(R.id.shimmer_layout_tasks)
        relative_jobs = view.findViewById(R.id.relative_jobs)





        viewAll.setOnClickListener {
            startActivity(Intent(requireContext(),JobsPostedByUser::class.java))
        }

        suggestedRecycler = view.findViewById(R.id.suggested_hotelier_job_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //suggestedRecycler. //recyclerView.setHasFixedSize(true)

        filter.setOnClickListener {
            val bottomSheet = BottomSheetJobFilter()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobFilter.Job_TAG)}
            bottomSheet.setOnJobClickListener(this)
        }

        if ( role == "Hotel Owner") {

            filter.visibility = View.GONE

        }
        else{

            filter.visibility = View.VISIBLE
        }


        recentJobRecycler = view.findViewById(R.id.recent_job_recycler_hotelier)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        //recentJobRecycler. //recyclerView.setHasFixedSize(true)

        getJobs()
    }

    private fun getJobs() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getJob = RetrofitBuilder.jobsApis.getIndividualJobs(user_id)

        getJob.enqueue(object : Callback<GetJobData?> {
            override fun onResponse(call: Call<GetJobData?>, response: Response<GetJobData?>) {


                if (response.code()==200 && isAdded){
                    shimmerLayout.visibility = View.GONE
                    if (response.body()?.userJobs != null) {


                        val suggestedJobAdapter = SuggestedJobAdaperHotelOwner(requireContext(), response.body()!!.userJobs)
                            suggestedRecycler.adapter = suggestedJobAdapter
                            suggestedJobAdapter.notifyDataSetChanged()

                        val recentJobAdapter = RecentJobAdapterOwner(requireContext(), response.body()!!.userJobs)
                            recentJobRecycler.adapter = recentJobAdapter
                            recentJobAdapter.notifyDataSetChanged()

                            searchBar.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {
                                }
                                override fun onTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {
                                    val filterData = response.body()!!.userJobs.filter { item ->
                                        item.jobTitle.contains(p0.toString(), ignoreCase = true)
                                    }

                                    suggestedJobAdapter.updateData(filterData)

                                    val filterData2 = response.body()!!.userJobs.filter { item ->
                                        item.jobTitle.contains(p0.toString(), ignoreCase = true)
                                    }
                                    recentJobAdapter.updateData(filterData2)
                                }

                                override fun afterTextChanged(p0: Editable?) {

                                }

                            })


                        } else {
                            nothing.visibility = View.VISIBLE
                            relative_jobs.visibility = View.GONE

                        }

                }else{
                    if (isAdded) {
                        shimmerLayout.visibility = View.GONE
                        nothing.visibility = View.VISIBLE
                        relative_jobs.visibility = View.GONE
                        Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<GetJobData?>, t: Throwable) {
                Log.d("sdfghjkldfghj", "onFailure: "+t.message)
                shimmerLayout.visibility = View.GONE
                if (isAdded) {
                   Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                  .show()
                }
            }
        })
    }

//    override fun bottomJobClick(jobFrBo: String) {
//
//    }

    override fun bottomJobClick(category: String, type: String, location: String, salary: String) {

    }
}