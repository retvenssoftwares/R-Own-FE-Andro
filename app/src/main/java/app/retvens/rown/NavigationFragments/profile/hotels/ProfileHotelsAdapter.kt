package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ProfileHotelsAdapter(val listS : List<HotelsName>, val context: Context) : RecyclerView.Adapter<ProfileHotelsAdapter.ProfileHotelsViewHolder>() {

    class ProfileHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.venue_name)
        val cover = itemView.findViewById<ImageView>(R.id.hotel_venue_cover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHotelsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_profile_hotels_card, parent, false)
        return ProfileHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ProfileHotelsViewHolder, position: Int) {
        holder.name.text = listS[position].hotelName

        Glide.with(context).load(listS[position].hotelLogoUrl).into(holder.cover)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, HotelDetailsProfileActivity::class.java)
            intent.putExtra("name", listS[position].hotelName)
            intent.putExtra("logo", listS[position].hotelLogoUrl)
            intent.putExtra("hotelId", listS[position].hotel_id)
            context.startActivity(intent)
        }
    }
}