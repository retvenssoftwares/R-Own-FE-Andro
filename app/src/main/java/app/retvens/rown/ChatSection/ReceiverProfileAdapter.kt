package app.retvens.rown.ChatSection

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.R
import com.mesibo.api.MesiboProfile

class ReceiverProfileAdapter(val context: Context, var userList:List<MesiboUsersData>) :
    RecyclerView.Adapter<ReceiverProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.receiver_name)
        var lastSeen = itemView.findViewById<TextView>(R.id.active_timeMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatprofile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]

        holder.nameTextView.text = data.address

        val lastSeen = data.lastonline

        val timestamp = lastSeen
        val now = System.currentTimeMillis() // get the current time in milliseconds
        val relativeTime = DateUtils.getRelativeTimeSpanString(timestamp * 1000L, now, DateUtils.SECOND_IN_MILLIS) // get the relative time

        holder.lastSeen.setText("Active $relativeTime")

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatScreen::class.java)
            intent.putExtra("address",data.address)
            intent.putExtra("userId",data.uid)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}