package app.retvens.rown.NavigationFragments.job

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.CreateCommunity.UploadIconAdapter
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.jobforvendors.JobsDetailsVendor
import app.retvens.rown.R

class JobsPostedAdapater(val context: Context, val jobList:List<JobsData>):RecyclerView.Adapter<JobsPostedAdapater.JobsPostedApdater>() {
    class JobsPostedApdater(itemView:View):ViewHolder(itemView) {

        val designation = itemView.findViewById<TextView>(R.id.view_job_designation)
        val location = itemView.findViewById<TextView>(R.id.view_job_location)
        val title = itemView.findViewById<TextView>(R.id.view_title)
        val type = itemView.findViewById<TextView>(R.id.view_type)
        val salary = itemView.findViewById<TextView>(R.id.view_salary)

        val button = itemView.findViewById<CardView>(R.id.btncard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsPostedApdater {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_userposted_job, parent, false)
        return JobsPostedApdater(view)
    }

    override fun onBindViewHolder(holder: JobsPostedApdater, position: Int) {

        val jobs = jobList[position]


        holder.designation.text = jobs.designationType
        holder.location.text = jobs.jobLocation
        holder.type.text = jobs.jobType
        holder.title.text = "Remote"
        holder.salary.text = jobs.expectedCTC



        holder.button.setOnClickListener {
           val intent = Intent(context,JobsDetailsVendor::class.java)
            intent.putExtra("jid",jobs.jid)
            intent.putExtra("title",jobs.jobTitle)
            intent.putExtra("company",jobs.companyName)
            intent.putExtra("location",jobs.jobLocation)
            intent.putExtra("type",jobs.jobType)
            intent.putExtra("description",jobs.jobDescription)
            intent.putExtra("skills",jobs.skillsRecq)
            intent.putExtra("salary",jobs.expectedCTC)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return jobList.size
    }
}