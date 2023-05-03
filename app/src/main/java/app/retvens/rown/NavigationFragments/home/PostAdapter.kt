package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.FetchPostDataClass
import app.retvens.rown.R
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val list : List<FetchPostDataClass>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    class PostViewHolder(itemView: View) : ViewHolder(itemView){
        val likedByProfile = itemView.findViewById<ImageView>(R.id.liked_by_profile)
        val likedByUser = itemView.findViewById<TextView>(R.id.liked_by_user)
        val postProfile = itemView.findViewById<ImageView>(R.id.post_profile)
        val likePost = itemView.findViewById<ImageView>(R.id.like_post)
        val commentPost = itemView.findViewById<ImageView>(R.id.comment)
        val sharePost = itemView.findViewById<ImageView>(R.id.share_post)
        val postImage = itemView.findViewById<ImageView>(R.id.post_pic)
        val postUserName = itemView.findViewById<TextView>(R.id.user_name_post)
        val postUserType = itemView.findViewById<TextView>(R.id.post_user_type)
        val postUserD = itemView.findViewById<TextView>(R.id.post_user_dominican)
        val postTime = itemView.findViewById<TextView>(R.id.post_time)
        val userIdOnComment = itemView.findViewById<TextView>(R.id.user_id_on_comment)
        val recentCommentBy = itemView.findViewById<TextView>(R.id.recentCommentByUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.users_posts_card,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = list[position]
//        holder.likedByProfile.setImageResource(currentItem.likedByProfile)
//        holder.likedByUser.text = currentItem.likedByUser
//        holder.postProfile.setImageResource(currentItem.postProfile)

        for (x in currentItem.media){
            Glide.with(context).load(x.post).into(holder.postImage)
            Log.e("error",x.post)
        }

//        holder.postUserName.text = currentItem.postUserName
//        holder.postUserType.text = currentItem.postUserType
        holder.postUserD.text = currentItem.location
//        holder.postTime.text = currentItem.postTime
//        holder.userIdOnComment.text = currentItem.postUserOnComment
        holder.recentCommentBy.text = currentItem.caption

        Log.e("error",currentItem.caption)
        Log.e("error",currentItem.location)


        holder.likePost.setOnClickListener {
            Toast.makeText(context,"Liked", Toast.LENGTH_SHORT).show()
        }

        holder.sharePost.setOnClickListener {
            Toast.makeText(context,"Shared", Toast.LENGTH_SHORT).show()
        }

        holder.commentPost.setOnClickListener {
            Toast.makeText(context,"Comment", Toast.LENGTH_SHORT).show()
        }

//        if (holder.postTime.toString() == "3Hr"){
//            holder.likedByLayout.visibility = GONE
//        }
    }

}