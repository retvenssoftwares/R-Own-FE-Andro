package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.R

class BasicInformationAdapter(val context: Context, var jobList:List<GetJobDataClass>): RecyclerView.Adapter<BasicInformationAdapter.MyViewHolderClass>() {

    private var jobClickListener: OnJobClickListener? = null

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.jobsname)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.getjoblist,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
       val data = jobList[position]
        holder.name.text = data.job_title

        holder.itemView.setOnClickListener {
            jobClickListener?.onJobClick(jobList[position])
        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    interface OnJobClickListener {
        fun onJobClick(job: GetJobDataClass)
    }

    fun setOnJobClickListener(listener: OnJobClickListener) {
        jobClickListener = listener
    }

}