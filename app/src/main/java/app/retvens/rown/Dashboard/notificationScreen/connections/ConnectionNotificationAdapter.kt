package app.retvens.rown.Dashboard.notificationScreen.connections

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationDataItem
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class ConnectionNotificationAdapter(val listS : List<PersonalNotificationDataItem>, val context: Context) : RecyclerView.Adapter<ConnectionNotificationAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val categoryName = itemView.findViewById<TextView>(R.id.connection_notification_name)
        val date = itemView.findViewById<TextView>(R.id.connection_notification_time)
        val profile = itemView.findViewById<ImageView>(R.id.connection_notification_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
        val ca_accept = itemView.findViewById<CardView>(R.id.ca_accept)
        val ca_decline = itemView.findViewById<CardView>(R.id.ca_decline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllVendorsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_connections_notification, parent, false)
        return ViewAllVendorsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllVendorsViewHolder, position: Int) {

        val data = listS[position]

        val verificationStatus = data.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)
        holder.categoryName.text = listS[position].body
        holder.date.text =  dateFormat(listS[position].date_added)

        holder.ca_accept.visibility = View.GONE
        holder.ca_decline.visibility = View.GONE

        holder.itemView.setOnClickListener {
            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            }
        }

    }
}