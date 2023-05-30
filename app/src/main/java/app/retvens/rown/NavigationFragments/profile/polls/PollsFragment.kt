package app.retvens.rown.NavigationFragments.profile.polls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PollsFragment(val userId: String) : Fragment() {

    lateinit var pollsRecyclerView: RecyclerView
    lateinit var pollsAdapter: PollsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pollsRecyclerView = view.findViewById(R.id.pollsRecycler)
        pollsRecyclerView.layoutManager = LinearLayoutManager(context)
        pollsRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        getPolls(userId)

    }

    private fun getPolls(userId: String) {

        val getPoll = RetrofitBuilder.feedsApi.getNormalUserPoll(userId,"1")

        getPoll.enqueue(object : Callback<List<PostsDataClass>?> {
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

                            pollsAdapter = PollsAdapter(postsDataClass.posts, requireContext())
                            pollsRecyclerView.adapter = pollsAdapter
                            pollsAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){

                            empty.text = "You did'nt create a poll yet!"
                            empty.visibility = View.VISIBLE
                        }


                    }
                        } else {
                            empty.text = "You did'nt create a poll yet!"
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
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}