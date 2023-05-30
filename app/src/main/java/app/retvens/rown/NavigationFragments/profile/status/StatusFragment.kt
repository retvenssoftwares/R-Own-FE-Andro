package app.retvens.rown.NavigationFragments.profile.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusFragment(val userId: String) : Fragment() {

    lateinit var statusRecycler : RecyclerView
    lateinit var statusAdapter: StatusAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusRecycler = view.findViewById(R.id.statusRecycler)

        statusRecycler.layoutManager = LinearLayoutManager(context)
        statusRecycler.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)



        getMedia(userId)
    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getNormalUserStatus(userId, "1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        val response = response.body()!!
                        if (response.isNotEmpty()) {

                            response.forEach { postsDataClass ->

                        try {
                            statusAdapter = StatusAdapter(postsDataClass.posts, requireContext())
                            statusRecycler.adapter = statusAdapter
                            statusAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){
                            empty.text = "You did'nt share your status yet"
                            empty.visibility = View.VISIBLE
                        }



                    }
                        } else {
                            empty.text = "You did'nt share your status yet"
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

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                if (isAdded) {
                    empty.visibility = View.VISIBLE
                    empty.text = "${t.localizedMessage}"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
//                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        })
    }
}