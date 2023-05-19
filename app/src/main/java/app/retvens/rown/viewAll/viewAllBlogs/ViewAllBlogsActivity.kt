package app.retvens.rown.viewAll.viewAllBlogs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllBlogsBinding

class ViewAllBlogsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewAllBlogsBinding

    lateinit var blogsRecyclerView: RecyclerView
    lateinit var exploreBlogsAdapter : ExploreBlogsAdapter

    lateinit var viewAllCategoriesAdapter : ViewAllCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBlogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewAllItem.setOnClickListener {
            startActivity(Intent(this, AllBlogsActivity::class.java))
        }

        binding.viewAllCategory.setOnClickListener {
            startActivity(Intent(this, ViewAllCategoriesActivity::class.java))
        }

        blogsRecyclerView = findViewById(R.id.blogsRecyclerView)
        blogsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        blogsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreBlogsData>(
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
        )

        exploreBlogsAdapter = ExploreBlogsAdapter(blogs, this)
        blogsRecyclerView.adapter = exploreBlogsAdapter
        exploreBlogsAdapter.notifyDataSetChanged()


        getCategories()

    }

    private fun getCategories() {
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerView.setHasFixedSize(true)


        val blogs = listOf<ViewAllCategoriesData>(
            ViewAllCategoriesData("Title 1"),
            ViewAllCategoriesData("Title 2"),
            ViewAllCategoriesData("Title 3"),
            ViewAllCategoriesData("Title 23"),
        )

        viewAllCategoriesAdapter = ViewAllCategoriesAdapter(blogs, this)
        binding.categoryRecyclerView.adapter = viewAllCategoriesAdapter
        viewAllCategoriesAdapter.notifyDataSetChanged()


    }
}