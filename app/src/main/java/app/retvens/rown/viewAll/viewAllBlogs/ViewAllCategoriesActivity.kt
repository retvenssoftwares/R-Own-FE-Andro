package app.retvens.rown.viewAll.viewAllBlogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R

class ViewAllCategoriesActivity : AppCompatActivity() {

    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var viewAllCategoriesAdapter: ViewAllCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_categories)

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        categoriesRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ViewAllCategoriesData>(
            ViewAllCategoriesData("Title 1"),
            ViewAllCategoriesData("Title 2"),
            ViewAllCategoriesData("Title 3"),
            ViewAllCategoriesData("Title 23"),
            ViewAllCategoriesData("Title 1"),
            ViewAllCategoriesData("Title 2"),
            ViewAllCategoriesData("Title 3"),
            ViewAllCategoriesData("Title 23"),
            ViewAllCategoriesData("Title 1"),
            ViewAllCategoriesData("Title 2"),
            ViewAllCategoriesData("Title 3"),
            ViewAllCategoriesData("Title 23"),
        )

        viewAllCategoriesAdapter = ViewAllCategoriesAdapter(blogs, this)
        categoriesRecyclerView.adapter = viewAllCategoriesAdapter
        viewAllCategoriesAdapter.notifyDataSetChanged()

    }
}