package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
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

    private var postList:ArrayList<PostItem> = ArrayList()
    private lateinit var progress: ProgressBar

    private var currentPage = 1
    private var isLoading:Boolean = false

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
        //media //recyclerView. //recyclerView.setHasFixedSize(true)

        mediaAdapter = MediaAdapter(requireContext(),postList, true, "Connected")
        mediaRecyclerView.adapter = mediaAdapter

        progress = view.findViewById(R.id.progress)

        mediaRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("working","okk")
                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    Log.e("working","okk")
//                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
                        isLoading = false
                        getData()


//                    }
                }


            }
        })

        empty = view.findViewById(R.id.empty)
        emptyImage = view.findViewById(R.id.emptyImage)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        getExplorePost()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getExplorePost()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getExplorePost() {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getExplorePost = RetrofitBuilder.ProfileApis.getSavedPost(user_id,"$currentPage")

        getExplorePost.enqueue(object : Callback<List<PostsDataClass>?> {
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
                            response.forEach {

                                currentPage++
                                postList.addAll(it.posts)

                                mediaAdapter.removePostsFromList(postList)
                                mediaAdapter.notifyDataSetChanged()

                            }
                            } else {
                                empty.text = "You did'nt save post yet"
                                empty.visibility = View.VISIBLE
                            if (currentPage == 1) {
                                emptyImage.visibility = View.VISIBLE
                                mediaRecyclerView.visibility = View.GONE
                            }
                            }
                    } else {
                        empty.visibility = View.VISIBLE
                        if (currentPage == 1) {
                            emptyImage.visibility = View.VISIBLE
                            mediaRecyclerView.visibility = View.GONE
                        }
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again ${t.localizedMessage}"
//                empty.visibility = View.VISIBLE
                if (currentPage == 1) {
                    emptyImage.visibility = View.VISIBLE
                }

            }
        })

    }

}