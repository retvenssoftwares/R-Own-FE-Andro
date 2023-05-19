package app.retvens.rown.viewAll.viewAllBlogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R

class AllBlogsActivity : AppCompatActivity() {

    lateinit var blogsRecyclerView: RecyclerView
    lateinit var exploreBlogsAdapter : ExploreBlogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_blogs)

        blogsRecyclerView = findViewById(R.id.all_blogs_recycler)
        blogsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        blogsRecyclerView.setHasFixedSize(true)

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

        exploreBlogsAdapter = ExploreBlogsAdapter(blogs, this)
        blogsRecyclerView.adapter = exploreBlogsAdapter
        exploreBlogsAdapter.notifyDataSetChanged()

    }
}