package app.retvens.rown.Dashboard.explorejob

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.JobsCollection.AppliedJobData
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.job.JobDetailsActivity
import app.retvens.rown.NavigationFragments.job.JobDetailsAppliedActivity
import app.retvens.rown.R

class AppliedJobAdapter(val context: Context, var appList : List<AppliedJobData>) : RecyclerView.Adapter<AppliedJobAdapter.AppliedViewHolder>() {

    class AppliedViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.applied_job_title)
        val location = itemView.findViewById<TextView>(R.id.applied_job_location)
        val type = itemView.findViewById<TextView>(R.id.applied_type)
        val title = itemView.findViewById<TextView>(R.id.appiled_title)
        val status = itemView.findViewById<TextView>(R.id.application_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_applied_job, parent, false)
        return AppliedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: AppliedViewHolder, position: Int) {

        val data = appList[position]



        holder.designation.text = data.jobData.jobTitle
        holder.type.text = data.jobData.jobType
        holder.location.text = "${data.jobData.companyName} ${data.jobData.jobLocation}"
        holder.title.text = "Remote"

        holder.status.text = data.status

        holder.itemView.setOnClickListener{
            val intent = Intent(context, JobDetailsAppliedActivity::class.java)
            intent.putExtra("title",data.jobData.jobTitle)
            intent.putExtra("company",data.jobData.companyName)
            intent.putExtra("location",data.jobData.jobLocation)
            intent.putExtra("type",data.jobData.jobType)
            intent.putExtra("worktype",data.jobData.workplaceType)
            intent.putExtra("description",data.jobData.jobDescription)
            intent.putExtra("skill",data.jobData.skillsRecq)
            intent.putExtra("jobId",data.jid)
            intent.putExtra("userId",data.user_id)
            intent.putExtra("AppId",data.applicationId)
            context.startActivity(intent)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<AppliedJobData>) {
        appList = newItems
        notifyDataSetChanged()
    }
}