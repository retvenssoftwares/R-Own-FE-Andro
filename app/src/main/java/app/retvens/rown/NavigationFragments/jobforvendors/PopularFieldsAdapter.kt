package app.retvens.rown.NavigationFragments.jobforvendors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.DataCollections.JobsCollection.GetRequestedJobDara
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class PopularFieldsAdapter(val context: Context, val requestData:List<GetRequestedJobDara>) : RecyclerView.Adapter<PopularFieldsAdapter.PopularViewHolder>() {

    class PopularViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.employee_name_item_popular)
        val department = itemView.findViewById<TextView>(R.id.employee_front_item_popular)
        val designation = itemView.findViewById<TextView>(R.id.employee_front_desk_item_popular)
        val preffredLocation = itemView.findViewById<TextView>(R.id.employee_indore_item_popular)
        val noticePeriod = itemView.findViewById<TextView>(R.id.employee_month_item_popular)
        val ctc = itemView.findViewById<TextView>(R.id.employee_salary_item_popular)
        val title = itemView.findViewById<TextView>(R.id.employee_role_item_popular)
        val type = itemView.findViewById<TextView>(R.id.employee_remote_item_popular)
        val location = itemView.findViewById<TextView>(R.id.employee_location_item_popular)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.popular_employees_profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_popular_in_their_fields, parent, false)
        return PopularViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requestData.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val data = requestData[position]

        holder.name.text = data.Full_name
        holder.department.text = data.department
        holder.designation.text = data.designationType
        holder.preffredLocation.text = data.preferredLocation
        holder.noticePeriod.text = data.noticePeriod
        holder.ctc.text = data.expectedCTC
        holder.title.text = data.jobTitle.get(0)
        holder.type.text = data.jobType.get(0)
        holder.location.text = data.Location

        Glide.with(context).load(data.profile_pic).into(holder.profile)
    }
}