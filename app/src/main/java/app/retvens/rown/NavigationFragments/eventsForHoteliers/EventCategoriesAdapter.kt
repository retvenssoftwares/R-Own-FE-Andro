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
import app.retvens.rown.R

class EventCategoriesAdapter(val listS : List<EventCategoriesData>, val context: Context) : RecyclerView.Adapter<EventCategoriesAdapter.ViewAllVendorsViewHolder>() {

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

        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context, AllBlogsActivity::class.java))
        }

        holder.blogsQuantity.text = "12 Events"
        holder.profile.setImageResource(R.drawable.png_profile)
        holder.categoryName.text = listS[position].title

    }
}