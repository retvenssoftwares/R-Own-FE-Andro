package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R

class PopularFieldsAdapter(val listS : MutableList<SuggestedJobData>, val context: Context) : RecyclerView.Adapter<PopularFieldsAdapter.PopularViewHolder>() {

    class PopularViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.employee_name_item_popular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_popular_in_their_fields, parent, false)
        return PopularViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.title.text = listS[position].title

    }
}