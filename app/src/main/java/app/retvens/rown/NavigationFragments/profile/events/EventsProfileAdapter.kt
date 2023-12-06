package app.retvens.rown.NavigationFragments.profile.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.EventDetailsActivity
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class EventsProfileAdapter(val listS : List<OnGoingEventsData>, val context: Context) : RecyclerView.Adapter<EventsProfileAdapter.EventsProfileViewHolder>() {

    class EventsProfileViewHolder(itemView: View) : ViewHolder(itemView){

        val event_date = itemView.findViewById<TextView>(R.id.blog_date)
        val title = itemView.findViewById<TextView>(R.id.blog_title)
        val blogType = itemView.findViewById<TextView>(R.id.blogType)
        val cover = itemView.findViewById<ImageView>(R.id.blog_cover)

        val bloggerProfile = itemView.findViewById<ImageView>(R.id.blogger_profile)
        val bloggerNamw = itemView.findViewById<TextView>(R.id.blogger)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsProfileViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_profile_events_card, parent, false)
        return EventsProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: EventsProfileViewHolder, position: Int) {

        holder.title.text = listS[position].event_title

        if (listS[position].date_added != null) {
            holder.event_date.text = dateFormat(listS[position].date_added)
        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.bloggerProfile)
        holder.bloggerNamw.text = listS[position].User_name

        Glide.with(context).load(listS[position].event_thumbnail).into(holder.cover)

        holder.blogType.text = listS[position].event_category


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
            intent.putExtra("saved", listS[position].saved)
            context.startActivity(intent)
        }
    }
}