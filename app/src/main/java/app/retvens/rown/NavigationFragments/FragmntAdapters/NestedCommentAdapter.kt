package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.DataCollections.FeedCollection.Reply
import app.retvens.rown.NavigationFragments.ProfileFragment
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.job.JobExploreFragment
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView


class NestedCommentAdapter(val context: Context,private val nestedComments: List<Reply>) : RecyclerView.Adapter<NestedCommentAdapter.MyViewHolder>() {

    val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
    val user_id = sharedPreferences?.getString("user_id", "").toString()

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
        val data = nestedComments[position]

        if (data.User_name.isNotEmpty()){
            holder.name.text = data.User_name
        } else {
            holder.name.text = data.Full_name
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
                    intent.putExtra("userId", data.user_id)
                    context.startActivity(intent)
            }
        }

        holder.comment.text = data.comment
        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.relies.visibility = View.GONE
        holder.reply.visibility = View.GONE

        val time = TimesStamp.convertTimeToText(data.date_added)
        holder.time.text = time
    }

    override fun getItemCount(): Int {
        return nestedComments.size
    }
}