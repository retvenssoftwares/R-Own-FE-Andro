package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.VendorServicesData
import app.retvens.rown.R

class VendorServicesAdapter(val context: Context, var serviceList:List<VendorServicesData>): RecyclerView.Adapter<VendorServicesAdapter.MyViewHolderClass>() {

    private var jobClickListener: OnJobClickListener? = null

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.servicesName)
        val image = itemview.findViewById<ImageView>(R.id.select)
    }


    interface OnJobClickListener {
        fun onJobClick(job: VendorServicesData)
    }

    fun setOnJobClickListener(listener: OnJobClickListener) {
        jobClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listofservices,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        val data = serviceList[position]
        holder.name.text = data.service_name

        holder.itemView.setOnClickListener {
            jobClickListener?.onJobClick(data)

            data.isSelected = !data.isSelected

            if (data.isSelected) {
                holder.image.setImageResource(R.drawable.checksquare)
            } else {
                holder.image.setImageResource(R.drawable.square)
            }

        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<VendorServicesData>) {
        serviceList = newItems
        notifyDataSetChanged()
    }

}