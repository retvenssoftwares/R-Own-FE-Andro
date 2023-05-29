package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.FeedCollection.CommentDataClass
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CommentAdapter(val context: Context, var commentList:GetComments): RecyclerView.Adapter<CommentAdapter.MyViewHolderClass3>() {



    class MyViewHolderClass3(itemview: View): RecyclerView.ViewHolder(itemview){
        val name = itemview.findViewById<TextView>(R.id.commented_username)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.comment_profile)
        val comment = itemview.findViewById<TextView>(R.id.commentOfUser)
        val reply = itemview.findViewById<CardView>(R.id.replyComment)
        val relies = itemview.findViewById<TextView>(R.id.replies_OnComment)
        val time = itemview.findViewById<TextView>(R.id.times_OnComment)

        val recycler = itemview.findViewById<RecyclerView>(R.id.nestedCommentRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass3 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commentslist,parent,false)
        return CommentAdapter.MyViewHolderClass3(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass3, position: Int) {



        val data = commentList.post.comments[position]


        holder.name.text = data.User_name
        holder.comment.text = data.comment
        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.relies.setOnClickListener {
            holder.recycler.visibility = View.VISIBLE
        }

        holder.recycler.layoutManager = LinearLayoutManager(context)
        val nestedAdapter = NestedCommentAdapter(context,commentList)
        holder.recycler.adapter = nestedAdapter
    }

    override fun getItemCount(): Int {
        return commentList.commentCount
    }


}
