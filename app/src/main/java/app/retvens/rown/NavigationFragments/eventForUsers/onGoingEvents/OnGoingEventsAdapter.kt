package app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

class OnGoingEventsAdapter(val listS : List<OnGoingEventsData>, val context: Context) : RecyclerView.Adapter<OnGoingEventsAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
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

        holder.name.text = listS[position].title

    }
}