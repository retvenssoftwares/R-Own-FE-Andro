package app.retvens.rown.viewAll.viewAllCommunities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.AddMemberData
import app.retvens.rown.DataCollections.FeedCollection.AddUserDataClass
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ResponseGroup
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllCommunityAdapter(var listS : ArrayList<GetCommunitiesData>, val context: Context) : RecyclerView.Adapter<ViewAllCommunityAdapter.ViewAllCommunityViewHolder>() {

    private var groupId = ""
    private var number: ArrayList<String> = ArrayList()
    class ViewAllCommunityViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val joinGroup = itemView.findViewById<TextView>(R.id.connection_notification_accept)
        val viewGroup = itemView.findViewById<TextView>(R.id.connection_notification_decline)
        val users = itemView.findViewById<TextView>(R.id.users)
        val location = itemView.findViewById<TextView>(R.id.location_notification)
        val profile = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCommunityViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_notification_community, parent, false)
        return ViewAllCommunityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllCommunityViewHolder, position: Int) {
        holder.name.text = listS[position].group_name
        holder.joinGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        holder.joinGroup.setTextColor(ContextCompat.getColor(context, R.color.white))
        holder.joinGroup.text = "Join"

        groupId = listS[position].group_id

        holder.users.text = listS[position].Members.size.toString()
//        holder.joinGroup.setOnClickListener {
//            if (listS[position].join == "Join"){
//                context.startActivity(Intent(context, OpenCommunityDetailsActivity::class.java))
//            } else {
//                context.startActivity(Intent(context, ClosedCommunityDetailsActivity::class.java))
//            }
//        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)

        try {
            holder.location.text = listS[position].location
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }


        holder.viewGroup.setOnClickListener {
            val intent = Intent(context,OpenCommunityDetailsActivity::class.java)
            intent.putExtra("groupId",listS[position].group_id.toLong())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }

        val sharedPreferences1 = context.getSharedPreferences("savePhoneNo", AppCompatActivity.MODE_PRIVATE)
        val phone = sharedPreferences1?.getString("savePhoneNumber", "").toString()

        number.add(phone)

       holder.joinGroup.setOnClickListener {
           addMembers()
       }
    }

    fun searchView(searchText : ArrayList<GetCommunitiesData>){
        listS = searchText
        notifyDataSetChanged()
    }
    private fun addMembers() {


// Remove any non-digit characters from each phone number
        val cleanedNumbers = number!!.map { it.replace(Regex("[^\\d]"), "+") }

// Join the cleaned phone numbers into a comma-separated string
        val formattedMembers = cleanedNumbers.joinToString(",")

        val groupId = groupId


        val data = AddMemberData(groupId.toString(), formattedMembers)

        val send = RetrofitBuilder.retrofitBuilder.addMember(data)

        send.enqueue(object : Callback<ResponseGroup?> {
            override fun onResponse(
                call: Call<ResponseGroup?>,
                response: Response<ResponseGroup?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(context,response.result.toString(), Toast.LENGTH_SHORT).show()
                    addCommunityMember()
                }else{
                    Toast.makeText(context,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseGroup?>, t: Throwable) {
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addCommunityMember() {
        val sharedPreferences2 = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences2?.getString("user_id", "").toString()
        val members = user_id
        val groupId = groupId

            val data = AddUserDataClass(members)

            val send = RetrofitBuilder.feedsApi.addUser(groupId.toString(), data)

            send.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(context, MesiboMessagingActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(MesiboUI.GROUP_ID, groupId.toLong())
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })


    }

}