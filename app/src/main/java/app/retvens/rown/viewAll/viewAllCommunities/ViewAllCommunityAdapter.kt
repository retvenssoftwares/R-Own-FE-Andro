package app.retvens.rown.viewAll.viewAllCommunities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

class ViewAllCommunityAdapter(val listS : List<ViewAllCommunityData>, val context: Context) : RecyclerView.Adapter<ViewAllCommunityAdapter.ViewAllCommunityViewHolder>() {

    class ViewAllCommunityViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val joinGroup = itemView.findViewById<TextView>(R.id.connection_notification_accept)
        val viewGroup = itemView.findViewById<TextView>(R.id.connection_notification_decline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCommunityViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_notification_community, parent, false)
        return ViewAllCommunityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllCommunityViewHolder, position: Int) {
        holder.name.text = listS[position].title
        holder.joinGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        holder.joinGroup.setTextColor(ContextCompat.getColor(context, R.color.white))
        holder.joinGroup.text = listS[position].join

        holder.joinGroup.setOnClickListener {
            if (listS[position].join == "Join"){
                context.startActivity(Intent(context, OpenCommunityDetailsActivity::class.java))
            } else {
                context.startActivity(Intent(context, ClosedCommunityDetailsActivity::class.java))
            }
        }
    }
}