package app.retvens.rown.DataCollections.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.JobsCollection.HotelsDataClass
import app.retvens.rown.DataCollections.JobsCollection.HotelsList
import app.retvens.rown.DataCollections.JobsCollection.HotelsListItem
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.R
import com.bumptech.glide.Glide

class HotelsAdapter(val context: Context, var cityList:List<HotelsList>): RecyclerView.Adapter<HotelsAdapter.MyViewHolderClass>() {

    private var countryClickListener: OnLocationClickListener? = null

    fun setOnLocationClickListener(listener: OnLocationClickListener) {
        countryClickListener = listener
    }

    interface OnLocationClickListener {
        fun onStateDataClick(hotelsList: HotelsList)
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
//
    holder.location.text = data.companyName

        holder.itemView.setOnClickListener {
            countryClickListener?.onStateDataClick(data)
        }

    }
    fun searchLocation(searchText : List<StateData>){
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<HotelsList>) {
        cityList = newItems
        notifyDataSetChanged()
    }

}