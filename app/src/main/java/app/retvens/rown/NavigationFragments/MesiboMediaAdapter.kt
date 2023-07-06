package app.retvens.rown.NavigationFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.MediaDataClass
import app.retvens.rown.R
import com.bumptech.glide.Glide

class MesiboMediaAdapter(val context: Context, val mediaList:List<Bitmap>) : RecyclerView.Adapter<MesiboMediaAdapter.MediaViewHolder>() {



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

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

        val data = mediaList[position]

        holder.post_img.setImageBitmap(data)

    }


}