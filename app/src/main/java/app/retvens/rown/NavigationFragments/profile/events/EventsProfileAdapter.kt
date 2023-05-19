package app.retvens.rown.NavigationFragments.profile.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.events.EventDetailsActivity
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R

class EventsProfileAdapter(val listS : List<ExploreEventsData>, val context: Context) : RecyclerView.Adapter<EventsProfileAdapter.EventsProfileViewHolder>() {

    class EventsProfileViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.blog_title)
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
        holder.title.text = listS[position].title
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, EventDetailsActivity::class.java))
        }
    }
}