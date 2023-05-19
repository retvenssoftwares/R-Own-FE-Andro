package app.retvens.rown.NavigationFragments.profile.viewConnections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.NavigationFragments.profile.viewRequests.RequestsAdapter
import app.retvens.rown.NavigationFragments.profile.viewRequests.RequestsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllCommmunitiesBinding
import app.retvens.rown.databinding.ActivityViewConnectionsBinding

class ViewConnectionsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewConnectionsBinding

    lateinit var connectionsAdapter: ConnectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewConnectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.connectionsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.connectionsRecycler.setHasFixedSize(true)

        val blogs = listOf<ConnectionsData>(
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
            ConnectionsData("title 1"),
            ConnectionsData("title 81"),
            ConnectionsData("title 12"),
        )

        connectionsAdapter = ConnectionsAdapter(blogs, applicationContext)
        binding.connectionsRecycler.adapter = connectionsAdapter
        connectionsAdapter.notifyDataSetChanged()

    }
}