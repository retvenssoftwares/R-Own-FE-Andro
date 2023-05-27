package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.eventForUsers.allEvents.SeeAllEventsActivity
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.bumptech.glide.Glide

class EventCategoriesAdapter(val listS : List<ViewAllCategoriesData>, val context: Context) : RecyclerView.Adapter<EventCategoriesAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val categoryName = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val blogsQuantity = itemView.findViewById<TextView>(R.id.personal_notification_time)
        val profile = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
        val endPic = itemView.findViewById<ImageView>(R.id.personal_notification_end_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllVendorsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_personal_notification, parent, false)
        return ViewAllVendorsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllVendorsViewHolder, position: Int) {
        holder.endPic.setPadding(40)
        holder.endPic.setImageResource(R.drawable.ic_right_arrow)

        Glide.with(context).load(listS[position].Image).into(holder.profile)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SeeAllEventsActivity::class.java)
            intent.putExtra("id", listS[position].category_id)
            intent.putExtra("name", listS[position].category_name)
            context.startActivity(intent)
        }

        holder.blogsQuantity.text = "${listS[position].event_count.toString()} events"
        holder.categoryName.text = listS[position].category_name

    }
}