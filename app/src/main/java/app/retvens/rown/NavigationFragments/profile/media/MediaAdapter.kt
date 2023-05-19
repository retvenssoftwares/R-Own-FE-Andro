package app.retvens.rown.NavigationFragments.profile.media

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.R

class MediaAdapter(val listS : List<MediaData>, val context: Context) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(itemView: View) : ViewHolder(itemView){
        val post_img = itemView.findViewById<ImageView>(R.id.posts_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.post_img.setImageResource(listS[position].image)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, PostDetailsActivity::class.java))
        }
    }
}