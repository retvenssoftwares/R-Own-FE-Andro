package app.retvens.rown.NavigationFragments.exploreForUsers

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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
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

class ExplorePostsFragment : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var mediaAdapter: MediaAdapter
    lateinit var searchBar:EditText
    private var currentPage = 1
    private var totalPages = 0
    private var isLoading = false
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var postList:ArrayList<PostItem> = ArrayList()
    private lateinit var progress:ProgressBar

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaRecyclerView = view.findViewById(R.id.explore_posts_recycler)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)

        mediaRecyclerView.setNestedScrollingEnabled(true)

        mediaAdapter = MediaAdapter(requireContext(),postList, false, "Connected")
        mediaRecyclerView.adapter = mediaAdapter
        mediaAdapter.removePostsFromList(postList)
        mediaAdapter.notifyDataSetChanged()

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        searchBar = view.findViewById(R.id.search_explore_posts)
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
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
                        isLoading = false
                        currentPage++
                        getData()


                    }
                }


            }
        })

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

        val getExplorePost = RetrofitBuilder.exploreApis.getExplorePost(user_id,currentPage.toString())

        Log.e("page",currentPage.toString())

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
                            Log.e("res",response.toString())
                            val originalData = response.toList()
                            response.forEach { postsDataClass ->
                        totalPages = postsDataClass.pageSize

                                postsDataClass.posts.forEach { post ->
                                    if (post.user_id != user_id){
                                        postList.add(post)
                                    }
                                }

                        mediaAdapter.removePostsFromList(postsDataClass.posts)
                        mediaAdapter.notifyDataSetChanged()
                        searchBar.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {


                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                                val letter = p0.toString()
                                if (letter != ""){
                                    searchPost(letter)
                                }else{
                                    mediaAdapter.notifyDataSetChanged()
                                }


                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }

                        })

                    }

                } else {
                            empty.text = "You did'nt post yet"
//                            empty.visibility = View.VISIBLE
                        }
            } else {
//                        empty.visibility = View.VISIBLE
//                        mediaRecyclerView.visibility = View.GONE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
//                empty.text = "Try Again"
//                empty.visibility = View.VISIBLE
//                mediaRecyclerView.visibility = View.GONE
                Log.e("error",t.message.toString())
            }
        })

    }

    private fun searchPost(letter: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val searchPost = RetrofitBuilder.exploreApis.searchPost(letter,user_id,"1")

        searchPost.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!

                    response.forEach { postsDataClass ->
                        if (postsDataClass.message == "You have reached the end"){
                            mediaAdapter = MediaAdapter(requireContext(), ArrayList(),false, "Connected")
                            mediaRecyclerView.adapter = mediaAdapter
                            mediaAdapter.notifyDataSetChanged()
                        }else{
                            mediaAdapter = MediaAdapter(requireContext(), postsDataClass.posts as ArrayList<PostItem>,false, "Connected")
                            mediaRecyclerView.adapter = mediaAdapter
                            mediaAdapter.removePostsFromList(postsDataClass.posts)
                            mediaAdapter.notifyDataSetChanged()
                        }

                    }


                }else{
                    Log.e("error",response.code().toString())
                    getExplorePost()
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                mediaAdapter = MediaAdapter(requireContext(), ArrayList(), false, "Connected")
                mediaAdapter.notifyDataSetChanged()
            }
        })


    }


}