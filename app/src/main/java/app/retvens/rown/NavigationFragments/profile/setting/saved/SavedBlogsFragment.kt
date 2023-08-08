package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogData
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsAdapter
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SavedBlogsFragment : Fragment() {

lateinit var savedBlogsRecyclerView : RecyclerView
lateinit var allBlogsAdapter: SavedBlogsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private var currentPage = 1

    private lateinit var progress: ProgressBar
    private var isLoading:Boolean = false
    private var blogList:ArrayList<AllBlogsData> = ArrayList()

    lateinit var empty : TextView
    lateinit var emptyImage : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_blogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        empty = view.findViewById(R.id.emptyBlog)
        emptyImage = view.findViewById(R.id.emptyImage)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        savedBlogsRecyclerView = view.findViewById(R.id.savedBlogsRecyclerView)
        savedBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        //savedBlogs //recyclerView. //recyclerView.setHasFixedSize(true)

        progress = view.findViewById(R.id.progress)

        allBlogsAdapter = SavedBlogsAdapter(blogList, requireContext())
        savedBlogsRecyclerView.adapter = allBlogsAdapter

        getAllBlogs()

        savedBlogsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (isAdded && isLoading && (lastVisibleItemPosition == totalItem-1)){
                            isLoading = false
                            getData()
                    }
                }


            }
        })

//        savedBlogsAdapter = SavedBlogsAdapter(blogs, requireContext())
//        savedBlogsRecyclerView.adapter = savedBlogsAdapter
//        savedBlogsAdapter.notifyDataSetChanged()

    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            if (isAdded) {
                getAllBlogs()
                progress.setVisibility(View.GONE);
            }
        },
            2000)
    }

    private fun getAllBlogs() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val allBlogs = RetrofitBuilder.ProfileApis.getSavedBlog(user_id,"$currentPage")
        allBlogs.enqueue(object : Callback<List<ExploreBlogData>?> {
            override fun onResponse(
                call: Call<List<ExploreBlogData>?>,
                response: Response<List<ExploreBlogData>?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {
                                blogList.addAll(it.blogs)
                                currentPage++
                                allBlogsAdapter.removeBlogFromList(it.blogs)
                                allBlogsAdapter.notifyDataSetChanged()
                                Log.d("on", it.toString())
                            }
                        } else {
//                            empty.visibility = View.VISIBLE
                            if (currentPage == 1) {
                                emptyImage.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        if (currentPage == 1) {
//                        empty.visibility = View.VISIBLE
                            emptyImage.visibility = View.VISIBLE
                            empty.text = response.code().toString()
                        }
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<ExploreBlogData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
//                empty.visibility = View.VISIBLE
                if (currentPage == 1) {
                    emptyImage.visibility = View.VISIBLE
                    savedBlogsRecyclerView.visibility = View.GONE
                }
            }
        })
    }
}