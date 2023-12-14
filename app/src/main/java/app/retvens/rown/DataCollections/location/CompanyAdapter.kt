package app.retvens.rown.DataCollections.location

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.JobsCollection.HotelsList
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.R

class CompanyAdapter(val context: Context, var cityList:List<CompanyDatacClass>): RecyclerView.Adapter<CompanyAdapter.MyViewHolderClass>() {

    private var countryClickListener: OnLocationClickListener? = null

    fun setOnLocationClickListener(listener: OnLocationClickListener) {
        countryClickListener = listener
    }

    interface OnLocationClickListener {
        fun onStateDataClick(companyDatacClass: CompanyDatacClass)
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
        holder.location.text = data.company_name


        holder.itemView.setOnClickListener {
            countryClickListener?.onStateDataClick(cityList[position])
        }

    }
    fun searchLocation(searchText : List<StateData>){

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cityList.size
    }



    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<CompanyDatacClass>) {
        cityList = newItems
        notifyDataSetChanged()
    }
}