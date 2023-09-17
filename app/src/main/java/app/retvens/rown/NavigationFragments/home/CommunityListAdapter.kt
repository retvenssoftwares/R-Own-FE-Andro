package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import app.retvens.rown.viewAll.communityDetails.ViewAllCommmunitiesActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class CommunityListAdapter(val context : Context,private val viewType: Int, val list: List<GetCommunitiesData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_STATIC_CARD = 0
    private val VIEW_TYPE_NORMAL = 1
    private val VIEW_TYPE_STATIC_Last = 2
    class StaticCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val addButton = itemView.findViewById<ImageView>(R.id.community_btn)
    }

    class LastCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lastCard = itemView.findViewById<CardView>(R.id.view_allCommunity)
    }

    class NormalCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views in the normal card layout
        val card = itemView.findViewById<CardView>(R.id.dataitem)
        val cImg: ShapeableImageView = itemView.findViewById(R.id.c_img)
        val title: TextView = itemView.findViewById(R.id.title)
        val members: TextView = itemView.findViewById(R.id.members)
        val joinG: TextView = itemView.findViewById(R.id.joinG)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if(viewType == VIEW_TYPE_STATIC_CARD){
            val staticFirstCard = inflater.inflate(R.layout.item_first_card,parent,false)
            return  StaticCardViewHolder(staticFirstCard)
        }else if(viewType == VIEW_TYPE_STATIC_Last){
            val staticlastCard = inflater.inflate(R.layout.item_last_card,parent,false)
            return  LastCardViewHolder(staticlastCard)
        } else{
            val view = inflater.inflate(R.layout.create_cummunity_item,parent,false)
            return NormalCardViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return  list.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is StaticCardViewHolder){


            holder.addButton.setOnClickListener {
                val intent = Intent(context,CreateCommunity::class.java)
                context.startActivity(intent)
            }


        }else if (holder is NormalCardViewHolder){
                val currentItem = list[position-1]
                Log.e("data",currentItem.toString())
                Glide.with(context).load(currentItem.Profile_pic).into(holder.cImg)
                holder.title.text = currentItem.group_name
                holder.members.text = "${currentItem.Members.size.toString()} members"


                val sharedPreferences1 =context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
                val user_id = sharedPreferences1?.getString("user_id", "").toString()
                var isAdmin = "false"

                currentItem.Admin.forEach {
                    if (user_id == it.User_id){
                        isAdmin = "true"
                    }else{
                        Log.e("error","not")
                    }

                    Log.e("user",user_id)
                    Log.e("user",it.User_id.toString())
                }




                holder.itemView.setOnClickListener {
                    Log.e("admin",isAdmin.toString())
                    val intent = Intent(context, MesiboMessagingActivity::class.java)
                    intent.putExtra("admin",isAdmin)
                    intent.putExtra(MesiboUI.GROUP_ID, currentItem.group_id.toLong())
                    context.startActivity(intent)
                }


        }else if (holder is LastCardViewHolder){
            Log.e("last","okkk")

            if (list.size in 3..4) {
                holder.lastCard.visibility = View.VISIBLE
                holder.lastCard.setOnClickListener {
                    context.startActivity(Intent(context, ViewAllCommmunitiesActivity::class.java))
                }
            } else {
                holder.lastCard.visibility = View.GONE
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        // Check if the list size is 3 or 4 and handle the position accordingly
        return if (list.size in 4..5) {
            when (position) {
                0 -> VIEW_TYPE_STATIC_CARD
                4 -> VIEW_TYPE_STATIC_Last
                else -> VIEW_TYPE_NORMAL
            }
        } else {
            when (position) {
                0 -> VIEW_TYPE_STATIC_CARD
                else -> VIEW_TYPE_NORMAL
            }
        }
    }
}