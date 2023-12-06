package app.retvens.rown.NavigationFragments.exploreForUsers.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreEventFragment : Fragment() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreEventAdapter : ExploreEventsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_events_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        //exploreBlogs //recyclerView. //recyclerView.setHasFixedSize(true)


        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        getEvents()

    }

    private fun getEvents() {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getEvent = RetrofitBuilder.exploreApis.getExploreEvent(user_id, "1")
        getEvent.enqueue(object : Callback<List<ExploreEventData>?> {
            override fun onResponse(
                call: Call<List<ExploreEventData>?>,
                response: Response<List<ExploreEventData>?>
            ) {

                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {

                                try {
                                    exploreEventAdapter = ExploreEventsAdapter(it.events, requireContext())
                                    exploreBlogsRecyclerView.adapter = exploreEventAdapter
                                    exploreEventAdapter.notifyDataSetChanged()
                                }catch (e:NullPointerException){
                                    Log.e("error",e.message.toString())
                                }
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

            }

            override fun onFailure(call: Call<List<ExploreEventData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                empty.visibility = View.VISIBLE
            }
        })
    }
}