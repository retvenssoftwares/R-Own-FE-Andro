package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.createPosts.CreateTextPost
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CreatePostAdapter(val context: Context,private val viewType: Int) : RecyclerView.Adapter<CreatePostAdapter.ExploreJobViewHolder>() {

    class ExploreJobViewHolder(itemView: View) : ViewHolder(itemView){

        val createPost =  itemView.findViewById<RelativeLayout>(R.id.relative_create)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.home_profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.static_post_layout, parent, false)
        return ExploreJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ExploreJobViewHolder, position: Int) {

        holder.createPost.setOnClickListener {
            val intent = Intent(context,CreateTextPost::class.java)
            context.startActivity(intent)
        }

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()
        if (profilePic.isNotEmpty()) {
            Glide.with(context).load(profilePic).into(holder.profile)
        } else {
            if (profilePic.isNotEmpty()) {
                Glide.with(context).load(profilePic).into(holder.profile)
            } else {
                holder.profile.setImageResource(R.drawable.svg_user)
            }
        }

    }
}