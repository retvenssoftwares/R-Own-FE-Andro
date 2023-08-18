package app.retvens.rown.NavigationFragments.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.BlockAccount
import app.retvens.rown.DataCollections.ConnectionCollection.BlockUserDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockUserAdapter(val context: Context, var userList: List<BlockUserDataClass>) :
    RecyclerView.Adapter<BlockUserAdapter.ProfileViewHolder>() {

    private var address = ""

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.share_fullname)
        var button = itemView.findViewById<CardView>(R.id.ca_connect)
        var image = itemView.findViewById<ImageView>(R.id.share_profile)
        var text = itemView.findViewById<TextView>(R.id.shared)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_share_post, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]

        holder.nameTextView.text = data.Full_name

        holder.text.text = "Unblock"

        fetchUser(data.User_id)

        if (data.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.Profile_pic).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.svg_user)
        }

        var click = "unblock"
        holder.button.setOnClickListener {
            if (click == "unblock"){
                holder.button.visibility = View.GONE
                unBlockUser(data.User_id)
                click = "block"
            }

        }

//        val timestamp = lastSeen
//        val now = System.currentTimeMillis() // get the current time in milliseconds
//        val relativeTime = DateUtils.getRelativeTimeSpanString(timestamp * 1000L, now, DateUtils.SECOND_IN_MILLIS) // get the relative time
//
//        holder.lastSeen.setText("Active $relativeTime")


    }

    private fun fetchUser(userId: String) {
        val getData = RetrofitBuilder.retrofitBuilder.fetchUser(userId)

        getData.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    address = response.Mesibo_account[0].address
                }else{
                    Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun unBlockUser(userId: String) {

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val Userid = sharedPreferences?.getString("user_id", "").toString()

        val unblock = RetrofitBuilder.ProfileApis.unblockAccount(Userid, BlockAccount(userId))

        unblock.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                    val profile = Mesibo.getProfile(address)
                    profile.block(false)
                    profile.save()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    override fun getItemCount(): Int {
        return userList.size
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newItems: List<Connections>) {
//        userList = newItems
//        notifyDataSetChanged()
//    }
}