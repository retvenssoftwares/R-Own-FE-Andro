package app.retvens.rown.NavigationFragments.profile.media

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.NavigationFragments.profile.status.StatusAdapter
import app.retvens.rown.R
import com.bumptech.glide.Glide

class MediaAdapter(val context: Context,val mediaList:List<PostItem>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(dataItem: PostItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

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

        val media = mediaList[position]


        media.media.forEach { item ->

                Glide.with(context).load(item.post).into(holder.post_img)

            }



        holder.itemView.setOnLongClickListener {
                onItemClickListener?.onItemClick(media)
            true
        }



        holder.itemView.setOnClickListener {



            val intent = Intent(context,PostDetailsActivity::class.java)
            intent.putExtra("profilePic",media.Profile_pic)
            intent.putExtra("profileName",media.Full_name)
            intent.putExtra("userName",media.User_name)
            intent.putExtra("caption",media.caption)
            val images:ArrayList<String> = ArrayList()
            media.media.forEach { item ->
                images.add(item.post)
                intent.putExtra("time",item.date_added)
            }
            intent.putStringArrayListExtra("postPic",images)
            intent.putExtra("location",media.location)
            intent.putExtra("likeCount",media.likeCount)
            intent.putExtra("commentCount",media.commentCount)
            intent.putExtra("like",media.liked)
            intent.putExtra("islike",media.islike)
            intent.putExtra("postId",media.post_id)
            intent.putExtra("saved",media.isSaved)
            context.startActivity(intent)
        }
    }
}