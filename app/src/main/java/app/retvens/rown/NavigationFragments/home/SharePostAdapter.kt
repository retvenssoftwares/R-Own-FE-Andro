package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.FeedCollection.Member
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class SharePostAdapter(val context: Context, var userList:List<Connections>) :
    RecyclerView.Adapter<SharePostAdapter.ProfileViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    // Define an interface for the listener
    interface OnItemClickListener {
        fun onItemClick(data:Connections)
    }

    // Define a function to set the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.share_fullname)
        val username = itemView.findViewById<TextView>(R.id.share_username)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.share_profile)
        val share = itemView.findViewById<CardView>(R.id.ca_connect)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_share_post, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]
        holder.name.text = data.Full_name
        holder.username.text = data.User_id
        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.share.setOnClickListener {
            onItemClickListener?.onItemClick(data)
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }


}