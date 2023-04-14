package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
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
    private  var selectedMember:ArrayList<String> = ArrayList()
    private  var userList: List<MesiboUsersData> = emptyList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_members)

        recycler = findViewById<RecyclerView>(R.id.listOfaddmembers)
        recycler.layoutManager = LinearLayoutManager(this)

        selectedMembersRecyclerView = findViewById(R.id.selectedItemrecycler)
        selectedMembersRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        selectedMembersAdapter = SelectedMembersAdapter(this)

        selectedMembersRecyclerView.adapter = selectedMembersAdapter

        receiverProfileAdapter = SelectMembersAdapter(baseContext, emptyList())
        recycler.adapter = receiverProfileAdapter
        receiverProfileAdapter.notifyDataSetChanged()

        val next = findViewById<ImageView>(R.id.select_next)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")



        next.setOnClickListener {
                val intent = Intent(this,UploadIcon::class.java)
            intent.putStringArrayListExtra("selectedMembers", selectedMember)
            intent.putExtra("name",name)
            intent.putExtra("desc",description)
            startActivity(intent)
        }

        receiverProfileAdapter.setOnItemClickListener(object : SelectMembersAdapter.OnItemClickListener {
            override fun onItemClick(member: MesiboUsersData) {

                next.visibility = View.VISIBLE

                val index = selectedMember.indexOf(member.address)
                if (index == -1) {
                    selectedMember.add(member.address)
                } else {
                    selectedMember.removeAt(index)
                }

                selectedMembersAdapter.addSelectedMember(member)
                selectedMembersAdapter.notifyDataSetChanged()


            }
        })

        getMesiboUsers()

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn_members)

        backbtn.setOnClickListener {
            startActivity(Intent(this,CreateCommunity::class.java))
        }

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