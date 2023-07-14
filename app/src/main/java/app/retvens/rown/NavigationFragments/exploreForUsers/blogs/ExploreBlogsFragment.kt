package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreBlogsFragment : Fragment() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var allBlogsAdapter: AllBlogsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var isLoading:Boolean = false
    lateinit var empty : TextView
    lateinit var errorImage : ImageView
    private lateinit var progress: ProgressBar
    lateinit var searchBar : EditText
    private var blogList:ArrayList<AllBlogsData> = ArrayList()

    private var currentPage = 1
    private var lastPage = 1

    private lateinit var progressDialog:Dialog

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
        errorImage = view.findViewById(R.id.errorImage)
        searchBar = view.findViewById(R.id.search_explore_blogs)

        progress = view.findViewById(R.id.progress)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        allBlogsAdapter = AllBlogsAdapter(blogList, requireContext(), true)
        exploreBlogsRecyclerView.adapter = allBlogsAdapter

        progressDialog = Dialog(requireContext())
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()


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
                    if (isAdded && isLoading && (lastVisibleItemPosition == totalItem-1)){
//                        if (currentPage > lastPage) {
                            isLoading = false
                            lastPage++
                            getData()
//                        }
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
            if (isAdded) {
                getAllBlogs()
                progress.setVisibility(View.GONE);
            }
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
                progressDialog.dismiss()
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
//                                    if (it.blogs.size >= 10){
                                        currentPage++
//                                    }
                                    Log.d("on", it.toString())
                                     searchBar.addTextChangedListener(object : TextWatcher {
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
                                                val text = p0.toString()
                                                searchBlog(text)
                                         }

                                         override fun afterTextChanged(p0: Editable?) {

                                         }
                                     })
                                }catch (e:NullPointerException){
//                                    errorImage.visibility = View.VISIBLE
//                                    Toast.makeText(requireContext(),"No More Blogs",Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
//                            errorImage.visibility = View.VISIBLE
                        }
                    } else {
//                        errorImage.visibility = View.VISIBLE
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
                progressDialog.dismiss()
//                errorImage.visibility = View.VISIBLE
            }
        })
    }

    private fun searchBlog(text: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        Log.e("userid",user_id)
        Log.e("word",text)

        val searchBlog = RetrofitBuilder.exploreApis.searchBlog(user_id,text,"1")

        searchBlog.enqueue(object : Callback<List<BlogData>?> {
            override fun onResponse(
                call: Call<List<BlogData>?>,
                response: Response<List<BlogData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("res",response.toString())
                    val searchList:ArrayList<Blog> = ArrayList()
//                    try {
//                        response.forEach {
//                            searchList.addAll(it.blogs)
//                            allBlogsAdapter = AllBlogsAdapter(searchList, requireContext())
//                            exploreBlogsRecyclerView.adapter = allBlogsAdapter
//                            allBlogsAdapter.notifyDataSetChanged()
//                        }
//                    }catch (e:NullPointerException){
//                        allBlogsAdapter = AllBlogsAdapter(searchList, requireContext())
//                        exploreBlogsRecyclerView.adapter = allBlogsAdapter
//                        allBlogsAdapter.notifyDataSetChanged()
//                    }

                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<BlogData>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }
}