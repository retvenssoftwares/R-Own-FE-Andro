package app.retvens.rown.viewAll.viewAllBlogs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllBlogsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllBlogsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewAllBlogsBinding

    lateinit var blogsRecyclerView: RecyclerView
    lateinit var allBlogsAdapter: AllBlogsAdapter

    lateinit var viewAllCategoriesAdapter : ViewAllCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBlogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        binding.viewAllItem.setOnClickListener {
            startActivity(Intent(this, AllBlogsActivity::class.java))
        }

        binding.viewAllCategory.setOnClickListener {
            startActivity(Intent(this, ViewAllCategoriesActivity::class.java))
        }

        binding.blogsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.blogsRecyclerView.setHasFixedSize(true)

        binding.refreshLayout.setOnRefreshListener {

            getAllBlogs()

            getCategories()

            binding.refreshLayout.isRefreshing = false
        }

        getAllBlogs()

        getCategories()

    }
    private fun getAllBlogs() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val allBlogs = RetrofitBuilder.viewAllApi.getAllBlogs(user_id = user_id)
        allBlogs.enqueue(object : Callback<List<AllBlogsData>?> {
            override fun onResponse(
                call: Call<List<AllBlogsData>?>,
                response: Response<List<AllBlogsData>?>
            ) {
                if(response.isSuccessful) {
                    binding.shimmerFrameLayoutBlog.stopShimmer()
                    binding.shimmerFrameLayoutBlog.visibility = View.GONE

                    if (response.body()!!.isNotEmpty()) {
                    allBlogsAdapter = AllBlogsAdapter(response.body()!! as ArrayList<AllBlogsData>, this@ViewAllBlogsActivity, false)
                    binding.blogsRecyclerView.adapter = allBlogsAdapter
                    allBlogsAdapter.removeBlogFromList(response.body()!!)
                    allBlogsAdapter.notifyDataSetChanged()

                    binding.searchCommunity.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val original = response.body()!!.toList()
                            val filter = original.filter { searchUser ->
                                searchUser.blog_title.contains(s.toString(), ignoreCase = true)
//                                searchUser.blog_content.contains(s.toString(),ignoreCase = true)
//                                searchUser.User_name.contains(s.toString(),ignoreCase = true)
                            }
                            allBlogsAdapter.searchView(filter as ArrayList<AllBlogsData>)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                    } else {
                        binding.shimmerFrameLayoutBlog.stopShimmer()
                        binding.shimmerFrameLayoutBlog.visibility = View.GONE
                        binding.emptyBlog.text = "No event Posted"
                        binding.emptyBlog.visibility = View.VISIBLE
                    }
                } else {
                    binding.shimmerFrameLayoutBlog.stopShimmer()
                    binding.shimmerFrameLayoutBlog.visibility = View.GONE
                    binding.emptyBlog.text = "${response.code()}  ${response.message()}"
                    binding.emptyBlog.visibility = View.VISIBLE
//                    Toast.makeText(applicationContext,"${response.code()}  ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<AllBlogsData>?>, t: Throwable) {
                binding.shimmerFrameLayoutBlog.stopShimmer()
                binding.shimmerFrameLayoutBlog.visibility = View.GONE

                binding.emptyBlog.text = "Try Again - Check your Internet"
                binding.emptyBlog.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "All Blogs ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getCategories() {
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerView.setHasFixedSize(true)

        val categories = RetrofitBuilder.viewAllApi.getBlogsCategory()
        categories.enqueue(object : Callback<List<ViewAllCategoriesData>?> {
            override fun onResponse(
                call: Call<List<ViewAllCategoriesData>?>,
                response: Response<List<ViewAllCategoriesData>?>
            ) {

                if (response.isSuccessful) {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE

                    if (response.body()!!.isNotEmpty()) {
                    viewAllCategoriesAdapter = ViewAllCategoriesAdapter(response.body()!! as ArrayList<ViewAllCategoriesData>, this@ViewAllBlogsActivity)
                    binding.categoryRecyclerView.adapter = viewAllCategoriesAdapter
                        viewAllCategoriesAdapter.removeEmptyCategoryFromList(response.body()!!)
                        viewAllCategoriesAdapter.notifyDataSetChanged()
                    } else {
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.empty.text = "No event Posted"
                        binding.empty.visibility = View.VISIBLE
                    }
                } else {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.empty.text = "${response.code()}  ${response.message()}"
                    binding.empty.visibility = View.VISIBLE
//                    Toast.makeText(applicationContext,"${response.code()}  ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ViewAllCategoriesData>?>, t: Throwable) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE

                binding.empty.text = "Try Again - Check your Internet"
                binding.empty.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "onFailure Blogs category: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}