package app.retvens.rown.NavigationFragments.profile.services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ProfileServicesAdapter(val listS : List<ProfileServicesDataItem>, val context: Context) : RecyclerView.Adapter<ProfileServicesAdapter.PollsViewHolder>() {

    class PollsViewHolder(itemView: View) : ViewHolder(itemView){
        val vendor_name = itemView.findViewById<TextView>(R.id.vendor_name)
        val vendors_id = itemView.findViewById<TextView>(R.id.vendors_id)
        val serviceName = itemView.findViewById<TextView>(R.id.serviceName)
        val servicePrice = itemView.findViewById<TextView>(R.id.servicePrice)
        val del = itemView.findViewById<ImageView>(R.id.del)
        val vendor_profile = itemView.findViewById<ImageView>(R.id.vendor_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_services_profile, parent, false)
        return PollsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: PollsViewHolder, position: Int) {
        val data = listS[position]
        holder.vendor_name.text = data.vendorName
        holder.vendors_id.text = data.user_id
        holder.serviceName.text = data.service_name
        holder.servicePrice.text = data.vendorServicePrice
        Glide.with(context).load(data.Profile_pic).into(holder.vendor_profile)

        holder.del.setOnClickListener {

        }


    }
}