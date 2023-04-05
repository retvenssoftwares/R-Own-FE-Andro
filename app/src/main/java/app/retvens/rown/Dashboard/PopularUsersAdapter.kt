package app.retvens.rown.Dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ChatSection.ChatScreen
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.R

class PopularUsersAdapter(val context: Context, var userList:List<MesiboUsersData>) :
    RecyclerView.Adapter<PopularUsersAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.connection_name)
        var lastSeen = itemView.findViewById<TextView>(R.id.active_timeMessage)

        var button = itemView.findViewById<Button>(R.id.connectUser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardofusers, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]

        holder.nameTextView.text = data.address

        holder.button.setOnClickListener {
            holder.button.setTextColor(Color.BLACK)
            holder.button.setText("Request")
            holder.button.setBackgroundColor(Color.BLUE)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }
}