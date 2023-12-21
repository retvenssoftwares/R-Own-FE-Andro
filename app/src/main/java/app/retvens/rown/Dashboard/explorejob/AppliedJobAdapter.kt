package app.retvens.rown.Dashboard.explorejob

import AppliedJobs
import UserJobAppliedData
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.job.JobDetailsAppliedActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class AppliedJobAdapter(val context: Context, var appList : List<AppliedJobs>) : RecyclerView.Adapter<AppliedJobAdapter.AppliedViewHolder>() {

    class AppliedViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.applied_job_title)
        val location = itemView.findViewById<TextView>(R.id.applied_job_location)
        val type = itemView.findViewById<TextView>(R.id.applied_type)
        val workLocation = itemView.findViewById<TextView>(R.id.work_location)
        val status = itemView.findViewById<TextView>(R.id.application_status)
        val companyImage = itemView.findViewById<ShapeableImageView>(R.id.profile_applied_job)
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

        holder.designation.text = data.jobTitle
        holder.status.text = "Applied"
        holder.type.text = data.jobType
        holder.workLocation.text = "Remote"

        if (data.companyName.isNotEmpty() && data.jobLocation.isNotEmpty()){
            holder.location.text = "${data.companyName} • ${data.jobLocation}"
        }else{

            if (data.jobLocation.isNotEmpty())
                holder.location.text = data.jobLocation
            else
                holder.location.text = data.companyName
        }


        Glide.with(context).load(data.companyImage).placeholder(R.drawable.png_blog)
            .into(holder.companyImage)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, JobDetailsAppliedActivity::class.java)
            intent.putExtra("ownerUserId",data.user_id)
            intent.putExtra("jobId",data.jobId)
            intent.putExtra("status",data.status)
            context.startActivity(intent)
        }
    }

    fun filterList(filteredList: List<AppliedJobs>) {
        appList = filteredList
        notifyDataSetChanged()
    }

}