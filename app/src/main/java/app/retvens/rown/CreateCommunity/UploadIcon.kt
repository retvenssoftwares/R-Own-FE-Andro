package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.ChatSection.GroupChat
import app.retvens.rown.DataCollections.AddMemberData
import app.retvens.rown.DataCollections.GroupCreate
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadIcon : AppCompatActivity() {

    private var groupId:String = ""
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var adapter:UploadIconAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_icon)

        val members = intent.getStringArrayListExtra("selectedMembers")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")

        val setName = findViewById<TextView>(R.id.Community_Name)

        setName.text = name

        myRecyclerView = findViewById(R.id.recyclerUpload)
        myRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        adapter = UploadIconAdapter(this,members)
        myRecyclerView.adapter = adapter

        val next = findViewById<ImageView>(R.id.nextUpload)

        next.setOnClickListener {
            val name = intent.getStringExtra("name")
            CreateGroup(name.toString())

        }



    }

    private fun CreateGroup(name:String) {

        val GroupName = GroupCreate(name)

        val data = RetrofitBuilder.retrofitBuilder.createGroup(GroupName)

        data.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    groupId = response.group.gid.toString()
                    addMembers()
                    val intent = Intent(applicationContext,GroupChat::class.java)
                    intent.putExtra("groupId",groupId)
                    startActivity(intent)

                }else{
                    Toast.makeText(applicationContext,response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun addMembers() {
        val members = intent.getStringArrayListExtra("selectedMembers")

// Remove any non-digit characters from each phone number
        val cleanedNumbers = members!!.map { it.replace(Regex("[^\\d]"), "") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")


        val data = AddMemberData(groupId, formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    Toast.makeText(applicationContext,response.result.toString(),Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}