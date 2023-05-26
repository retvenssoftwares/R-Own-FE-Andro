package app.retvens.rown.viewAll.viewAllBlogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var viewAllCategoriesAdapter: ViewAllCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewAllCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        categoriesRecyclerView.setHasFixedSize(true)

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
                    viewAllCategoriesAdapter =
                        ViewAllCategoriesAdapter(response.body()!!, this@ViewAllCategoriesActivity)
                    categoriesRecyclerView.adapter = viewAllCategoriesAdapter
                    viewAllCategoriesAdapter.notifyDataSetChanged()
                } else{
                    Toast.makeText(applicationContext, "isNotSuccessful Blogs category: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ViewAllCategoriesData>?>, t: Throwable) {
                Toast.makeText(applicationContext, "onFailure Blogs category: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}