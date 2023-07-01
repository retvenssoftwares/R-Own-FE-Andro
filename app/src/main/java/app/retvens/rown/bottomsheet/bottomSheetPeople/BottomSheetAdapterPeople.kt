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
import com.bumptech.glide.Glide

class BottomSheetAdapterPeople(val context: Context, val peopleList:ArrayList<Post>):RecyclerView.Adapter<BottomSheetAdapterPeople.ExplorePeopleViewholder>() {

    var userId = ""
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

        userId = data.User_id

        var status = data.connectionStatus

        if (data.connectionStatus == "Connected") {
            holder.connect.text = "Interact"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }


        if (data.connectionStatus == "Requested") {
            holder.connect.text = "Requested"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        holder.connect.setOnClickListener {

            if (status == "Not Connected") {
                connectClickListener?.onJobSavedClick(data)

                holder.connect.text = "Requested"
                data.connectionStatus = "Requested"
                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
            }

            if (status == "Requested" || data.connectionStatus == "Requested") {
                connectClickListener?.onCancelRequest(data)

                data.connectionStatus = "Not Connected"
                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.green_own))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.connect.text = "CONNECT"
            }

            if (holder.connect.text == "Interact") {
                val intent = Intent(context, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, data.Mesibo_account[0].address)
                context.startActivity(intent)

            }

            status = "Requested"

        }

        holder.profile.setOnClickListener {

            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",data.Mesibo_account[0].address)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",data.Mesibo_account[0].address)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",data.Mesibo_account[0].address)
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
    fun setJobSavedClickListener(listener: ConnectClickListener) {
        connectClickListener = listener
    }

    fun cancelConnRequest(listener: ConnectClickListener){
        connectClickListener = listener
    }
}