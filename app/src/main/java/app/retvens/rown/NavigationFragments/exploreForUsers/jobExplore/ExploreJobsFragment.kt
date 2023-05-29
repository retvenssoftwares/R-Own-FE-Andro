package app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreJobsFragment : Fragment() {

    lateinit var exploreJobsRecyclerView: RecyclerView
    lateinit var exploreJobAdapter: ExploreJobAdapter
    lateinit var searchBar:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreJobsRecyclerView = view.findViewById(R.id.explore_jobs_recycler)
        exploreJobsRecyclerView.layoutManager = LinearLayoutManager(context)
        exploreJobsRecyclerView.setHasFixedSize(true)


        searchBar = view.findViewById(R.id.search_explore_jobs)


        getExploreJob()

    }

    private fun getExploreJob() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getJob = RetrofitBuilder.exploreApis.getExploreJob("1")

        getJob.enqueue(object : Callback<List<ExploreJobData>?> {
            override fun onResponse(
                call: Call<List<ExploreJobData>?>,
                response: Response<List<ExploreJobData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    response.forEach { jobsData ->
                        exploreJobAdapter = ExploreJobAdapter(jobsData.posts, requireContext())
                        exploreJobsRecyclerView.adapter = exploreJobAdapter
                        exploreJobAdapter.notifyDataSetChanged()
                        
                        searchBar.addTextChangedListener(object : TextWatcher{
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
                                
                                searchJob(p0.toString())
                                
                            }

                            override fun afterTextChanged(p0: Editable?) {
                                
                            }

                        })

                    }


                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ExploreJobData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun searchJob(text: String) {

       val searchJob = RetrofitBuilder.exploreApis.searchJob(text,"1")

        searchJob.enqueue(object : Callback<List<ExploreJobData>?> {
            override fun onResponse(
                call: Call<List<ExploreJobData>?>,
                response: Response<List<ExploreJobData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach { item ->

                        try {
                            exploreJobAdapter = ExploreJobAdapter(item.posts, requireContext())
                            exploreJobsRecyclerView.adapter = exploreJobAdapter
                            exploreJobAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){

                        }



                    }
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ExploreJobData>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }


}