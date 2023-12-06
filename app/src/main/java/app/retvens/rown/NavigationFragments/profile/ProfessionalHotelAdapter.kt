package app.retvens.rown.NavigationFragments.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.MediaDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ProfessionalHotelAdapter(val context: Context, val list:List<HotelsName>) : RecyclerView.Adapter<ProfessionalHotelAdapter.MediaViewHolder>() {



    class MediaViewHolder(itemView: View) : ViewHolder(itemView){

        val name = itemView.findViewById<TextView>(R.id.hotelName)
        val star = itemView.findViewById<TextView>(R.id.textExperience)
        val location = itemView.findViewById<TextView>(R.id.location)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_property_card, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

        val data = list[position]
            holder.name.text = data.hotelName
            holder.star.text = "${data.hotelRating} Star Hotel"
            holder.location.text = data.hotelAddress




    }


}