package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import app.retvens.rown.viewAll.communityDetails.CommunityDetailsActivity
import com.bumptech.glide.Glide

class CommunityListAdapter(val context : Context, val list: ArrayList<Community>) : RecyclerView.Adapter<CommunityListAdapter.CommuListViewHolder>() {

    class CommuListViewHolder(itemView: View) : ViewHolder(itemView){
        val cumm_pic = itemView.findViewById<ImageView>(R.id.c_img)
        val title = itemView.findViewById<TextView>(R.id.title)
        val member = itemView.findViewById<TextView>(R.id.members)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommuListViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.create_cummunity_item,parent,false)
        return CommuListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommuListViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context).load(currentItem.image).into(holder.cumm_pic)
        holder.title.text = currentItem.title
        holder.member.text = currentItem.members

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CommunityDetailsActivity::class.java)
            intent.putExtra("image", currentItem.image)
            intent.putExtra("title", currentItem.title)
            context.startActivity(intent)
        }
    }
}