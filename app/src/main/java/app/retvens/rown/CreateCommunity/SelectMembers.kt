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
import app.retvens.rown.DataCollections.UserProfileRequestItem
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
    private  var selectedMembe: ArrayList<UserProfileRequestItem> = ArrayList()
    private  var number:ArrayList<String> = ArrayList()
    private  var names:ArrayList<String> = ArrayList()
    private  var profile:ArrayList<String> = ArrayList()
    private  var userList: List<UserProfileRequestItem> = emptyList()
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
        val type = intent.getStringExtra("type")


        next.setOnClickListener {
                val intent = Intent(this, UploadIcon::class.java)
                intent.putStringArrayListExtra("number", number)
                intent.putStringArrayListExtra("names", names)
                intent.putStringArrayListExtra("pic", profile)
                intent.putExtra("name",name)
                intent.putExtra("desc",description)
                intent.putExtra("type",type)
                startActivity(intent)
        }

        receiverProfileAdapter.setOnItemClickListener(object :
            SelectMembersAdapter.OnItemClickListener {
            override fun onItemClick(member: UserProfileRequestItem) {
                next.visibility = View.VISIBLE

                val index = selectedMembe.indexOf(member)
                if (index == -1) {
                    number.add(member.Phone)
                    names.add(member.Full_name!!)
                    profile.add(member.Profile_pic!!)
                } else {
                    selectedMembe.removeAt(index)
                }

                selectedMembersAdapter.addSelectedMember(member)
                selectedMembersAdapter.notifyDataSetChanged()
            }
        })

        getUsers()

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn_members)

        backbtn.setOnClickListener {
            startActivity(Intent(this, CreateCommunity::class.java))
        }

    }

    private fun getUsers() {
        val send = RetrofitBuilder.retrofitBuilder.getProfile()

        send.enqueue(object : Callback<List<UserProfileRequestItem>?> {
            override fun onResponse(
                call: Call<List<UserProfileRequestItem>?>,
                response: Response<List<UserProfileRequestItem>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    userList = response
                    // Update the adapter with the new data
                    receiverProfileAdapter.userList = userList ?: emptyList()
                    receiverProfileAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<UserProfileRequestItem>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })



    }

}