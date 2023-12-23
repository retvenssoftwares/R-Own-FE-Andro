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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.FatchAllJobData
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestedAllJobAdapter(val context: Context, var jobList: FatchAllJobData) : RecyclerView.Adapter<SuggestedAllJobAdapter.SuggestedJobViewHolder>() {

    interface JobSavedClickListener {
        fun onJobSavedClick(job: JobsData)
    }

    private var jobSavedClickListener: JobSavedClickListener? = null

    class SuggestedJobViewHolder(itemView: View) : ViewHolder(itemView){
        val position = itemView.findViewById<TextView>(R.id.suggested_job_title)
        val location = itemView.findViewById<TextView>(R.id.suggested_job_location)
        val type = itemView.findViewById<TextView>(R.id.workType)
        val title = itemView.findViewById<TextView>(R.id.jobTitle)
        val salary = itemView.findViewById<TextView>(R.id.salary)
        val button = itemView.findViewById<CardView>(R.id.apply_btn)
        val text = itemView.findViewById<TextView>(R.id.buttontext)
        val jobSaved = itemView.findViewById<ImageView>(R.id.save_suggested)
        val color = itemView.findViewById<ConstraintLayout>(R.id.view_all_suggested_items_cLayout)
        val companyProfile = itemView.findViewById<ImageView>(R.id.profile_suggested_job)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_all_suggested_job, parent, false)
        return SuggestedJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.jobs.size
    }

    @SuppressLint("SuspiciousIndentation", "ResourceAsColor")
    override fun onBindViewHolder(holder: SuggestedJobViewHolder, position: Int) {

        val jobs = jobList.jobs[position]

        var operation = "push"
        if(position % 2 !=0){
            holder.title.setBackgroundColor(ContextCompat.getColor(holder.title.context,R.color.text_color_black_white22))
            holder.type.setBackgroundColor(ContextCompat.getColor(holder.type.context,R.color.text_color_black_white22))
            holder.color.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.text_color_black_white2))
        }
        else{
            holder.title.setBackgroundColor(ContextCompat.getColor(holder.title.context,R.color.suggested_job_yellowB))
            holder.type.setBackgroundColor(ContextCompat.getColor(holder.type.context,R.color.suggested_job_yellowB))
            holder.color.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.suggested_job_yellow))

        }

        holder.position.text = jobs.jobTitle
        holder.type.text = jobs.jobType
        holder.title.text = "Remote"
        holder.salary.text = jobs.expectedCTC
        Glide.with(context).load(jobs.companyImage).placeholder(R.drawable.png_blog).into(holder.companyProfile)

        if (jobs.companyName?.isNotEmpty() == true && jobs.jobLocation.isNotEmpty()){
            holder.location.text = "${jobs.companyName} • ${jobs.jobLocation}"
        }else{
            if (jobs.jobLocation.isNotEmpty())
                holder.location.text = jobs.jobLocation
            else
                holder.location.text = jobs.companyName
        }

//        holder.jobSaved.setOnClickListener {
//            jobSavedClickListener?.onJobSavedClick(jobs)
//        }


        holder.jobSaved.setOnClickListener {
            if (operation == "push"){
                saveJob(jobs.jobId, "push")
                holder.jobSaved.setImageResource(R.drawable.svg_saved)
                operation = "pop"
            } else {
                saveJob(jobs.jobId, "pop")
                holder.jobSaved.setImageResource(R.drawable.svg_jobs_explore)
                operation = "push"
            }
        }

        holder.itemView.setOnClickListener{
            val intent= Intent (context,JobDetailsActivity::class.java)
            intent.putExtra("jobID",jobs.jobId)
            intent.putExtra("userId",jobs.user_id)
//            intent.putExtra("companyImageUri",jobs.companyImage)
            context.startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: FatchAllJobData) {
        jobList = newItems
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
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setJobSavedClickListener(listener: JobSavedClickListener) {
        jobSavedClickListener = listener
    }
}