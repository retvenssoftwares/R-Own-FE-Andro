package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class SavedEventsFragment : Fragment() {

    lateinit var savedEventsRecyclerView: RecyclerView
    lateinit var savedEventsAdapter: SavedEventsAdapter

    lateinit var exploreEventAdapter : ExploreEventsAdapter
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedEventsRecyclerView = view.findViewById(R.id.savedEventsRecyclerView)
        savedEventsRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedEventsRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        getEvents()

        val blogs = listOf<ExploreEventsData>(
            ExploreEventsData("Radiohead Concert"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Neck Deep Concert"),
            ExploreEventsData("Title 23"),
            ExploreEventsData("Title 1"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Title 3"),
            ExploreEventsData("Title 23"),
            ExploreEventsData("Title 1"),
            ExploreEventsData("Title 2"),
            ExploreEventsData("Title 3"),
            ExploreEventsData("Title 23"),
        )

//        savedEventsAdapter = SavedEventsAdapter(blogs, requireContext())
//        savedEventsRecyclerView.adapter = savedEventsAdapter
//        savedEventsAdapter.notifyDataSetChanged()

    }
    private fun getEvents() {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getEvent = RetrofitBuilder.ProfileApis.getSavedEvent(user_id, "1")
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
                            try{
                            val data = response.body()!!
                            data.forEach {
                                exploreEventAdapter =
                                    ExploreEventsAdapter(it.events, requireContext())
                                savedEventsRecyclerView.adapter = exploreEventAdapter
                                exploreEventAdapter.notifyDataSetChanged()
                            }
                            } catch ( e : Exception){
                                empty.visibility = View.VISIBLE
                                empty.text = e.toString()
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