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
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide

class MediaAdapter(val context: Context,val mediaList:List<PostItem>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(itemView: View) : ViewHolder(itemView){
        val post_img = itemView.findViewById<ImageView>(R.id.posts_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

        val media = mediaList[position]

        media.media.forEach { item ->

            Glide.with(context).load(item.post).into(holder.post_img)

        }


        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, PostDetailsActivity::class.java))
        }
    }
}