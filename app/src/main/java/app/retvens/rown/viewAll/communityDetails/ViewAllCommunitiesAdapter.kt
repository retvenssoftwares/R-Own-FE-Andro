package app.retvens.rown.viewAll.communityDetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ViewAllCommunitiesAdapter (val context : Context, var list: ArrayList<Community>) : RecyclerView.Adapter<ViewAllCommunitiesAdapter.ViewAllCommunitiesAdapterViewHolder>() {

        class ViewAllCommunitiesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val cumm_pic = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
            val title = itemView.findViewById<TextView>(R.id.personal_notification_name)
            val location = itemView.findViewById<TextView>(R.id.location_notification)
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
            Glide.with(context).load(currentItem.image).into(holder.cumm_pic)
            holder.title.text = currentItem.title

            holder.itemView.setOnClickListener {
                val intent = Intent(context, CommunityDetailsActivity::class.java)
                intent.putExtra("image", currentItem.image)
                intent.putExtra("title", currentItem.title)
                context.startActivity(intent)
            }
        }

    fun searchCom(searchText : ArrayList<Community>){
        list = searchText
        notifyDataSetChanged()
    }
}