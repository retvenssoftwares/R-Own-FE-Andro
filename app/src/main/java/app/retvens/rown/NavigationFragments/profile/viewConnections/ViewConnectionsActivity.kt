package app.retvens.rown.NavigationFragments.profile.viewConnections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.NavigationFragments.profile.viewRequests.RequestsAdapter
import app.retvens.rown.NavigationFragments.profile.viewRequests.RequestsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllCommmunitiesBinding
import app.retvens.rown.databinding.ActivityViewConnectionsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewConnectionsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewConnectionsBinding

    lateinit var connectionsAdapter: ConnectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewConnectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.connectionsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.connectionsRecycler.setHasFixedSize(true)




        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        getAllConnections(user_id)

    }

    private fun getAllConnections(userId: String) {

        val getConnections = RetrofitBuilder.connectionApi.getConnectionList(userId)

        getConnections.enqueue(object : Callback<GetAllRequestDataClass?> {
            override fun onResponse(
                call: Call<GetAllRequestDataClass?>,
                response: Response<GetAllRequestDataClass?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    connectionsAdapter = ConnectionsAdapter(response, applicationContext)
                    binding.connectionsRecycler.adapter = connectionsAdapter
                    connectionsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<GetAllRequestDataClass?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }


}