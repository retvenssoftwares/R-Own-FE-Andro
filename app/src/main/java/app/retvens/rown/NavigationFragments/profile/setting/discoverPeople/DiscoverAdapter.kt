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

class DiscoverAdapter(val listS : ArrayList<MatchedContact>, val context: Context) : RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder>() {

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

        holder.title.text = currentItem!!.matchedNumber.Full_name
        holder.role.text = currentItem.matchedNumber.Role
        holder.connect.text = "CONNECT"
        holder.reject.visibility = View.GONE
        holder.reject.text = "MESSAGE"

        val verificationStatus = currentItem.matchedNumber.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }
        if (currentItem.matchedNumber.Role.isNotEmpty()) {
            holder.role.text = currentItem.matchedNumber.Role
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

        if(currentItem.matchedNumber!!.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(currentItem.matchedNumber.Profile_pic).into(holder.profile)
        }

        holder.connect.setOnClickListener {

            if (status == "Not Connected") {
//                connectClickListener?.onJobSavedClick(currentItem)

                currentItem.connectionStatus = "Requested"
                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
                sendConnectionRequest(currentItem.matchedNumber.User_id, context, holder.reject)

                holder.connect.text = "Requested"
            }

            if (status == "Requested" || currentItem.connectionStatus == "Requested") {
//                connectClickListener?.onCancelRequest(currentItem)

                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.green_own))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.connect.text = "CONNECT"
                currentItem.connectionStatus = "Not Connected"
                removeConnRequest(currentItem.matchedNumber.User_id, context, holder.reject)

            }

            if (holder.connect.text == "Interact") {
                val intent = Intent(context, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, currentItem.matchedNumber.Mesibo_account[0].address)
                context.startActivity(intent)
            }

            status = "Requested"

        }

        holder.itemView.setOnClickListener {
            if(currentItem.matchedNumber.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",currentItem.matchedNumber.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.matchedNumber.Mesibo_account[0].address)
                context.startActivity(intent)
            }else if (currentItem.matchedNumber.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",currentItem.matchedNumber.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.matchedNumber.Mesibo_account[0].address)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",currentItem.matchedNumber.User_id)
                intent.putExtra("status",status)
                intent.putExtra("address",currentItem.matchedNumber.Mesibo_account[0].address)
                context.startActivity(intent)
            }
        }

    }

    fun removeUsersFromList(data: List<MatchedContact>){
        try {
            data.forEach {
                if (it.matchedNumber.display_status == "0"){
                    listS!!.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

    fun removeUser(data: List<MatchedContact>){
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        try {
            data.forEach {
                if (user_id == it.matchedNumber.User_id){
                    listS!!.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}