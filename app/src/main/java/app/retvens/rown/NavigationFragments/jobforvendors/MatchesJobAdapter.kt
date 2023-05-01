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

class MatchesJobAdapter(val listS : MutableList<SuggestedJobData>, val context: Context) : RecyclerView.Adapter<MatchesJobAdapter.MatchesViewHolder>() {

    class MatchesViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.employee_name_explore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_employes, parent, false)
        return MatchesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.title.text = listS[position].title

    }
}