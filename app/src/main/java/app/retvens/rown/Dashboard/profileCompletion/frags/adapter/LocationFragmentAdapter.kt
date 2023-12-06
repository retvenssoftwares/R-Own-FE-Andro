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
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.R
import com.bumptech.glide.Glide

class LocationFragmentAdapter(val context: Context, var cityList:List<CountryData>): RecyclerView.Adapter<LocationFragmentAdapter.MyViewHolderClass>() {

    private var countryClickListener: OnLocationClickListener? = null

    interface OnLocationClickListener {
        fun onCountryClick(country: String, code:String)
    }

    fun setOnLocationClickListener(listener: OnLocationClickListener) {
        countryClickListener = listener
    }

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val location = itemview.findViewById<TextView>(R.id.location)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
            val data = cityList[position]

        holder.location.text = data.name
        holder.itemView.setOnClickListener {
            countryClickListener?.onCountryClick(data.name, data.numeric_code)
        }

    }
    fun searchLocation(searchText : List<CountryData>){
        cityList = searchText
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

}