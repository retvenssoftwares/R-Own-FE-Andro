package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MesiboUsers : AppCompatActivity() {

    private lateinit var receiverProfileAdapter: ReceiverProfileAdapter
    private lateinit var recycler:RecyclerView
    private  var userList: List<MesiboUsersData> = emptyList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesibo_users)


        recycler = findViewById<RecyclerView>(R.id.chatRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        receiverProfileAdapter = ReceiverProfileAdapter(baseContext, emptyList())
        recycler.adapter = receiverProfileAdapter
        receiverProfileAdapter.notifyDataSetChanged()

        getMesiboUsers()

    }

    private fun getMesiboUsers() {

        val send = RetrofitBuilder.retrofitBuilder.getMesiboUsers()

       send.enqueue(object : Callback<UsersList?> {
           override fun onResponse(call: Call<UsersList?>, response: Response<UsersList?>) {
               if (response.isSuccessful) {
                   val response = response.body()!!
                   if (response.result) {
                       userList = response.users
                       // Update the adapter with the new data
                       receiverProfileAdapter.userList = userList ?: emptyList()
                       receiverProfileAdapter.notifyDataSetChanged()

                       Toast.makeText(applicationContext,userList.size.toString(),Toast.LENGTH_SHORT).show()

                   }
               }else{
                   Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                   Log.e("",response.message().toString())
               }
           }

           override fun onFailure(call: Call<UsersList?>, t: Throwable) {
               Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
               Log.e("",t.message.toString())
           }
       })

    }
}