package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

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
        return 5
    }

    override fun onBindViewHolder(holder: CommuListViewHolder, position: Int) {
        val currentItem = list[position]
        holder.cumm_pic.setImageResource(currentItem.image)
        holder.title.text = currentItem.title
        holder.member.text = currentItem.members
    }
}