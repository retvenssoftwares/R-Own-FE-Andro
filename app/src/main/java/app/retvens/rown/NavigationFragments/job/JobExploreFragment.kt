package app.retvens.rown.NavigationFragments.job

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.FilterDataClass
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
    lateinit var searchBar : EditText
    private lateinit var filterlist:FilterDataClass


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
        searchBar = view.findViewById(R.id.jobs_search)

        filterlist = FilterDataClass("","","","")

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


        getJobs()

        recentJobRecycler = requireView().findViewById(R.id.recent_job_recycler)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        recentJobRecycler.setHasFixedSize(true)

    }


    private fun getJobs() {
        val getJob = RetrofitBuilder.jobsApis.getJobs()

        getJob.enqueue(object : Callback<List<JobsData>?>,
            SuggestedJobAdapter.JobSavedClickListener {
            override fun onResponse(
                call: Call<List<JobsData>?>,
                response: Response<List<JobsData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    response.forEach { it ->

                        if (it.jobTitle == filterlist.category || it.jobType == filterlist.type || it.expectedCTC == filterlist.salary || it.jobLocation == filterlist.location){
                            val originalData = response.toList()
                            val suggestedJobAdapter = SuggestedJobAdapter(requireContext(),response)
                            suggestedRecycler.adapter = suggestedJobAdapter
                            suggestedJobAdapter.notifyDataSetChanged()

                            suggestedJobAdapter.setJobSavedClickListener(this)


                            val recentJobAdapter = RecentJobAdapter(requireContext(), response)
                            recentJobRecycler.visibility = View.VISIBLE
                            shimmerLayout.visibility = View.GONE
                            recentJobRecycler.adapter = recentJobAdapter
                            recentJobAdapter.notifyDataSetChanged()




                            searchBar.addTextChangedListener(object :TextWatcher{
                                override fun beforeTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {

                                }

                                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                    val filterData = originalData.filter { item ->
                                        item.jobTitle.contains(p0.toString(),ignoreCase = true)
                                    }

                                    suggestedJobAdapter.updateData(filterData)

                                    val filterData1 = originalData.filter { item ->
                                        item.jobTitle.contains(p0.toString(),ignoreCase = true)
                                    }

                                    suggestedJobAdapter.updateData(filterData1)

                                }

                                override fun afterTextChanged(p0: Editable?) {

                                }

                            })
                        }

                    }


                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JobsData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onJobSavedClick(job: JobsData) {

            }
        })
    }

    override fun bottomJobClick(category: String, type: String, location: String, salary: String) {
            filterlist?.category = category
            filterlist?.type = type
            filterlist?.salary = salary
            filterlist?.location = location

            filterData()

    }

    private fun filterData() {

        val card1 = view?.findViewById<CardView>(R.id.filter_card1)
        val card2 =  view?.findViewById<CardView>(R.id.filter_card2)
        val card3 =  view?.findViewById<CardView>(R.id.filter_card3)
        val card4 =  view?.findViewById<CardView>(R.id.filter_card4)

        val filterJob = view?.findViewById<TextView>(R.id.filter_job)
        val type = view?.findViewById<TextView>(R.id.filter_type)
        val location = view?.findViewById<TextView>(R.id.filter_location)
        val salary = view?.findViewById<TextView>(R.id.filter_salary)

        filterJob?.text = filterlist.category
        type?.text = filterlist.type

        location?.text = filterlist.location
        salary?.text = filterlist.salary

        getJobs()

        try {
            if (filterJob!!.text.isNotEmpty()){
                card1?.visibility = View.VISIBLE
            }else if (type!!.text.isNotEmpty()) {
                card2?.visibility = View.VISIBLE
            }else if (location!!.text.isNotEmpty()){
                card3?.visibility = View.VISIBLE
            }else if (salary!!.text.isNotEmpty()){
                card4?.visibility = View.VISIBLE
            }
        }catch (e:java.lang.NullPointerException){

        }



    }


}