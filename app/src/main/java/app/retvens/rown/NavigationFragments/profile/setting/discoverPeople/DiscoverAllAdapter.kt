package app.retvens.rown.NavigationFragments.profile.setting.discoverPeople

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.bottomSheetPeople.MatchedContact
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.sendConnectionRequest
import com.bumptech.glide.Glide

class DiscoverAllAdapter(val listS : ArrayList<Post>, val context: Context) : RecyclerView.Adapter<DiscoverAllAdapter.DiscoverViewHolder>() {

    class DiscoverViewHolder(itemView: View) : ViewHolder(itemView){
        val profile = itemView.findViewById<ImageView>(R.id.suggetions_notification_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
        val title = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        val role = itemView.findViewById<TextView>(R.id.suggetions_notification_role)
        val connect = itemView.findViewById<TextView>(R.id.suggetions_notification_connect)
        val reject = itemView.findViewById<TextView>(R.id.suggetions_notification_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggetions_notification, parent, false)
        return DiscoverViewHolder(view)
    }

    override fun getItemCount(): Int {
           return listS.size
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val currentItem = listS?.get(position)

        holder.title.text = currentItem!!.Full_name
        holder.role.text = currentItem.Role
        holder.connect.text = "CONNECT"
        holder.reject.visibility = View.GONE
        holder.reject.text = "MESSAGE"

        val verificationStatus = currentItem.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }
        if (currentItem.Role.isNotEmpty()) {
            holder.role.text = currentItem.Role
        } else {
            holder.role.text = "Incomplete Profile"
        }

        var status = currentItem.connectionStatus

        if (currentItem.connectionStatus == "Connected") {
            holder.connect.text = "Interact"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }
        if (currentItem.connectionStatus == "Requested") {
            holder.connect.text = "Requested"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        if(currentItem.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(currentItem.Profile_pic).into(holder.profile)
        }

        holder.connect.setOnClickListener {

            if (status == "Not Connected") {
//                connectClickListener?.onJobSavedClick(currentItem)

                sendConnectionRequest(currentItem.User_id, context, holder.connect)
                currentItem.connectionStatus = "Requested"

//                holder.connect.text = "Requested"
//                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
//                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
            }

            if (status == "Requested" || currentItem.connectionStatus == "Requested") {
//                connectClickListener?.onCancelRequest(currentItem)

                removeConnRequest(currentItem.User_id, context, holder.connect)

//                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.green_own))
//                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.white))
//                holder.connect.text = "CONNECT"
                currentItem.connectionStatus = "Not Connected"
            }

            if (holder.connect.text == "Interact") {
                val intent = Intent(context, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, currentItem.Mesibo_account[0].address)
                context.startActivity(intent)
            }

            status = "Requested"

        }

        holder.itemView.setOnClickListener {

            if(currentItem.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",currentItem.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.Mesibo_account[0].address)
                context.startActivity(intent)
            }else if (currentItem.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",currentItem.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.Mesibo_account[0].address)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",currentItem.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.Mesibo_account[0].address)
                context.startActivity(intent)
            }
        }

    }

    fun removeUsersFromList(data: List<Post>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    listS!!.remove(it)
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
                    listS!!.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}