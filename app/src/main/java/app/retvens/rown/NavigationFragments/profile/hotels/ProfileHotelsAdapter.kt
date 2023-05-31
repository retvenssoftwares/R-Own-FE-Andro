package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ProfileHotelsAdapter(val listS : List<HotelsName>, val context: Context) : RecyclerView.Adapter<ProfileHotelsAdapter.ProfileHotelsViewHolder>() {

    class ProfileHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.venue_name)
        val locationHotel = itemView.findViewById<TextView>(R.id.location_hotel)

        val cover = itemView.findViewById<ImageView>(R.id.hotel_venue_cover)
        val hotelRating = itemView.findViewById<RatingBar>(R.id.hotelRating)
        val edit = itemView.findViewById<CardView>(R.id.read_more_blog)
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
        holder.locationHotel.text = listS[position].hotelAddress

        Glide.with(context).load(listS[position].hotelCoverpicUrl).into(holder.cover)

//        if (listS[position] != null) {
//            holder.hotelRating.rating = listS[position].hotelRating.toFloat()
//        }

//        holder.edit.setOnClickListener {
//            val intent = Intent(this, EditHotelDetailsActivity::class.java)
//            intent.putExtra("name", listS[position].hotelName)
//            intent.putExtra("img1", listS[position].)
//            intent.putExtra("img2", img2)
//            intent.putExtra("img3", img3)
//            intent.putExtra("location", location)
//            intent.putExtra("hotelDescription", Hoteldescription)
//            intent.putExtra("hotelId", hotelId)
//            context.startActivity(intent)
//        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, HotelDetailsProfileActivity::class.java)
            intent.putExtra("name", listS[position].hotelName)
            intent.putExtra("logo", listS[position].hotelCoverpicUrl)
            intent.putExtra("hotelId", listS[position].hotel_id)
            intent.putExtra("hotelAddress", listS[position].hotelAddress)
            context.startActivity(intent)
        }
    }
}