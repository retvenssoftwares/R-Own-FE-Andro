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
import app.retvens.rown.DataCollections.UserProfileRequestItem
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



        getMesiboUsers()

    }

    private fun getMesiboUsers() {

        val send = RetrofitBuilder.retrofitBuilder.getProfile()

        send.enqueue(object : Callback<List<UserProfileRequestItem>?> {
            override fun onResponse(
                call: Call<List<UserProfileRequestItem>?>,
                response: Response<List<UserProfileRequestItem>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    receiverProfileAdapter = ReceiverProfileAdapter(baseContext,response)
                    recycler.adapter = receiverProfileAdapter
                    receiverProfileAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserProfileRequestItem>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }
}