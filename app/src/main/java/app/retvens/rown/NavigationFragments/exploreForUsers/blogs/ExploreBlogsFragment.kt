package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.profile.events.EventsProfileAdapter
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsAdapter
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreBlogsFragment : Fragment() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var allBlogsAdapter: AllBlogsAdapter
    private var currentPage = 1
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var isLoading:Boolean = false
    lateinit var empty : TextView
    private lateinit var progress: ProgressBar
    lateinit var searchBar : EditText
    private var blogList:ArrayList<AllBlogsData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_blogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_blogs_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.emptyBlog)
        searchBar = view.findViewById(R.id.search_explore_blogs)

        progress = view.findViewById(R.id.progress)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        allBlogsAdapter = AllBlogsAdapter(blogList, requireContext())
        exploreBlogsRecyclerView.adapter = allBlogsAdapter


        exploreBlogsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){

                        isLoading = false
                        currentPage++
                        getData()


                    }
                }


            }
        })

        getAllBlogs()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getAllBlogs()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getAllBlogs() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val allBlogs = RetrofitBuilder.exploreApis.getExploreBlog(user_id,currentPage.toString())
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

                                try {

                                    blogList.addAll(it.blogs)
                                    allBlogsAdapter.removeBlogFromList(it.blogs)
                                    allBlogsAdapter.notifyDataSetChanged()
                                    Log.d("on", it.toString())
                                    /* searchBar.addTextChangedListener(object : TextWatcher {
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

                                         }

                                         override fun afterTextChanged(p0: Editable?) {

                                         }
                                     })*/
                                }catch (e:NullPointerException){
//                                    Toast.makeText(requireContext(),"No More Blogs",Toast.LENGTH_SHORT).show()
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
            override fun onFailure(call: Call<List<ExploreBlogData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })
    }
}