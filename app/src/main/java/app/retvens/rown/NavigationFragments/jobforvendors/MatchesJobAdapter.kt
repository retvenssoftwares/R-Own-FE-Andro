package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.GetRequestedJobDara
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class MatchesJobAdapter(val context: Context, var requestJob:List<GetRequestedJobDara>) : RecyclerView.Adapter<MatchesJobAdapter.MatchesViewHolder>() {

    class MatchesViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.employee_name_explore)
        val role = itemView.findViewById<TextView>(R.id.employee_role)
        val location = itemView.findViewById<TextView>(R.id.employee_city)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.employee_profile_explore)
        val view = itemView.findViewById<CardView>(R.id.view_candidateDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_employes, parent, false)
        return MatchesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requestJob.size
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {

        val data = requestJob[position]
        holder.name.text = data.Full_name
       holder.role.text = data.jobTitle
        holder.location.text = data.Location
        Glide.with(context)
            .load(data.profile_pic)
            .placeholder(R.drawable.svg_user)
            .into(holder.profile)

        holder.view.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra("userId",data.userId)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<GetRequestedJobDara>) {
        requestJob = newItems
        notifyDataSetChanged()
    }
}