package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R

class SavedEventsAdapter(val listS : List<ExploreEventsData>, val context: Context) : RecyclerView.Adapter<SavedEventsAdapter.ExploreEventViewHolder>() {

    class ExploreEventViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.blog_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreEventViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_events_card, parent, false)
        return ExploreEventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreEventViewHolder, position: Int) {
        holder.title.text = listS[position].title
        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context, EventDetailsActivity::class.java))
        }
    }
}