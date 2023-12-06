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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.JobsCollection.ApplicantDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.job.JobsPostedAdapater
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class AppliedCandidateAdapter(val context:Context, var applicantData:List<ApplicantDataClass>):RecyclerView.Adapter<AppliedCandidateAdapter.AppliedCandidateAdapter>() {

    class AppliedCandidateAdapter(itemview:View):ViewHolder(itemview) {

        val profile = itemview.findViewById<ShapeableImageView>(R.id.employee_profile_explore)
        val name = itemview.findViewById<TextView>(R.id.employee_name_explore)
        val role = itemview.findViewById<TextView>(R.id.employee_role)
        val city = itemview.findViewById<TextView>(R.id.employee_city)
        val button = itemview.findViewById<CardView>(R.id.view_candidateDetail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCandidateAdapter {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_employes, parent, false)
        return AppliedCandidateAdapter(view)
    }

    override fun onBindViewHolder(holder: AppliedCandidateAdapter, position: Int) {

        val data = applicantData[position]

        holder.name.text = data.Full_name
        holder.city.text = "Kanpur"
        holder.role.text = data.jobTitle
        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.button.setOnClickListener {
            val intent = Intent(context,CandidateDetailsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("name",data.Full_name)
            intent.putExtra("city",data.Location)
            intent.putExtra("role",data.jobTitle)
            intent.putExtra("profile",data.Profile_pic)
            intent.putExtra("applicationId",data.applicationId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return applicantData.size
    }

}