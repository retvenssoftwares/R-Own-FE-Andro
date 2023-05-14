package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.JobsCollection.GetApplicantDataClass
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class JobsDetailsVendorAdapter(val context: Context,val getApplicantData:List<GetApplicantDataClass>): RecyclerView.Adapter<JobsDetailsVendorAdapter.JobsDetailViewHolder>() {

    class JobsDetailViewHolder(item:View): ViewHolder(item) {

        val name = item.findViewById<TextView>(R.id.employee_name_explore)
        val profile = item.findViewById<ShapeableImageView>(R.id.employee_profile_explore)
        val title = item.findViewById<TextView>(R.id.employee_role)
        val experience = item.findViewById<TextView>(R.id.employee_experience)
        val location = item.findViewById<TextView>(R.id.employee_city)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsDetailViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_employes, parent, false)
        return JobsDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobsDetailViewHolder, position: Int) {

        val data = getApplicantData[position]

        holder.name.text = data.full_name

        Glide.with(context).load(data.profile_pic).into(holder.profile)
    }

    override fun getItemCount(): Int {
        return getApplicantData.size
    }


}