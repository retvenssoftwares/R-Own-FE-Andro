package app.retvens.rown.NavigationFragments.profile.viewRequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.Connection
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewRequestsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewRequestsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewRequestsBinding

    lateinit var requestsAdapter: RequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.requestsRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.requestsRecycler.setHasFixedSize(true)


        binding.reBackBtn.setOnClickListener { onBackPressed() }



        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        getAppRequestConnection(user_id)
    }

    private fun getAppRequestConnection(userId: String) {

        val getRequest = RetrofitBuilder.connectionApi.getRequestList(userId)

       getRequest.enqueue(object : Callback<GetAllRequestDataClass?>,
           RequestsAdapter.ConnectClickListener {
           override fun onResponse(
               call: Call<GetAllRequestDataClass?>,
               response: Response<GetAllRequestDataClass?>
           ) {
               if (response.isSuccessful){
                   val response = response.body()!!
                   requestsAdapter = RequestsAdapter(response, applicationContext)
                   binding.requestsRecycler.adapter = requestsAdapter
                   requestsAdapter.notifyDataSetChanged()

                   requestsAdapter.setJobSavedClickListener(this)
               }else{
                   Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
               }
           }

           override fun onFailure(call: Call<GetAllRequestDataClass?>, t: Throwable) {
               Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
           }

           override fun onJobSavedClick(connect: Connection) {
               accceptRequest(connect.User_id)
           }
       })

    }

    private fun accceptRequest(userId: String) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val accept = RetrofitBuilder.connectionApi.sendRequest(user_id, ConnectionDataClass(userId))

        accept.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,"Request Accepted",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,"Request Accepted",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}