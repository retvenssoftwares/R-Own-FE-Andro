package app.retvens.rown.viewAll.viewAllCommunities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ViewAllCommunityAdapter(var listS : ArrayList<GetCommunitiesData>, val context: Context) : RecyclerView.Adapter<ViewAllCommunityAdapter.ViewAllCommunityViewHolder>() {

    class ViewAllCommunityViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val joinGroup = itemView.findViewById<TextView>(R.id.connection_notification_accept)
        val viewGroup = itemView.findViewById<TextView>(R.id.connection_notification_decline)
        val users = itemView.findViewById<TextView>(R.id.users)
        val location = itemView.findViewById<TextView>(R.id.location_notification)
        val profile = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCommunityViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_notification_community, parent, false)
        return ViewAllCommunityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllCommunityViewHolder, position: Int) {
        holder.name.text = listS[position].group_name
        holder.joinGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        holder.joinGroup.setTextColor(ContextCompat.getColor(context, R.color.white))
        holder.joinGroup.text = "Join"

        holder.users.text = listS[position].Members.size.toString()
//        holder.joinGroup.setOnClickListener {
//            if (listS[position].join == "Join"){
//                context.startActivity(Intent(context, OpenCommunityDetailsActivity::class.java))
//            } else {
//                context.startActivity(Intent(context, ClosedCommunityDetailsActivity::class.java))
//            }
//        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)

        try {
            holder.location.text = listS[position].location
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }


        holder.viewGroup.setOnClickListener {
            val intent = Intent(context,OpenCommunityDetailsActivity::class.java)
            intent.putExtra("groupId",listS[position].group_id.toLong())
            context.startActivity(intent)
        }

    }

    fun searchView(searchText : ArrayList<GetCommunitiesData>){
        listS = searchText
        notifyDataSetChanged()
    }
}