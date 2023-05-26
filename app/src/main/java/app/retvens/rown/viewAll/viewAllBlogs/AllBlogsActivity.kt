package app.retvens.rown.viewAll.viewAllBlogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAllBlogsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllBlogsActivity : AppCompatActivity() {
    lateinit var binding:ActivityAllBlogsBinding

    lateinit var blogsRecyclerView: RecyclerView
    lateinit var allBlogsAdapter: AllBlogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAllBlogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        blogsRecyclerView = findViewById(R.id.all_blogs_recycler)
        blogsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        blogsRecyclerView.setHasFixedSize(true)

        val idCategory = intent.getStringExtra("id")
        if (idCategory == null){
            getAllBlogs()
        } else {
            val categoryName = intent.getStringExtra("name")
            binding.topTitle.text = categoryName
            getBlogsByCategory(idCategory)
        }
    }

    private fun getBlogsByCategory(idCategory: String) {
        val blogsBy = RetrofitBuilder.viewAllApi.getBlogsByCategory(idCategory)
        blogsBy.enqueue(object : Callback<List<AllBlogsData>?> {
            override fun onResponse(
                call: Call<List<AllBlogsData>?>,
                response: Response<List<AllBlogsData>?>
            ) {
                if (response.isSuccessful){

                    allBlogsAdapter = AllBlogsAdapter(response.body()!!, this@AllBlogsActivity)
                    blogsRecyclerView.adapter = allBlogsAdapter
                    allBlogsAdapter.notifyDataSetChanged()

                    binding.searchBlogs.addTextChangedListener(object : TextWatcher {
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
                                searchUser.blog_content.contains(s.toString(),ignoreCase = true)
                                searchUser.User_name.contains(s.toString(),ignoreCase = true)
                            }
                            allBlogsAdapter.searchView(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<AllBlogsData>?>, t: Throwable) {
                Toast.makeText(applicationContext, "Categorized Blogs ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAllBlogs() {

        val allBlogs = RetrofitBuilder.viewAllApi.getAllBlogs()
        allBlogs.enqueue(object : Callback<List<AllBlogsData>?> {
            override fun onResponse(
                call: Call<List<AllBlogsData>?>,
                response: Response<List<AllBlogsData>?>
            ) {
                if (response.isSuccessful) {
                    allBlogsAdapter = AllBlogsAdapter(response.body()!!, this@AllBlogsActivity)
                    blogsRecyclerView.adapter = allBlogsAdapter
                    allBlogsAdapter.notifyDataSetChanged()

                    binding.searchBlogs.addTextChangedListener(object : TextWatcher {
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
                                searchUser.blog_content.contains(s.toString(), ignoreCase = true)
                                searchUser.User_name.contains(s.toString(),ignoreCase = true)
                            }
                            allBlogsAdapter.searchView(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
            }
            override fun onFailure(call: Call<List<AllBlogsData>?>, t: Throwable) {
                Toast.makeText(applicationContext, "All Blogs ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}