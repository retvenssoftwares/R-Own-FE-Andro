package app.retvens.rown.NavigationFragments.profile.jobs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.R

class ProfileJobAdapter(val listS : List<ExploreJobData>, val context: Context) : RecyclerView.Adapter<ProfileJobAdapter.ExploreJobViewHolder>() {

    class ExploreJobViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.recent_job_designation)
        val save_recent = itemView.findViewById<ImageView>(R.id.save_recent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreJobViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_profile_job, parent, false)
        return ExploreJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreJobViewHolder, position: Int) {
        holder.name.text = listS[position].title
        holder.save_recent.visibility = View.GONE
    }
}