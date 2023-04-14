package app.retvens.rown.CreateCommunity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.ChatSection.ReceiverProfileAdapter
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectMembers : AppCompatActivity() {

    private lateinit var receiverProfileAdapter: SelectMembersAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var selectedMembersAdapter: SelectedMembersAdapter
    private lateinit var selectedMembersRecyclerView: RecyclerView
    private  var userList: List<MesiboUsersData> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_members)

        recycler = findViewById<RecyclerView>(R.id.listOfaddmembers)
        recycler.layoutManager = LinearLayoutManager(this)

        selectedMembersRecyclerView = findViewById(R.id.selectedItemrecycler)
        selectedMembersRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        selectedMembersAdapter = SelectedMembersAdapter(this)

        receiverProfileAdapter = SelectMembersAdapter(baseContext, emptyList())
        recycler.adapter = receiverProfileAdapter
        receiverProfileAdapter.notifyDataSetChanged()

        receiverProfileAdapter.setOnItemClickListener(object : SelectMembersAdapter.OnItemClickListener {
            override fun onItemClick(member: MesiboUsersData) {
                selectedMembersAdapter.addSelectedMember(member)
                recycler.visibility = View.VISIBLE
            }
        })

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


                    }
                }else{
                    Toast.makeText(applicationContext,response.message().toString(), Toast.LENGTH_SHORT).show()
                    Log.e("",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UsersList?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message, Toast.LENGTH_SHORT).show()
                Log.e("",t.message.toString())
            }
        })

    }

}