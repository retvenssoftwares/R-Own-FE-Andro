package app.retvens.rown.bottomsheet.bottomSheetPeople

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.acceptRequest
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.removeConnection
import app.retvens.rown.utils.sendConnectionRequest
import com.bumptech.glide.Glide

class BottomSheetAdapterPeople(val context: Context, var peopleList:ArrayList<Post>):RecyclerView.Adapter<BottomSheetAdapterPeople.ExplorePeopleViewholder>() {

    val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

    interface ConnectClickListener {
        fun onJobSavedClick(connect: Post)

        fun onCancelRequest(connect: Post)
    }

    private var connectClickListener: ConnectClickListener? = null

    class ExplorePeopleViewholder(itemview:View): ViewHolder(itemview) {

        val role = itemview.findViewById<TextView>(R.id.connection_position)
        val connect = itemview.findViewById<TextView>(R.id.connectUser)

        var name = itemView.findViewById<TextView>(R.id.connection_name)
        var bio = itemView.findViewById<TextView>(R.id.bio)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorePeopleViewholder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.cardofusers, parent, false)
        return ExplorePeopleViewholder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ExplorePeopleViewholder, position: Int) {

        val data = peopleList[position]

        val verificationStatus = data.verificationStatus
        if (verificationStatus != "false") {
            holder.verification.visibility = View.VISIBLE
        }


        if (data.Profile_pic.isNullOrEmpty()) {
            holder.profile.setImageResource(R.drawable.svg_person_account)
        } else {
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        }
        holder.name.text = data.Full_name
        holder.role.text = data.Role
        holder.bio.text = data.userBio

        val userId = data.User_id


        if (data.connectionStatus == "Connected"){
            holder.connect.text = "Interact"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        } else if (data.connectionStatus == "Not connected"){
            holder.connect.text = "CONNECT"
        } else if (data.connectionStatus == "Requested"){
            holder.connect.text = "Requested"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
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

            }
        }

        holder.profile.setOnClickListener {

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

    fun searchConnection(searchText : List<Post>){
        peopleList = searchText as ArrayList<Post>
        notifyDataSetChanged()
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
    fun setJobSavedClickListener(listener: ConnectClickListener) {
        connectClickListener = listener
    }

    fun cancelConnRequest(listener: ConnectClickListener){
        connectClickListener = listener
    }
}