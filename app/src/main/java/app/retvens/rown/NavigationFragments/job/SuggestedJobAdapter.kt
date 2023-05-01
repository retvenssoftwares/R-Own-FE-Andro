package app.retvens.rown.NavigationFragments.job

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
import app.retvens.rown.R

class SuggestedJobAdapter(val listS : MutableList<SuggestedJobData>, val context: Context) : RecyclerView.Adapter<SuggestedJobAdapter.SuggestedJobViewHolder>() {

    class SuggestedJobViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.suggested_job_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggested_job, parent, false)
        return SuggestedJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: SuggestedJobViewHolder, position: Int) {
        holder.title.text = listS[position].title

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, JobDetailsActivity::class.java))
        }
    }
}