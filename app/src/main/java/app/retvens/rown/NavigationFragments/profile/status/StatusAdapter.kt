package app.retvens.rown.NavigationFragments.profile.status

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
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class StatusAdapter(val listS : List<PostItem>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LAYOUT_ONE = 1
    private val VIEW_TYPE_LAYOUT_TWO = 2


    class LayoutOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val postProfile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val status = itemView.findViewById<TextView>(R.id.title_status)
        val likeButton = itemView.findViewById<ImageView>(R.id.like_post)
        val commentButton = itemView.findViewById<ImageView>(R.id.comment)
        val location = itemView.findViewById<TextView>(R.id.post_time)
        val likeCount = itemView.findViewById<TextView>(R.id.like_count)
        val commentCount = itemView.findViewById<TextView>(R.id.comment_count)
    }


    class LayoutTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val event_Image = itemView.findViewById<ShapeableImageView>(R.id.event_image)
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val status = itemView.findViewById<TextView>(R.id.title_status)
        val title = itemView.findViewById<TextView>(R.id.event_title)
        val username = itemView.findViewById<TextView>(R.id.user_id_on_comment)
        val caption = itemView.findViewById<TextView>(R.id.recentCommentByUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)

        return when (viewType) {
            VIEW_TYPE_LAYOUT_ONE -> {
                val status: View = inflater.inflate(R.layout.item_status, parent, false)
                LayoutOneViewHolder(status)
            }
            VIEW_TYPE_LAYOUT_TWO -> {
                val status: View = inflater.inflate(R.layout.item_event_post, parent, false)
                LayoutTwoViewHolder(status)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {

        val data = listS[position]

        return if (data.post_type == "normal status"){
            VIEW_TYPE_LAYOUT_ONE
        }else{
            VIEW_TYPE_LAYOUT_TWO
        }


    }

    override fun getItemCount(): Int {
        return listS.size
    }


    override fun onBindViewHolder(holder: ViewHolder , position: Int) {

        val item = listS[position]

        when (holder.itemViewType) {
            VIEW_TYPE_LAYOUT_ONE -> {
                val layoutOneViewHolder = holder as LayoutOneViewHolder
                layoutOneViewHolder.name.text = item.User_name
                layoutOneViewHolder.status.text = item.caption
                Glide.with(context).load(item.Profile_pic).into(layoutOneViewHolder.postProfile)

                if (item.Like_count != ""){
                    holder.likeCount.text = item.Like_count
                }
                if (item.Comment_count != ""){
                    holder.commentCount.text = item.Comment_count
                }

                if (item.like == "Liked"){
                    holder.likeButton.setImageResource(R.drawable.liked_vectore)
                }else if (item.like == "Unliked"){
                    holder.likeButton.setImageResource(R.drawable.svg_like_post)
                }


                holder.likeButton.setOnClickListener {
                    item.islike = !item.islike

                    val count:Int
                    if(item.islike){
                        holder.likeButton.setImageResource(R.drawable.liked_vectore)
                        count = item.Like_count.toInt()+1
                        holder.likeCount.text = count.toString()
                    }else{
                        holder.likeButton.setImageResource(R.drawable.svg_like_post)
                        count = item.Like_count.toInt()
                        holder.likeCount.text = count.toString()
                    }

//                    onItemClickListener?.onItemClick(banner)
                }

//                binding.comment.setOnClickListener {
//                    onItemClickListener?.onItemClickForComment(banner,position)
//                }

            }


            VIEW_TYPE_LAYOUT_TWO -> {
                val layoutTwoViewHolder = holder as LayoutTwoViewHolder
                layoutTwoViewHolder.name.text = item.Full_name
                layoutTwoViewHolder.caption.text = item.caption
                layoutTwoViewHolder.title.text = item.Event_name
                layoutTwoViewHolder.username.text = item.User_name
                layoutTwoViewHolder.status.text = "Hello all, I am going to ${item.Event_name} on ${item.event_start_date}"

                Glide.with(context).load(item.Profile_pic).into(layoutTwoViewHolder.profile)
                Glide.with(context).load(item.event_thumbnail).into(layoutTwoViewHolder.event_Image)

            }
        }

    }
}