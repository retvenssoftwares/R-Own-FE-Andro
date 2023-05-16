package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

class ExploreHotelsAdapter(val listS : List<ExploreHotelsData>, val context: Context) : RecyclerView.Adapter<ExploreHotelsAdapter.ExploreHotelsViewHolder>() {

    class ExploreHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.venue_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreHotelsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_hotels_card, parent, false)
        return ExploreHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreHotelsViewHolder, position: Int) {
        holder.name.text = listS[position].title

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, HotelDetailsActivity::class.java))
        }
    }
}