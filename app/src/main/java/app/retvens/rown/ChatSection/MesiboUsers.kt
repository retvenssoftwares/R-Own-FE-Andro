package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.Connection
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MesiboUsers : AppCompatActivity() {

    private lateinit var receiverProfileAdapter: ReceiverProfileAdapter
    private lateinit var recycler:RecyclerView
    private  var userList: List<MesiboUsersData> = emptyList()
    private lateinit var searchBar:EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesibo_users)

        val back = findViewById<ImageView>(R.id.notifications_backBtn)

        back.setOnClickListener {
            onBackPressed()
        }

        recycler = findViewById<RecyclerView>(R.id.chatRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        searchBar = findViewById(R.id.searchBar)


        getMesiboUsers()

    }

    private fun getMesiboUsers() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val send = RetrofitBuilder.connectionApi.getConnectionList(user_id)

        send.enqueue(object : Callback<List<ConnectionListDataClass>?> {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    var original = emptyList<Connections>()
                    response.forEach {
                        receiverProfileAdapter = ReceiverProfileAdapter(baseContext,it.conns)
                        recycler.adapter = receiverProfileAdapter
                        receiverProfileAdapter.notifyDataSetChanged()

                        original = it.conns.toList()
                    }

                    searchBar.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            val filterData = original.filter { item ->
                                item.Full_name.contains(p0.toString(),ignoreCase = true)
                            }

                            receiverProfileAdapter.updateData(filterData)
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })

                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }
}