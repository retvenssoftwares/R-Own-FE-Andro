package app.retvens.rown.NavigationFragments.eventForUsers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesData
import app.retvens.rown.R

class AllEventCategoryActivity : AppCompatActivity() {

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_event_category)

        categoryRecyclerView = findViewById(R.id.eventCategoryRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.setHasFixedSize(true)
        getCategories()

     }
    private fun getCategories() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.setHasFixedSize(true)

        val blogs = listOf<EventCategoriesData>(
            EventCategoriesData("Title 1"),
            EventCategoriesData("Title 2"),
            EventCategoriesData("Title 3"),
            EventCategoriesData("Title 23"),
        )

        eventCategoriesAdapter = EventCategoriesAdapter(blogs, this)
        categoryRecyclerView.adapter = eventCategoriesAdapter
        eventCategoriesAdapter.notifyDataSetChanged()
    }
}