package app.retvens.rown.NavigationFragments.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.R

class RecentJobAdapterOwner(val context: Context, var jobsList:List<JobsData>) : RecyclerView.Adapter<RecentJobAdapterOwner.RecentJobViewHolder>() {

    class RecentJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
        val salary = itemView.findViewById<TextView>(R.id.salary)
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


        holder.designation.text = data.jobTitle
        holder.location.text = data.jobLocation
        holder.type.text = data.jobType
        holder.title.text = "Remote"

        holder.salary.text = data.expectedCTC
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<JobsData>) {
        jobsList = newItems
        notifyDataSetChanged()
    }
}