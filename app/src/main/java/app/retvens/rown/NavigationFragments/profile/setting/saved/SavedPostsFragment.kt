package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedPostsFragment : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var savedPostsAdapter: SavedPostsAdapter

    lateinit var mediaAdapter: MediaAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var empty : TextView
    lateinit var emptyImage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mediaRecyclerView = view.findViewById(R.id.mediaSavedRecyclerView)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)


        empty = view.findViewById(R.id.empty)
        emptyImage = view.findViewById(R.id.emptyImage)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        val blogs = listOf<MediaData>(
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_awards),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_awards),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
        )

//        savedPostsAdapter = SavedPostsAdapter(blogs, requireContext())
//        mediaRecyclerView.adapter = savedPostsAdapter
//        savedPostsAdapter.notifyDataSetChanged()

        getExplorePost()
    }

    private fun getExplorePost() {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getExplorePost = RetrofitBuilder.ProfileApis.getSavedPost(user_id,"1")

        getExplorePost.enqueue(object : Callback<PostsDataClass?> {
            override fun onResponse(
                call: Call<PostsDataClass?>,
                response: Response<PostsDataClass?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

//                        if (response.body()!!.isNotEmpty()) {
                            val response = response.body()!!

                            if (response.message != "You have reached the end") {
                                mediaAdapter = MediaAdapter(
                                    requireContext(),
                                    response.posts as ArrayList<PostItem>
                                )
                                mediaRecyclerView.adapter = mediaAdapter
                                mediaAdapter.removePostsFromList(response.posts)
                                mediaAdapter.notifyDataSetChanged()
                            } else {
                                empty.text = "You did'nt save post yet"
//                                empty.visibility = View.VISIBLE
                                emptyImage.visibility = View.VISIBLE
                            }

                           /*
                            val originalData = response.toList()
                            response.forEach { postsDataClass ->

                                if (postsDataClass.message != "You have reached the end") {
                                    mediaAdapter = MediaAdapter(
                                        requireContext(),
                                        postsDataClass.posts as ArrayList<PostItem>
                                    )
                                    mediaRecyclerView.adapter = mediaAdapter
                                    mediaAdapter.removePostsFromList(postsDataClass.posts)
                                    mediaAdapter.notifyDataSetChanged()
                                } else {
                                    empty.text = "You did'nt save post yet"
                                    empty.visibility = View.VISIBLE
                                }
                            }*/

//                        } else {
//                            empty.text = "You did'nt save post yet"
//                            empty.visibility = View.VISIBLE
//                        }
                    } else {
//                        empty.visibility = View.VISIBLE
                        emptyImage.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<PostsDataClass?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again ${t.localizedMessage}"
//                empty.visibility = View.VISIBLE
                emptyImage.visibility = View.VISIBLE
            }
        })

    }

}