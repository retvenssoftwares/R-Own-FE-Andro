package app.retvens.rown.viewAll.viewAllBlogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllCategoriesBinding
import app.retvens.rown.databinding.ActivityViewAllCommmunitiesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllCategoriesActivity : AppCompatActivity() {
    lateinit var binding:ActivityViewAllCategoriesBinding
    private var blogList:ArrayList<ViewAllCategoriesData> = ArrayList()
    lateinit var viewAllCategoriesAdapter: ViewAllCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewAllCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoriesRecyclerView.setHasFixedSize(true)

        getAllCategory()
    }
    private fun getAllCategory() {

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
                        val response = response.body()!!
                        response.forEach {
                            if (it.blog_count != 0){
                                blogList.add(it)
                            }
                        }
                    viewAllCategoriesAdapter = ViewAllCategoriesAdapter(blogList as ArrayList<ViewAllCategoriesData>, this@ViewAllCategoriesActivity)
                        binding.categoriesRecyclerView.adapter = viewAllCategoriesAdapter
                        viewAllCategoriesAdapter.removeEmptyCategoryFromList(blogList)
                    viewAllCategoriesAdapter.notifyDataSetChanged()

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
                                val original = blogList.toList()
                                val filter = original.filter { searchUser ->
                                    searchUser.category_name.contains(s.toString(), ignoreCase = true)
//                                searchUser.blog_content.contains(s.toString(),ignoreCase = true)
//                                searchUser.User_name.contains(s.toString(),ignoreCase = true)
                                }
                                viewAllCategoriesAdapter.searchView(filter as ArrayList<ViewAllCategoriesData>)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })
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