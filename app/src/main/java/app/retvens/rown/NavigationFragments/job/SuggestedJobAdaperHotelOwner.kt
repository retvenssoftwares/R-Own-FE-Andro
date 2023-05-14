package app.retvens.rown.NavigationFragments.job

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.R

class SuggestedJobAdaperHotelOwner(val context: Context, val jobList:List<JobsData>) : RecyclerView.Adapter<SuggestedJobAdaperHotelOwner.SuggestedJobViewHolder>() {

    class SuggestedJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val position = itemView.findViewById<TextView>(R.id.suggested_job_title)
        val location = itemView.findViewById<TextView>(R.id.suggested_job_location)
        val type = itemView.findViewById<TextView>(R.id.workType)
        val title = itemView.findViewById<TextView>(R.id.jobTitle)
        val salary = itemView.findViewById<TextView>(R.id.salary)
        val button = itemView.findViewById<CardView>(R.id.apply_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggested_job, parent, false)
        return SuggestedJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: SuggestedJobViewHolder, position: Int) {

        val jobs = jobList[position]


        holder.position.text = jobs.jobTitle
        holder.location.text = jobs.jobLocation
        holder.type.text = jobs.jobType
        holder.title.text = "Remote"

        val min = jobs.minSalary
        val max = jobs.maxSalary

        holder.salary.text = max


    }
}