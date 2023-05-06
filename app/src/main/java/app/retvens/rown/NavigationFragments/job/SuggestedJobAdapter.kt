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
import app.retvens.rown.R

class SuggestedJobAdapter(val context: Context, val jobList:List<JobsData>) : RecyclerView.Adapter<SuggestedJobAdapter.SuggestedJobViewHolder>() {

    class SuggestedJobViewHolder(itemView: View) : ViewHolder(itemView){
        val position = itemView.findViewById<TextView>(R.id.suggested_job_title)
        val location = itemView.findViewById<TextView>(R.id.suggested_job_location)
        val type = itemView.findViewById<TextView>(R.id.workType)
        val title = itemView.findViewById<TextView>(R.id.jobTitle)
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

        for (x in jobs.joblist){

            holder.position.text = x.designationType
            holder.location.text = x.jobLocation
            holder.type.text = x.jobType
            holder.title.text = x.jobTitle
        }

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, JobDetailsActivity::class.java))
        }
    }
}