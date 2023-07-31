package app.retvens.rown.NavigationFragments.home

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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.BlockAccount
import app.retvens.rown.DataCollections.ConnectionCollection.BlockUserDataClass
import app.retvens.rown.DataCollections.FeedCollection.LikeDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.role
import com.bumptech.glide.Glide
import com.mesibo.api.Mesibo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeAdapter(val context: Context, var userList: LikeDataClass) :
    RecyclerView.Adapter<LikeAdapter.ProfileViewHolder>() {

    private var address = ""

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        var button = itemView.findViewById<CardView>(R.id.ca_connect)
        var status = itemView.findViewById<ImageView>(R.id.verification)
        var view = itemView.findViewById<CardView>(R.id.ca_view_profile)
        var image = itemView.findViewById<ImageView>(R.id.suggetions_notification_profile)
        var role = itemView.findViewById<TextView>(R.id.suggetions_notification_role)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggetions_notification, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList.post.likes[position]

        holder.nameTextView.text = data.Full_name

        holder.role.text = data.Role

        fetchUser(data.user_id)

        if (data.verificationStatus == "true") {
            holder.status.visibility = View.VISIBLE
        } else if (data.verificationStatus == "false") {
           holder.status.visibility = View.GONE
        }

        if (data.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.Profile_pic).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.svg_user)
        }

        holder.image.setOnClickListener {
            if (data.Role == "Business Vendor / Freelancer") {
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", data.user_id)
                context.startActivity(intent)
            } else if (data.Role == "Hotel Owner") {
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", data.user_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", data.user_id)
                context.startActivity(intent)
            }
        }

       holder.view.visibility = View.GONE
        holder.button.visibility = View.GONE

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
                    Toast.makeText(context,response.message, Toast.LENGTH_SHORT).show()
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
        return userList.post.likes.size
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newItems: List<Connections>) {
//        userList = newItems
//        notifyDataSetChanged()
//    }
}