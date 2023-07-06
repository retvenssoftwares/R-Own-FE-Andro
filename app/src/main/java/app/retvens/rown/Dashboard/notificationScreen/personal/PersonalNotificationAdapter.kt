package app.retvens.rown.Dashboard.notificationScreen.personal

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
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class PersonalNotificationAdapter(val listS : List<PersonalNotificationDataItem>, val context: Context) : RecyclerView.Adapter<PersonalNotificationAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val categoryName = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val date = itemView.findViewById<TextView>(R.id.personal_notification_time)
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
        if (listS[position].callType == "Audio"){
            holder.endPic.setImageResource(com.mesibo.messaging.R.drawable.baseline_call_missed_black_24)
        } else if (listS[position].callType == "Video"){
            holder.endPic.setImageResource(com.mesibo.messaging.R.drawable.baseline_missed_video_call_black_24)
        } else {
            holder.endPic.setImageResource(R.drawable.svg_notification)
        }

        if (listS[position].date_added.isNotEmpty()) {
            holder.date.text = dateFormat(listS[position].date_added)
        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)
        holder.categoryName.text = listS[position].body

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, AllBlogsActivity::class.java)
//            intent.putExtra("id", listS[position].category_id)
//            intent.putExtra("name", listS[position].category_name)
//            context.startActivity(intent)
        }

    }
}