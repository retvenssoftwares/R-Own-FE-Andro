package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.FeedCollection.CommentDataClass
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CommentAdapter(val context: Context, var commentList:List<GetComments>): RecyclerView.Adapter<CommentAdapter.MyViewHolderClass3>() {



    class MyViewHolderClass3(itemview: View): RecyclerView.ViewHolder(itemview){
        val name = itemview.findViewById<TextView>(R.id.commented_username)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.comment_profile)
        val comment = itemview.findViewById<TextView>(R.id.commentOfUser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass3 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commentslist,parent,false)
        return CommentAdapter.MyViewHolderClass3(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass3, position: Int) {

        val data = commentList[position]

        holder.comment.text = data.comment
        holder.name.text = data.name

        Glide.with(context).load(data.profile).into(holder.profile)

    }

    override fun getItemCount(): Int {
        return commentList.size
    }


}
