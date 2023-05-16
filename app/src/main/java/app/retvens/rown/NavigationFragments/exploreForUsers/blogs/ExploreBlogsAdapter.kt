package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.R

class ExploreBlogsAdapter(val listS : List<ExploreBlogsData>, val context: Context) : RecyclerView.Adapter<ExploreBlogsAdapter.ExploreBlogsViewHolder>() {

    class ExploreBlogsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.blog_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreBlogsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_blogs_card, parent, false)
        return ExploreBlogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreBlogsViewHolder, position: Int) {
        holder.title.text = listS[position].title

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, BlogsDetailsActivity::class.java))
        }
    }
}