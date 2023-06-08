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
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.FilterDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.JobData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentJobAdapter(val context: Context, var jobsList:List<JobsData>) : RecyclerView.Adapter<RecentJobAdapter.RecentJobViewHolder>() {

    class RecentJobViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val location = itemView.findViewById<TextView>(R.id.recent_job_location)
        val title = itemView.findViewById<TextView>(R.id.jobs_title)
        val type = itemView.findViewById<TextView>(R.id.jobs_type)
        val salary = itemView.findViewById<TextView>(R.id.salary)

        val save_recent = itemView.findViewById<ImageView>(R.id.save_recent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_recent_job, parent, false)
        return RecentJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    override fun onBindViewHolder(holder: RecentJobViewHolder, position: Int) {

        val data = jobsList[position]

        var operation = "push"

        holder.designation.text = data.jobTitle
        holder.location.text = data.companyName
        holder.type.text = data.jobType
        holder.title.text = "Remote"
        holder.salary.text = data.expectedCTC

        holder.save_recent.setOnClickListener {
            if (operation == "push"){
                saveJob(data.jid, "push")
                holder.save_recent.setImageResource(R.drawable.svg_saved)
                operation = "pop"
                holder.save_recent.setImageResource(R.drawable.vector_saved)
            } else {
                saveJob(data.jid, "pop")
                holder.save_recent.setImageResource(R.drawable.svg_jobs_explore)
                operation = "push"
            }
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context,JobDetailsActivity::class.java)
            intent.putExtra("title",data.jobTitle)
            intent.putExtra("company",data.companyName)
            intent.putExtra("location",data.jobLocation)
            intent.putExtra("type",data.jobType)
            intent.putExtra("worktype",data.workplaceType)
            intent.putExtra("description",data.jobDescription)
            intent.putExtra("skill",data.skillsRecq)
            intent.putExtra("jobId",data.jid)
            intent.putExtra("userId",data.user_id)
            intent.putExtra("applyStatus",data.applyStatus)
            intent.putExtra("appliedStatus",data.display_status)
            context.startActivity(intent)
        }

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
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<JobsData>) {
        jobsList = newItems
        notifyDataSetChanged()
    }
}