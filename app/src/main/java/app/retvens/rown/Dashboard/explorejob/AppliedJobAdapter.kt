package app.retvens.rown.Dashboard.explorejob

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.JobsCollection.AppliedJobData
import app.retvens.rown.NavigationFragments.job.JobDetailsAppliedActivity
import app.retvens.rown.R

class AppliedJobAdapter(val context: Context, val appList : AppliedJobData) : RecyclerView.Adapter<AppliedJobAdapter.AppliedViewHolder>() {

    class AppliedViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.applied_job_title)
        val location = itemView.findViewById<TextView>(R.id.applied_job_location)
        val type = itemView.findViewById<TextView>(R.id.applied_type)
        val title = itemView.findViewById<TextView>(R.id.appiled_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_applied_job, parent, false)
        return AppliedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: AppliedViewHolder, position: Int) {

        val data = appList

        holder.designation.text = data.designationType
        holder.type.text = data.jobType
        holder.location.text = data.jobLocation
        holder.title.text = data.jobTitle

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, JobDetailsAppliedActivity::class.java))
        }
    }

}