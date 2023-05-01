package app.retvens.rown.Dashboard.explorejob

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.job.JobDetailsAppliedActivity
import app.retvens.rown.R

class AppliedJobAdapter(val context: Context, val al : MutableList<Applied>) : RecyclerView.Adapter<AppliedJobAdapter.AppliedViewHolder>() {

    class AppliedViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.applied_job_title)
//        val status = itemView.findViewById<TextView>(R.id.applied_job_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_applied_job, parent, false)
        return AppliedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return al.size
    }

    override fun onBindViewHolder(holder: AppliedViewHolder, position: Int) {
        holder.title.text = al[position].title

        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, JobDetailsAppliedActivity::class.java))
        }
    }

}