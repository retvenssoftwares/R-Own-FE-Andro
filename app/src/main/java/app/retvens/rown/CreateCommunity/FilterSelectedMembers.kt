package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.*
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.FeedCollection.AddUserDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import com.bumptech.glide.Glide
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
    private lateinit var searchBar:EditText
    private lateinit var progressDialog:Dialog
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

        searchBar = findViewById(R.id.searchBar)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")
        val location = intent.getStringExtra("location")
        val type = intent.getStringExtra("type")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")

        next.setOnClickListener {
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()

            addMembers()
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
                val original = userList.toList()


            }
        })

        getUsers()

        val backbtn = findViewById<ImageView>(R.id.createCommunity_backBtn_members)

        backbtn.setOnClickListener {
            onBackPressed()
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

                    val original = userList.toList()

                    searchBar.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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
    private fun addMembers() {


// Remove any non-digit characters from each phone number
        val cleanedNumbers = number!!.map { it.replace(Regex("[^\\d]"), "+") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")

        val groupId = intent.getStringExtra("groupId")


        val data = AddMemberData(groupId!!, formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.result.toString(),Toast.LENGTH_SHORT).show()
                    addCommunityMember()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addCommunityMember() {
        val members = userId
        val groupId = intent.getStringExtra("groupId")
        for (x in members!!) {

            val data = AddUserDataClass(x)

            val send = RetrofitBuilder.feedsApi.addUser(groupId!!, data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
//                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT)
//                            .show()
                        val intent = Intent(applicationContext, MesiboMessagingActivity::class.java)
                        intent.putExtra(MesiboUI.GROUP_ID, groupId.toLong())
                        startActivity(intent)
                        progressDialog.dismiss()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
    }
}