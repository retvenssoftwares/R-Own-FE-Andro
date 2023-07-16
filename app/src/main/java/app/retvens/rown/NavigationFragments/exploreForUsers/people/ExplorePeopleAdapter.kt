package app.retvens.rown.NavigationFragments.exploreForUsers.people

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.acceptRequest
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ExplorePeopleAdapter(val context: Context,val peopleList:ArrayList<Post>):RecyclerView.Adapter<ExplorePeopleAdapter.ExplorePeopleViewholder>() {

    val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    interface ConnectClickListener {
        fun onJobSavedClick(connect: Post)

        fun onCancelRequest(connect: Post)
    }

    private var connectClickListener: ConnectClickListener? = null

    class ExplorePeopleViewholder(itemview:View): ViewHolder(itemview) {

        val name = itemview.findViewById<TextView>(R.id.explore_people_name)
        val role = itemview.findViewById<TextView>(R.id.suggetions_notification_role)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.explore_people_profile)
        val connect = itemview.findViewById<TextView>(R.id.suggetions_notification_connect)
        val viewProfile = itemview.findViewById<CardView>(R.id.ca_view_profile)
        val verification = itemview.findViewById<ImageView>(R.id.verification)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorePeopleViewholder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_people, parent, false)
        return ExplorePeopleViewholder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ExplorePeopleViewholder, position: Int) {

        val data = peopleList[position]

        val verificationStatus = data.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }


        if (data.Profile_pic.isNotEmpty()){
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        }else{
            holder.profile.setImageResource(R.drawable.svg_user)
        }
        holder.name.text = data.Full_name
        holder.role.text = data.Role

        val userId = data.User_id

        if (data.connectionStatus == "Connected"){
            holder.connect.text = "Interact"
        } else if (data.connectionStatus == "Not connected"){
            holder.connect.text = "CONNECT"
        } else if (data.connectionStatus == "Requested"){
            holder.connect.text = "Requested"
        } else if (data.connectionStatus == "Confirm request"){
            holder.connect.text = "Accept"
        }
        holder.connect.setOnClickListener {
            if (holder.connect.text == "Remove"){

                removeConnection(userId,user_id, context){
                    holder.connect.text = "CONNECT"
                }

            } else if (holder.connect.text == "CONNECT") {

                sendConnectionRequest(userId, context){
                    holder.connect.text = "Requested"
                }

            } else  if (holder.connect.text == "Requested") {

                removeConnRequest(userId, context){
                    holder.connect.text = "CONNECT"
                }

            } else if (holder.connect.text == "Accept") {

                acceptRequest(userId, context){
                    holder.connect.text = "Remove"
                }

            } else if (holder.connect.text == "Interact") {

                val intent = Intent(context, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, data.Mesibo_account.get(0).address)
                context.startActivity(intent)

            }
        }


        holder.viewProfile.setOnClickListener {

            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }
        }

        holder.name.setOnClickListener {
            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
       return peopleList.size
    }

    fun removeUsersFromList(data: List<Post>){

        try {
            data.forEach {
                if (it.display_status == "0"){
                    peopleList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
    fun removeUser(data: List<Post>){
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        try {

            data.forEach {
                if (user_id == it.User_id){
                    peopleList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
    fun removeEmptyNameUser(data: List<Post>){
        try {
            data.forEach {
                if (it.Full_name.isEmpty()){
                    peopleList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

    fun setJobSavedClickListener(listener: ConnectClickListener) {
        connectClickListener = listener
    }
    fun cancelConnRequest(listener: ConnectClickListener){
        connectClickListener = listener
    }
}