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

    private var countryClickListener: OnLocationClickListener? = null

    interface OnLocationClickListener {
        fun onCountryClick(country: String)
    }

    fun setOnLocationClickListener(listener: OnLocationClickListener) {
        countryClickListener = listener
    }

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val location = itemview.findViewById<TextView>(R.id.location)
//        val state = itemview.findViewById<TextView>(R.id.state)
//        val country = itemview.findViewById<TextView>(R.id.country)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
            val data = cityList[position]

        holder.location.text = data.name
        holder.itemView.setOnClickListener {
            countryClickListener?.onCountryClick(data.name)
        }

/*        holder.country.text = data.name
        for (x in data.states){
            holder.state.text = x.name

            for (y in x.cities){
                holder.city.text = y.name
            }

        }*/


    }

    override fun getItemCount(): Int {
        return cityList.size
    }

}