package app.retvens.rown.NavigationFragments.profile.viewConnections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
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

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.connectionsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.connectionsRecycler.setHasFixedSize(true)




        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        getAllConnections(user_id)

    }

    private fun getAllConnections(userId: String) {

        val getConnections = RetrofitBuilder.connectionApi.getConnectionList(userId)

        getConnections.enqueue(object : Callback<List<ConnectionListDataClass>?> {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                try {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        response.forEach {
                            connectionsAdapter = ConnectionsAdapter(it.conns as ArrayList<Connections>, this@ViewConnectionsActivity)
                            binding.connectionsRecycler.adapter = connectionsAdapter
                            connectionsAdapter.notifyDataSetChanged()
                        }

                    }
                }catch (e:NullPointerException){
                    Toast.makeText(applicationContext,"No Connections Yet",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {
               Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }


}