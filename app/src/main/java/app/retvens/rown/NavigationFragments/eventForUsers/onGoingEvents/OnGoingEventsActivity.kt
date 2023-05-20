package app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R

class OnGoingEventsActivity : AppCompatActivity() {

    private lateinit var onGoingRecyclerView: RecyclerView
    lateinit var onGoingEventsAdapter: OnGoingEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_going_events)

        onGoingRecyclerView = findViewById(R.id.onGoingRecyclerView)
        onGoingRecyclerView.layoutManager = LinearLayoutManager(this)
        onGoingRecyclerView.setHasFixedSize(true)
        onGoingEvents()

    }
    private fun onGoingEvents() {
        val blogs = listOf<OnGoingEventsData>(
            OnGoingEventsData("Title 1"),
            OnGoingEventsData("Title 2"),
            OnGoingEventsData("Title 3"),
            OnGoingEventsData("Title 23"),
        )

        onGoingEventsAdapter = OnGoingEventsAdapter(blogs, this)
        onGoingRecyclerView.adapter = onGoingEventsAdapter
        onGoingEventsAdapter.notifyDataSetChanged()
    }
}