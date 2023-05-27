package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.EventDetailsActivity
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class AllEventsPostedAdapter(val listS : List<OnGoingEventsData>, val context: Context) : RecyclerView.Adapter<AllEventsPostedAdapter.AllEventsPostedViewHolder>() {

    class AllEventsPostedViewHolder(itemView: View) : ViewHolder(itemView){
        val event_date = itemView.findViewById<TextView>(R.id.event_date)
        val title = itemView.findViewById<TextView>(R.id.blog_title)
        val scheduledText = itemView.findViewById<TextView>(R.id.scheduledText)
        val cover = itemView.findViewById<ImageView>(R.id.blog_cover)

        val edit = itemView.findViewById<CardView>(R.id.card_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllEventsPostedViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_posted_events_card, parent, false)
        return AllEventsPostedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: AllEventsPostedViewHolder, position: Int) {

        holder.title.text = listS[position].event_title

        if (listS[position].date_added != null) {
            holder.event_date.text = dateFormat(listS[position].date_added)
        }

//        holder.scheduledText.text = listS[position].event_category
        Glide.with(context).load(listS[position].event_thumbnail).into(holder.cover)

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

        holder.edit.setOnClickListener {
            val intent = Intent(context, EditEventActivity::class.java)
            intent.putExtra("location", listS[position].location)
            intent.putExtra("venue", listS[position].venue)

            intent.putExtra("title", listS[position].event_title)
            intent.putExtra("description", listS[position].event_description)
            intent.putExtra("eventCategory", listS[position].event_category)
            intent.putExtra("category_id", listS[position].category_id)

            intent.putExtra("email", listS[position].email)
            intent.putExtra("phone", listS[position].phone)
            intent.putExtra("website", listS[position].website_link)
            intent.putExtra("booking", listS[position].booking_link)
            intent.putExtra("price", listS[position].price)

            intent.putExtra("cover", listS[position].event_thumbnail)
            intent.putExtra("eStartDate", listS[position].event_start_date)
            intent.putExtra("eStartTime", listS[position].event_start_time)
            intent.putExtra("eEndDate", listS[position].event_end_date)
            intent.putExtra("eEndTime", listS[position].event_end_time)
            intent.putExtra("rStartDate", listS[position].registration_start_date)
            intent.putExtra("rStartTime", listS[position].registration_start_time)
            intent.putExtra("rEndDate", listS[position].registration_end_date)
            intent.putExtra("rEndTime", listS[position].registration_end_time)

            intent.putExtra("userId", listS[position].user_id)
            intent.putExtra("User_name", listS[position].User_name)
            intent.putExtra("Profile_pic", listS[position].Profile_pic)
            intent.putExtra("eventId", listS[position].event_id)
            context.startActivity(intent)
        }
    }
}