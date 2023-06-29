package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.ChatSection.ReceiverProfileAdapter
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.FeedCollection.Member
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterSelectedMembers : AppCompatActivity() {

    private lateinit var receiverProfileAdapter: SelectMembersAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var selectedMembersAdapter: SelectedMembersAdapter
    private lateinit var selectedMembersRecyclerView: RecyclerView
    private  var selectedMembe: ArrayList<Connections> = ArrayList()
    private  var number:ArrayList<String> = ArrayList()
    private  var names:ArrayList<String> = ArrayList()
    private var existingMembers:ArrayList<String> = ArrayList()
    private var newUserList:ArrayList<String> = ArrayList()
    private  var profile:ArrayList<String> = ArrayList()
    private  var userId:ArrayList<String> = ArrayList()
    private  var userList: List<Connections> = emptyList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_members)

        findViewById<ImageButton>(R.id.createCommunity_backBtn_members).setOnClickListener { onBackPressed() }

        recycler = findViewById<RecyclerView>(R.id.listOfaddmembers)
        recycler.layoutManager = LinearLayoutManager(this)

        selectedMembersRecyclerView = findViewById(R.id.selectedItemrecycler)
        selectedMembersRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        selectedMembersAdapter = SelectedMembersAdapter(this)

        selectedMembersRecyclerView.adapter = selectedMembersAdapter

        existingMembers = intent.getStringArrayListExtra("members")!!

        receiverProfileAdapter = SelectMembersAdapter(baseContext, emptyList())
        recycler.adapter = receiverProfileAdapter
        receiverProfileAdapter.notifyDataSetChanged()

        val next = findViewById<ImageView>(R.id.select_next)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")
        val location = intent.getStringExtra("location")
        val type = intent.getStringExtra("type")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")

        next.setOnClickListener {



        }

        receiverProfileAdapter.setOnItemClickListener(object :
            SelectMembersAdapter.OnItemClickListener {
            override fun onItemClick(member: Connections) {
                next.visibility = View.VISIBLE

                val index = selectedMembe.indexOfFirst { it.Full_name == member.Full_name }
                if (index == -1) {


                    member.isSelected = !member.isSelected

                    if (member.isSelected){
                        number.add(member.Phone)
                        names.add(member.Full_name!!)
                        profile.add(member.Profile_pic!!)
                        userId.add(member.User_id)
                    }else{
                        number.remove(member.Phone)
                        names.remove(member.Full_name)
                        profile.remove(member.Profile_pic)
                        userId.remove(member.User_id)
                    }

                } else {
                    selectedMembe.removeAt(index)

                }



                selectedMembersAdapter.addSelectedMember(member)
                selectedMembersAdapter.notifyDataSetChanged()

                Toast.makeText(applicationContext,number.toString(),Toast.LENGTH_SHORT).show()
            }
        })

        getUsers()

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn_members)

        backbtn.setOnClickListener {
            startActivity(Intent(this, CreateCommunity::class.java))
        }

    }

    private fun getUsers() {
        val sharedPreferences1 = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        val send = RetrofitBuilder.connectionApi.getConnectionList(user_id)

        send.enqueue(object : Callback<List<ConnectionListDataClass>?> {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    response.forEach {
                        userList = it.conns
                    }

                    val uniqueItems: ArrayList<Connections> = ArrayList()

                    for (item in userList) {
                        val userId = item.User_id
                        if (!existingMembers.contains(userId)) {
                            uniqueItems.add(item)
                        }
                    }

                    println("List 1: $userList")
                    println("List 2: $existingMembers")
                    println("Unique Items: $uniqueItems")

                    // Update the adapter with the new data
                    receiverProfileAdapter.userList = uniqueItems ?: emptyList()
                    receiverProfileAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {

            }
        })



    }

}