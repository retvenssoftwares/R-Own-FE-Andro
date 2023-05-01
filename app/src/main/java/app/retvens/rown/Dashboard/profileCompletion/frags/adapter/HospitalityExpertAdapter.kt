package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.R

class HospitalityExpertAdapter(val context: Context, var companyList:List<CompanyDatacClass>): RecyclerView.Adapter<HospitalityExpertAdapter.MyViewHolderClass>() {

    private var jobClickListener: OnJobClickListener? = null

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.jobsname)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.getjoblist,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        val data = companyList[position]
        holder.name.text = data.company_name

        holder.itemView.setOnClickListener {
            jobClickListener?.onJobClick(companyList[position])
        }
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    interface OnJobClickListener {
        fun onJobClick(job: CompanyDatacClass)
    }

    fun setOnJobClickListener(listener: OnJobClickListener) {
        jobClickListener = listener
    }

}