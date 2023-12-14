package app.retvens.rown.NavigationFragments.profile.jobs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.MessagingModule.UserData
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.NavigationFragments.job.GetJobData
import app.retvens.rown.NavigationFragments.job.UserJob
import app.retvens.rown.R

class ProfileJobAdapter(val context: Context, var jobsList:List<UserJob>) : RecyclerView.Adapter<ProfileJobAdapter.ExploreJobViewHolder>() {

    class ExploreJobViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
        val salary = itemView.findViewById<TextView>(R.id.salary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_profile_job, parent, false)
        return ExploreJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    override fun onBindViewHolder(holder: ExploreJobViewHolder, position: Int) {
        val data = jobsList[position]


        holder.designation.text = data.jobTitle
        holder.location.text = data.jobLocation
        holder.type.text = data.jobType
        holder.title.text = "Remote"

        holder.salary.text = data.expectedCTC
    }
}