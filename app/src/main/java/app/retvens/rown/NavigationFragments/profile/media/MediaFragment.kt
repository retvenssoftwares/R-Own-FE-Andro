package app.retvens.rown.NavigationFragments.profile.media

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaFragment(val userId: String) : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var mediaAdapter: MediaAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaRecyclerView = view.findViewById(R.id.mediaRecyclerView)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)


        getMedia(userId)

    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getUserProfileMedia(userId,"1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                    val response = response.body()!!

                    response.forEach { postsDataClass ->

                        try {
                            mediaAdapter = MediaAdapter(requireContext(),postsDataClass.posts)
                            mediaRecyclerView.adapter = mediaAdapter
                            mediaAdapter.notifyDataSetChanged()
                        }catch (e : NullPointerException){
                            empty.text = "No posts"
                            empty.visibility = View.VISIBLE
                        }



                    }
                        } else {
                            empty.text = "No posts"
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
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })



    }
}