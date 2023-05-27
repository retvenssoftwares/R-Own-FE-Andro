package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.R

class BottomVenueByLocationAdapter(val context: Context, var cityList:List<HotelsName>): RecyclerView.Adapter<BottomVenueByLocationAdapter.MyViewHolderClass>() {

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

        holder.location.text = data.hotelName
        holder.itemView.setOnClickListener {
            countryClickListener?.onCountryClick(data.hotelName, "")
        }

    }
    fun search(searchText : List<HotelsName>){
        cityList = searchText
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

}