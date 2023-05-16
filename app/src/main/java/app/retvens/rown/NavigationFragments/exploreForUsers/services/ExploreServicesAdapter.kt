package app.retvens.rown.NavigationFragments.exploreForUsers.services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R

class ExploreServicesAdapter(val listS : List<ExploreServicesData>, val context: Context) : RecyclerView.Adapter<ExploreServicesAdapter.ExploreServicesViewHolder>() {

    class ExploreServicesViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.vendor_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreServicesViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_services_card, parent, false)
        return ExploreServicesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreServicesViewHolder, position: Int) {
        holder.name.text = listS[position].title

    }
}