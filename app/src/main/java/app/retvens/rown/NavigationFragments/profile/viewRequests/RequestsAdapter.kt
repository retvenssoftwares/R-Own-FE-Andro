package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

class RequestsAdapter(val listS : List<RequestsData>, val context: Context) : RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {

    class RequestsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        val accept = itemView.findViewById<TextView>(R.id.suggetions_notification_connect)
        val reject = itemView.findViewById<TextView>(R.id.suggetions_notification_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggetions_notification, parent, false)
        return RequestsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        holder.title.text = listS[position].title
        holder.accept.text = "Accept"
        holder.reject.text = "Reject"


    }
}