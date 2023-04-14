package app.retvens.rown.CreateCommunity

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ChatSection.ChatScreen
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.R

class SelectMembersAdapter(val context: Context, var userList:List<MesiboUsersData>) :
    RecyclerView.Adapter<SelectMembersAdapter.ProfileViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    // Define an interface for the listener
    interface OnItemClickListener {
        fun onItemClick(member: MesiboUsersData)
    }

    // Define a function to set the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.membersName)
        var bioText = itemView.findViewById<TextView>(R.id.membersBio)
        var profile = itemView.findViewById<ImageView>(R.id.memberspic)
        var check = itemView.findViewById<ImageView>(R.id.check)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listgrpmembers, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]

        holder.nameTextView.text = data.address

        var isProfileVisible = false

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(data)
        }

        holder.itemView.setOnClickListener {
            if (isProfileVisible) {
                holder.check.visibility = View.GONE
            } else {
                holder.check.visibility = View.VISIBLE
            }
            isProfileVisible = !isProfileVisible
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }


}