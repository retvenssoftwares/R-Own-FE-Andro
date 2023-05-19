package app.retvens.rown.NavigationFragments.profile.viewRequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewRequestsBinding

class ViewRequestsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewRequestsBinding

    lateinit var requestsAdapter: RequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.requestsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.requestsRecycler.setHasFixedSize(true)

        val blogs = listOf<RequestsData>(
            RequestsData("title 1"),
            RequestsData("title 14"),
            RequestsData("title 1"),
            RequestsData("title 14"),
            RequestsData("title 1"),
            RequestsData("title 14"),
            RequestsData("title 1"),
            RequestsData("title 14"),
            RequestsData("title 1"),
            RequestsData("title 14"),
            RequestsData("title 1"),
            RequestsData("title 14"),
        )

        requestsAdapter = RequestsAdapter(blogs, applicationContext)
        binding.requestsRecycler.adapter = requestsAdapter
        requestsAdapter.notifyDataSetChanged()
    }
}