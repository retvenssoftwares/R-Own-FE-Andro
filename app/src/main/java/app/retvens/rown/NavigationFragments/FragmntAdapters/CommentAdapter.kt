package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import app.retvens.rown.DataCollections.FeedCollection.Comments
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CommentAdapter(val context: Context, var commentList:GetComments): RecyclerView.Adapter<CommentAdapter.MyViewHolderClass3>() {

    private var onItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(dataItem: Comments)


    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

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

        if (data.User_name.isNotEmpty()){
            holder.name.text = data.User_name
        } else {
            holder.name.text = data.Full_name
        }
        holder.comment.text = data.comment
        Glide.with(context).load(data.Profile_pic).into(holder.profile)
        if (data.replies.isNotEmpty()){
            if (data.replies.size > 1) {
                holder.relies.text = data.replies.size.toString() + " Replies"
            } else {
                holder.relies.text = data.replies.size.toString() + " Reply"
            }
        } else {
            holder.relies.visibility = View.GONE
        }


        holder.profile.setOnClickListener {
            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.user_id)
                context.startActivity(intent)
            }
        }


        holder.relies.setOnClickListener {
            holder.recycler.visibility = View.VISIBLE
        }

        holder.recycler.layoutManager = LinearLayoutManager(context)
        val nestedAdapter = NestedCommentAdapter(context,data.replies)
        holder.recycler.adapter = nestedAdapter

        holder.reply.setOnClickListener {
            onItemClickListener?.onItemClick(data)
        }

        val timestamp = TimesStamp.convertTimeToText(data.date_added)
        holder.time.text = timestamp

    }

    override fun getItemCount(): Int {
        return commentList.commentCount
    }


}
