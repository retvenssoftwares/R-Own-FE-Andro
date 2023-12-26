package app.retvens.rown.NavigationFragments.job.savedJobs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.NavigationFragments.job.JobDetailsActivity
import app.retvens.rown.NavigationFragments.jobforvendors.JobsDetailsVendor
import app.retvens.rown.R
import com.bumptech.glide.Glide

class SavedJobsAdapter(val listS : List<SavedJob>, val context: Context) : RecyclerView.Adapter<SavedJobsAdapter.PollsViewHolder>() {

    class PollsViewHolder(itemView: View) : ViewHolder(itemView){
        val designation = itemView.findViewById<TextView>(R.id.view_job_designation)
        val location = itemView.findViewById<TextView>(R.id.view_job_location)
        val title = itemView.findViewById<TextView>(R.id.view_title)
        val type = itemView.findViewById<TextView>(R.id.view_type)
        val salary = itemView.findViewById<TextView>(R.id.view_salary)

        val hotelLogo = itemView.findViewById<ImageView>(R.id.profile_recent_job)

        val button = itemView.findViewById<CardView>(R.id.btncard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_userposted_job, parent, false)
        return PollsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: PollsViewHolder, position: Int) {
        val jobs = listS[position]


        holder.designation.text = jobs.jobTitle
        holder.location.text = jobs.jobLocation
        holder.type.text = jobs.jobType
        holder.title.text = "Remote"
        holder.salary.text = jobs.expectedCTC

        Glide.with(context).load(jobs.companyImage).placeholder(R.drawable.png_blog).into(holder.hotelLogo)


        holder.button.setOnClickListener {
            val intent = Intent(context, JobDetailsActivity::class.java)
            intent.putExtra("jobID",jobs.jobId)
            intent.putExtra("title",jobs.jobTitle)
            intent.putExtra("company",jobs.companyName)
            intent.putExtra("location",jobs.jobLocation)
            intent.putExtra("type",jobs.jobType)
            intent.putExtra("description",jobs.jobDescription)
            intent.putExtra("salary",jobs.expectedCTC)
            context.startActivity(intent)
        }

    }
}