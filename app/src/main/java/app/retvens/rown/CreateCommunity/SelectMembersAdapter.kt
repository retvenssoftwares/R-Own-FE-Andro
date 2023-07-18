package app.retvens.rown.CreateCommunity

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import com.bumptech.glide.Glide

class SelectMembersAdapter(val context: Context, var userList:List<Connections>) :
    RecyclerView.Adapter<SelectMembersAdapter.ProfileViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    // Define an interface for the listener
    interface OnItemClickListener {
        fun onItemClick(member: Connections)
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
        holder.nameTextView.text = data.Full_name
        if (data.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        } else {
            holder.profile.setImageResource(R.drawable.svg_user)
        }

        holder.bioText.text = data.Role

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(data)

            data.isClick = !data.isClick

            // Update checkmark icon visibility
            if (data.isClick) {
                Log.e("check","okkk")
                holder.check.visibility = View.VISIBLE
            } else {
                holder.check.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Connections>) {
        userList = newItems
        notifyDataSetChanged()
    }

}