package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
lateinit var savedBlogsAdapter: SavedBlogsAdapter

    lateinit var allBlogsAdapter: AllBlogsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

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
        savedBlogsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreBlogsData>(
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
        )

        getAllBlogs()

//        savedBlogsAdapter = SavedBlogsAdapter(blogs, requireContext())
//        savedBlogsRecyclerView.adapter = savedBlogsAdapter
//        savedBlogsAdapter.notifyDataSetChanged()

    }
    private fun getAllBlogs() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val allBlogs = RetrofitBuilder.ProfileApis.getSavedBlog(user_id,"1")
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
                                allBlogsAdapter = AllBlogsAdapter(it.blogs as ArrayList<AllBlogsData>, requireContext())
                                savedBlogsRecyclerView.adapter = allBlogsAdapter
                                allBlogsAdapter.removeBlogFromList(it.blogs)
                                allBlogsAdapter.notifyDataSetChanged()
                                Log.d("on", it.toString())
                            }
                        } else {
//                            empty.visibility = View.VISIBLE
                            emptyImage.visibility = View.VISIBLE
                        }
                    } else {
//                        empty.visibility = View.VISIBLE
                        emptyImage.visibility = View.VISIBLE
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
//                empty.visibility = View.VISIBLE
                emptyImage.visibility = View.VISIBLE
            }
        })
    }
}