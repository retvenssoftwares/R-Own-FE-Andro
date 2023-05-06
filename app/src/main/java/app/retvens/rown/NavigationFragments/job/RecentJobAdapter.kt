package app.retvens.rown.NavigationFragments.job

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.JobData
import app.retvens.rown.R

class RecentJobAdapter(val context: Context, val jobsList:List<JobsData>) : RecyclerView.Adapter<RecentJobAdapter.RecentJobViewHolder>() {

    class RecentJobViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_recent_job, parent, false)
        return RecentJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    override fun onBindViewHolder(holder: RecentJobViewHolder, position: Int) {

        val data = jobsList[position]

        for (x in data.joblist){

            holder.designation.text = x.designationType
            holder.location.text = x.jobLocation
            holder.type.text = x.jobType
            holder.title.text = x.jobTitle
        }

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, JobDetailsActivity::class.java))
        }

    }
}