package app.retvens.rown.NavigationFragments.profile.viewConnections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
    lateinit var searchBar:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewConnectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        binding.connectionsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.connectionsRecycler.setHasFixedSize(true)

        searchBar = findViewById(R.id.searchBar)



        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        getAllConnections(user_id)

        binding.refreshLayout.setOnRefreshListener {
            getAllConnections(user_id)
            binding.refreshLayout.isRefreshing = false
        }

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
                            var original:List<Connections> = emptyList()
                        response.forEach {
                            original = it.conns
                            connectionsAdapter = ConnectionsAdapter(it.conns as ArrayList<Connections>, this@ViewConnectionsActivity)
                            binding.connectionsRecycler.adapter = connectionsAdapter
                            connectionsAdapter.notifyDataSetChanged()

                        }

                        searchBar.addTextChangedListener(object :TextWatcher{
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                val filterData = original.filter { item ->
                                    item.Full_name.contains(p0.toString(),ignoreCase = true)
                                }

                                connectionsAdapter.updateData(filterData as ArrayList<Connections>)
                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }
                        })

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