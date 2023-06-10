package app.retvens.rown.NavigationFragments.exploreForUsers.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreServicesFragment : Fragment() {


    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreServicesAdapter: ExploreServicesAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        exploreBlogsRecyclerView = view.findViewById(R.id.explore_services_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        getServices()
    }

    private fun getServices() {
        val serv = RetrofitBuilder.exploreApis.getExploreService("2")
        serv.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {
                try {
                    if (isAdded){
                        if (response.isSuccessful){
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                                val data = response.body()!!
                                data.forEach {
                                    exploreServicesAdapter = ExploreServicesAdapter(it.events, requireContext())
                                    exploreBlogsRecyclerView.adapter = exploreServicesAdapter
                                    exploreServicesAdapter.notifyDataSetChanged()
                                }
                            } else {
                                empty.visibility = View.VISIBLE
                            }
                        } else {
                            empty.visibility = View.VISIBLE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    }
                }catch (e:NullPointerException){
                    Toast.makeText(requireContext(),"No Services Yet",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                empty.visibility = View.VISIBLE
            }
        })
    }
}