package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
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
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import okhttp3.internal.notify
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
    private  var userId:ArrayList<String> = ArrayList()
    private  var userList: List<Connections> = emptyList()
    private lateinit var searchBar:EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_members)


        searchBar = findViewById(R.id.searchBar)
        findViewById<ImageButton>(R.id.createCommunity_backBtn_members).setOnClickListener { onBackPressed() }

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
        val location = intent.getStringExtra("location")
        val type = intent.getStringExtra("type")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")

        next.setOnClickListener {
                val intent = Intent(this, UploadIcon::class.java)
                intent.putStringArrayListExtra("number", number)
                intent.putStringArrayListExtra("names", names)
                intent.putStringArrayListExtra("pic", profile)
                intent.putStringArrayListExtra("userId",userId)
                intent.putExtra("name",name)
                intent.putExtra("desc",description)
                intent.putExtra("location",location)
                intent.putExtra("type",type)
                intent.putExtra("latitude",latitude)
                intent.putExtra("longitude",longitude)
                startActivity(intent)
            Log.e("location",location.toString())
        }

        receiverProfileAdapter.setOnItemClickListener(object :
            SelectMembersAdapter.OnItemClickListener {
            override fun onItemClick(member: Connections) {

                val index = selectedMembe.indexOfFirst { it.Full_name == member.Full_name }

                if (index == -1) {
                    if (number.size != 0 && number.size!=1){
                        next.visibility = View.VISIBLE
                    }


                    member.isSelected = !member.isSelected

                    if (member.isSelected){
                        number.add(member.Phone)
                        names.add(member.Full_name)
                        profile.add(member.Profile_pic)
                        userId.add(member.User_id)
                    }else{
                        number.remove(member.Phone)
                        names.remove(member.Full_name)
                        profile.remove(member.Profile_pic)
                        userId.remove(member.User_id)
                        Log.e("number",number.toString())
                        if (number.size == 1){
                            next.visibility = View.GONE
                        }
                    }

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

        val sharedPreferences1 = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences1?.getString("user_id", "").toString()

        val send = RetrofitBuilder.connectionApi.getConnectionList(user_id)

        send.enqueue(object : Callback<List<ConnectionListDataClass>?> {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach {
                        userList = it.conns
                    }
                    // Update the adapter with the new data
                    receiverProfileAdapter.userList = userList ?: emptyList()
                    receiverProfileAdapter.notifyDataSetChanged()
                    val original = userList.toList()

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

                }
            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {

            }
        })



    }

}