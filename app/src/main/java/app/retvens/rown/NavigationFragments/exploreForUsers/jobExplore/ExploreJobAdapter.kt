package app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore

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

class ExploreJobAdapter(val listS : List<JobPost>, val context: Context) : RecyclerView.Adapter<ExploreJobAdapter.ExploreJobViewHolder>() {

    class ExploreJobViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
        val salary = itemView.findViewById<TextView>(R.id.salary)

        val save_recent = itemView.findViewById<ImageView>(R.id.save_recent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_recent_job, parent, false)
        return ExploreJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreJobViewHolder, position: Int) {

        val data = listS[position]

        holder.designation.text = data.jobTitle
        holder.location.text = data.jobLocation
        holder.title.text = data.jobType
        holder.type.text = data.jobType

        holder.save_recent.visibility = View.GONE

    }
}