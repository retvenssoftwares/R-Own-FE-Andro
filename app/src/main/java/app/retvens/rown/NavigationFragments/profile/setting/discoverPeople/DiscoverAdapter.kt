package app.retvens.rown.NavigationFragments.profile.setting.discoverPeople

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide

class DiscoverAdapter(val listS : List<UserProfileRequestItem>, val context: Context) : RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder>() {

    class DiscoverViewHolder(itemView: View) : ViewHolder(itemView){
        val profile = itemView.findViewById<ImageView>(R.id.suggetions_notification_profile)
        val title = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        val accept = itemView.findViewById<TextView>(R.id.suggetions_notification_connect)
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
        holder.title.text = listS[position].Full_name
        holder.accept.text = "MESSAGE"
        holder.reject.text = "CONNECT"

        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)

    }
}