package app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.events.EventDetailsActivity
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class OnGoingEventsAdapter(val listS : List<OnGoingEventsData>, val context: Context) : RecyclerView.Adapter<OnGoingEventsAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val price = itemView.findViewById<TextView>(R.id.price)
        val dateAdded = itemView.findViewById<TextView>(R.id.date_added)
        val pic = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllVendorsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_events_ongoing, parent, false)
        return ViewAllVendorsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllVendorsViewHolder, position: Int) {
        val item = listS[position]
        holder.name.text = item.event_title

        if (listS[position].date_added != null) {
            holder.dateAdded.text = dateFormat(listS[position].date_added)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra("title", listS[position].event_title)
            intent.putExtra("about", listS[position].event_description)
            intent.putExtra("price", listS[position].price)
            intent.putExtra("userId", listS[position].user_id)
            intent.putExtra("User_name", listS[position].User_name)
            intent.putExtra("Profile_pic", listS[position].Profile_pic)
            intent.putExtra("eventId", listS[position].event_id)
            intent.putExtra("cover", listS[position].event_thumbnail)
            intent.putExtra("location", listS[position].location)
            context.startActivity(intent)
        }
        holder.price.text = item.price
        Glide.with(context).load(item.event_thumbnail).into(holder.pic)
    }
}