package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.R
import com.bumptech.glide.Glide

class LocationFragmentAdapter(val context: Context, var cityList:List<LocationDataClass>): RecyclerView.Adapter<LocationFragmentAdapter.MyViewHolderClass>() {

    private var jobClickListener: OnLocationClickListener? = null

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val city = itemview.findViewById<TextView>(R.id.city)
        val state = itemview.findViewById<TextView>(R.id.state)
        val country = itemview.findViewById<TextView>(R.id.country)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
            val data = cityList[position]

        holder.country.text = data.name
        for (x in data.states){
            holder.state.text = x.name

            for (y in x.cities){
                holder.city.text = y.name
            }

        }


    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    interface OnLocationClickListener {
        fun onJobClick(job: LocationDataClass)
    }

    fun setOnJobClickListener(listener: OnLocationClickListener) {
        jobClickListener = listener
    }

}