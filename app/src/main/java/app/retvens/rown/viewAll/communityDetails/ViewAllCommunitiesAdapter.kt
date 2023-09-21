package app.retvens.rown.viewAll.communityDetails

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
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllCommunities.MembersCommunityDetailsActivity
import com.bumptech.glide.Glide

class ViewAllCommunitiesAdapter (val context : Context, var list: List<GetCommunitiesData>) : RecyclerView.Adapter<ViewAllCommunitiesAdapter.ViewAllCommunitiesAdapterViewHolder>() {

        class ViewAllCommunitiesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val cumm_pic = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
            val title = itemView.findViewById<TextView>(R.id.personal_notification_name)
            val location = itemView.findViewById<TextView>(R.id.location_notification)
            val chat = itemView.findViewById<CardView>(R.id.ca_accept)
            val view = itemView.findViewById<CardView>(R.id.ca_decline)
            val user = itemView.findViewById<TextView>(R.id.users)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCommunitiesAdapterViewHolder {
            val inflater : LayoutInflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_notification_community,parent,false)
            return ViewAllCommunitiesAdapterViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewAllCommunitiesAdapterViewHolder, position: Int) {
            val currentItem = list[position]
            Glide.with(context).load(currentItem.Profile_pic).into(holder.cumm_pic)
            holder.title.text = currentItem.group_name

            val sharedPreferences1 =context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences1?.getString("user_id", "").toString()
            var isAdmin = "false"

            currentItem.Admin.forEach {
                if (user_id == it.user_id){
                    isAdmin = "true"
                }else{
                    Log.e("error","not")
                }
            }

            holder.location.text = list[position].location
            holder.user.text = "${currentItem.Members.size+1} members"
            holder.chat.setOnClickListener {
                val intent = Intent(context,MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.GROUP_ID,currentItem.group_id.toLong())
                intent.putExtra("admin",isAdmin)
                context.startActivity(intent)
            }

            holder.view.setOnClickListener {
                if (isAdmin == "true") {
                    val intent = Intent(context, CommunityDetailsActivity::class.java)
                    intent.putExtra("groupId", currentItem.group_id.toLong())
                    context.startActivity(intent)
                }else{
                    val intent = Intent(context, MembersCommunityDetailsActivity::class.java)
                    intent.putExtra("groupId", currentItem.group_id.toLong())
                    context.startActivity(intent)
                }
            }

        }

    fun searchCom(searchText : List<GetCommunitiesData>){
        list = searchText
        notifyDataSetChanged()
    }
}