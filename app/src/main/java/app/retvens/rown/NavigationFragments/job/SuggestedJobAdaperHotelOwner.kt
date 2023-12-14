package app.retvens.rown.NavigationFragments.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.R

class SuggestedJobAdaperHotelOwner(val context: Context, var jobList:List<UserJob>) : RecyclerView.Adapter<SuggestedJobAdaperHotelOwner.SuggestedJobViewHolder>() {

    class SuggestedJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val position = itemView.findViewById<TextView>(R.id.suggested_job_title)
        val location = itemView.findViewById<TextView>(R.id.suggested_job_location)
        val type = itemView.findViewById<TextView>(R.id.workType)
        val title = itemView.findViewById<TextView>(R.id.jobTitle)
        val salary = itemView.findViewById<TextView>(R.id.salary)
        val button = itemView.findViewById<CardView>(R.id.apply_btn)
        val color = itemView.findViewById<ConstraintLayout>(R.id.constraintback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_posted_job, parent, false)
        return SuggestedJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SuggestedJobViewHolder, position: Int) {

        val jobs = jobList[position]

        val backgroundColor = if (position % 2 == 0) {
            ContextCompat.getColor(holder.itemView.context, R.color.suggested_job_black)

        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.suggested_job_yellow)


        }

        holder.color.setBackgroundColor(backgroundColor)


        holder.position.text = jobs.jobTitle
        holder.location.text = jobs.jobLocation
        holder.type.text = jobs.jobType
        holder.title.text = "Remote"

        holder.salary.text = jobs.expectedCTC

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<UserJob>) {
        jobList = newItems
        notifyDataSetChanged()
    }
}