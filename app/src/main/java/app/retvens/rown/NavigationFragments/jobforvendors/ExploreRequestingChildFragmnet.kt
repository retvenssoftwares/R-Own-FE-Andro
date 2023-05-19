package app.retvens.rown.NavigationFragments.jobforvendors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.GetRequestedJobDara
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreRequestingChildFragmnet : Fragment() {

    lateinit var popularRecycler : RecyclerView
    lateinit var matchesRecycler : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_requesting_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularRecycler = view.findViewById(R.id.popular_fields_recycler)
        popularRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularRecycler.setHasFixedSize(true)





        matchesRecycler = view.findViewById(R.id.matches_recycler)
        matchesRecycler.layoutManager = LinearLayoutManager(context)
        matchesRecycler.setHasFixedSize(true)


        getRequedtedList()
    }

    private fun getRequedtedList() {

        val getData = RetrofitBuilder.jobsApis.getRequestJob()

        getData.enqueue(object : Callback<List<GetRequestedJobDara>?> {
            override fun onResponse(
                call: Call<List<GetRequestedJobDara>?>,
                response: Response<List<GetRequestedJobDara>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    val popularFieldsAdapter = PopularFieldsAdapter(requireContext(),response)
                    popularRecycler.adapter = popularFieldsAdapter
                    popularFieldsAdapter.notifyDataSetChanged()

                    val matchesJobAdapter = MatchesJobAdapter(requireContext(),response)
                    matchesRecycler.adapter = matchesJobAdapter
                    matchesJobAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetRequestedJobDara>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }
}