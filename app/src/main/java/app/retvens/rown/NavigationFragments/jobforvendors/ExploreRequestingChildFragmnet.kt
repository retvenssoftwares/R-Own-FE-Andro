package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.GetRequestedJobDara
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreRequestingChildFragmnet : Fragment() {

    lateinit var popularRecycler : RecyclerView
    lateinit var matchesRecycler : RecyclerView

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

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
        //popularRecycler. //recyclerView.setHasFixedSize(true)


        empty = view.findViewById(R.id.empty)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)


        matchesRecycler = view.findViewById(R.id.matches_recycler)
        matchesRecycler.layoutManager = LinearLayoutManager(context)
        //matchesRecycler. //recyclerView.setHasFixedSize(true)

        val viewAll = view.findViewById<TextView>(R.id.viewAll_popular)

        viewAll.setOnClickListener {
            startActivity(Intent(requireContext(),ViewAllPolpular::class.java))
        }

        val viewAllMatches = view.findViewById<TextView>(R.id.view_all_suggested)

        viewAllMatches.setOnClickListener {
            startActivity(Intent(requireContext(),ViewAllMatches::class.java))
        }

        getRequedtedList()
    }

    private fun getRequedtedList() {

        val getData = RetrofitBuilder.jobsApis.getRequestJob()

        getData.enqueue(object : Callback<List<GetRequestedJobDara>?> {
            override fun onResponse(
                call: Call<List<GetRequestedJobDara>?>,
                response: Response<List<GetRequestedJobDara>?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        try {
                            val popularFieldsAdapter = PopularFieldsAdapter(requireContext(),response.body()!!)
                            popularRecycler.adapter = popularFieldsAdapter
                            popularFieldsAdapter.notifyDataSetChanged()

                            val matchesJobAdapter = MatchesJobAdapter(requireContext(),response.body()!!)
                            matchesRecycler.adapter = matchesJobAdapter
                            matchesJobAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {


                } else {
                        empty.visibility = View.VISIBLE
                }
                    } else {
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<GetRequestedJobDara>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })

    }
}