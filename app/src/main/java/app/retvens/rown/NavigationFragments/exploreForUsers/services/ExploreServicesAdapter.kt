package app.retvens.rown.NavigationFragments.exploreForUsers.services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.viewAll.vendorsDetails.VendorDetailsActivity
import com.bumptech.glide.Glide

class ExploreServicesAdapter(val listS : List<ProfileServicesDataItem>, val context: Context) : RecyclerView.Adapter<ExploreServicesAdapter.ExploreServicesViewHolder>() {

    class ExploreServicesViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.vendor_name)
        val vendors_id = itemView.findViewById<TextView>(R.id.vendors_id)
        val avg_price = itemView.findViewById<TextView>(R.id.avg_price)

        val cover = itemView.findViewById<ImageView>(R.id.vendor_cover)
        val profile = itemView.findViewById<ImageView>(R.id.vendor_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreServicesViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_services_card, parent, false)
        return ExploreServicesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreServicesViewHolder, position: Int) {
        val recyclerItem = listS[position]
        holder.name.text = listS[position].vendorName
        holder.vendors_id.text = "@${listS[position].User_name}"
        if (listS[position].vendorServicePrice == ""){
            holder.avg_price.text = "000 INR"
        } else {
            holder.avg_price.text = "${listS[position].vendorServicePrice}"
        }
        Glide.with(context).load(listS[position].vendorImage).into(holder.cover)
        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VendorDetailsActivity::class.java)
            intent.putExtra("user_id", recyclerItem.user_id)
            intent.putExtra("vendorImage", recyclerItem.vendorImage)
            intent.putExtra("vendorName", recyclerItem.vendorName)
            context.startActivity(intent)
        }
    }
}