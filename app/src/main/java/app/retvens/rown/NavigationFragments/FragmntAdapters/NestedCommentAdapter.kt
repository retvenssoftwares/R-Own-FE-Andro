package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView


class NestedCommentAdapter(val context: Context,private val nestedComments: GetComments) : RecyclerView.Adapter<NestedCommentAdapter.MyViewHolder>() {

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val name = itemview.findViewById<TextView>(R.id.commented_username)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.comment_profile)
        val comment = itemview.findViewById<TextView>(R.id.commentOfUser)
        val reply = itemview.findViewById<CardView>(R.id.replyComment)
        val relies = itemview.findViewById<TextView>(R.id.replies_OnComment)
        val time = itemview.findViewById<TextView>(R.id.times_OnComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commentslist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = nestedComments.post.comments[position]


        holder.name.text = data.User_name
        holder.comment.text = data.comment
        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.relies.visibility = View.GONE
        holder.reply.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return nestedComments.commentCount
    }
}