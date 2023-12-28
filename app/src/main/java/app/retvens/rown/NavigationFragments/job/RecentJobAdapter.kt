package app.retvens.rown.NavigationFragments.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.FatchAllJobData
import app.retvens.rown.DataCollections.JobsCollection.Job
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentJobAdapter(val context: Context, var jobsList: FatchAllJobData) : RecyclerView.Adapter<RecentJobAdapter.RecentJobViewHolder>() {

    class RecentJobViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
        val salary = itemView.findViewById<TextView>(R.id.salary)
        val save_recent = itemView.findViewById<ImageView>(R.id.save_recent)
        val companyProfile = itemView.findViewById<ImageView>(R.id.profile_recent_job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_recent_job, parent, false)
        return RecentJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobsList.jobs.size
    }

    override fun onBindViewHolder(holder: RecentJobViewHolder, position: Int) {

        val data = jobsList.jobs[position]

        var operation: String

        holder.designation.text = data.jobTitle
        holder.location.text = data.jobLocation
        holder.type.text = data.jobType
        holder.title.text = "Remote"
        holder.salary.text = data.expectedCTC

        operation = if (data.saved != "not saved"){
            holder.save_recent.setImageResource(R.drawable.svg_saved_green)
            "pop"
        }else{
            holder.save_recent.setImageResource(R.drawable.svg_jobs_explore)
            "push"
        }

        Glide.with(context).load(data.companyImage).placeholder(R.drawable.png_blog).into(holder.companyProfile)

        holder.save_recent.setOnClickListener {
            operation = if (operation == "push"){
                saveJob(data.jobId, "push")
                holder.save_recent.setImageResource(R.drawable.svg_saved_green)
                "pop"
            } else {
                saveJob(data.jobId, "pop")
                holder.save_recent.setImageResource(R.drawable.svg_jobs_explore)
                "push"
            }
        }

        holder.itemView.setOnClickListener{
            val intent= Intent (context,JobDetailsActivity::class.java)
            intent.putExtra("jobID",data.jobId)
            intent.putExtra("userId",data.user_id)
            intent.putExtra("saved",data.saved)
            context.startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Job>) {
        jobsList = FatchAllJobData( newItems)
        notifyDataSetChanged()
    }

    private fun saveJob(jid: String, operation: String) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val save = RetrofitBuilder.jobsApis.saveJobs(user_id, SaveJob(operation, jid))
        save.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newItems: FatchAllJobData) {
//        jobsList = newItems
//        notifyDataSetChanged()
//    }
}