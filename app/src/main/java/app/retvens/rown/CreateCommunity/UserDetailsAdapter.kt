package app.retvens.rown.CreateCommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.Member
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class UserDetailsAdapter(val context: Context, var userList:List<Member>) :
    RecyclerView.Adapter<UserDetailsAdapter.ProfileViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    // Define an interface for the listener
    interface OnItemClickListener {
        fun onItemClick(member: UserProfileRequestItem)
    }

    // Define a function to set the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val location = itemView.findViewById<TextView>(R.id.location_notification)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.personal_notification_profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_businessess_in__community, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]
        holder.name.text = data.Full_name
        val location = data.location
        val cityName = location.split(",")[0].trim()
            holder.location.text = cityName
            Glide.with(context).load(data.Profile_pic).into(holder.profile)




    }

    override fun getItemCount(): Int {
        return userList.size
    }


}