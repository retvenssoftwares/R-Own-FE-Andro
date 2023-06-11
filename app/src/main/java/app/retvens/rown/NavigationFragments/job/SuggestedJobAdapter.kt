package app.retvens.rown.NavigationFragments.job

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.R
import com.mackhartley.roundedprogressbar.ext.setDrawableTint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestedJobAdapter(val context: Context, var jobList:List<JobsData>) : RecyclerView.Adapter<SuggestedJobAdapter.SuggestedJobViewHolder>() {

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
        val color = itemView.findViewById<ConstraintLayout>(R.id.constraintback)
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

        var operation = "push"

        val backgroundColor = if (position % 2 == 0) {
            ContextCompat.getColor(holder.itemView.context, R.color.suggested_job_black)

        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.suggested_job_yellow)


        }

        holder.color.setBackgroundColor(backgroundColor)


            holder.position.text = jobs.designationType
            holder.location.text = jobs.companyName
            holder.type.text = jobs.jobType
            holder.title.text = "Remote"

//        holder.jobSaved.setOnClickListener {
//            jobSavedClickListener?.onJobSavedClick(jobs)
//        }

        holder.salary.text = jobs.expectedCTC

        holder.jobSaved.setOnClickListener {
            if (operation == "push"){
                saveJob(jobs.jid, "push")
                holder.jobSaved.setImageResource(R.drawable.svg_saved)
                operation = "pop"
            } else {
                saveJob(jobs.jid, "pop")
                holder.jobSaved.setImageResource(R.drawable.svg_jobs_explore)
                operation = "push"
            }
        }

        if (jobs.applyStatus == "Applied"){
            holder.text.setBackgroundColor(Color.parseColor("#AFAFAF"))
            holder.text.text = "Applied"
        }


            holder.button.setOnClickListener{

                if (jobs.applyStatus == "Not Applied"){
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



    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<JobsData>) {
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