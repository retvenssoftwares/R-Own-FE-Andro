package app.retvens.rown.NavigationFragments.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.R

class SuggestedJobAdapter(val context: Context, var jobList:List<JobsData>) : RecyclerView.Adapter<SuggestedJobAdapter.SuggestedJobViewHolder>() {

    class SuggestedJobViewHolder(itemView: View) : ViewHolder(itemView){
        val position = itemView.findViewById<TextView>(R.id.suggested_job_title)
        val location = itemView.findViewById<TextView>(R.id.suggested_job_location)
        val type = itemView.findViewById<TextView>(R.id.workType)
        val title = itemView.findViewById<TextView>(R.id.jobTitle)
        val salary = itemView.findViewById<TextView>(R.id.salary)
        val button = itemView.findViewById<CardView>(R.id.apply_btn)
        val text = itemView.findViewById<TextView>(R.id.buttontext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggested_job, parent, false)
        return SuggestedJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: SuggestedJobViewHolder, position: Int) {

        val jobs = jobList[position]


            holder.position.text = jobs.jobTitle
            holder.location.text = jobs.jobLocation
            holder.type.text = jobs.jobType
            holder.title.text = "Remote"


        holder.salary.text = jobs.expectedCTC


            holder.button.setOnClickListener{
                val intent = Intent(context,JobDetailsActivity::class.java)
                intent.putExtra("title",jobs.jobTitle)
                intent.putExtra("company",jobs.companyName)
                intent.putExtra("location",jobs.jobLocation)
                intent.putExtra("type",jobs.jobType)
                intent.putExtra("worktype",jobs.workplaceType)
                intent.putExtra("description",jobs.jobDescription)
                intent.putExtra("skill",jobs.skillsRecq)
                intent.putExtra("jobId",jobs.jid)
                intent.putExtra("userId",jobs.user_id)
                context.startActivity(intent)
            }



    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<JobsData>) {
        jobList = newItems
        notifyDataSetChanged()
    }
}