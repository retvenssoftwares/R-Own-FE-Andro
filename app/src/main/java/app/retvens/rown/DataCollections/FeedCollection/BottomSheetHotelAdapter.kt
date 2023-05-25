package app.retvens.rown.DataCollections.FeedCollection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.R

class BottomSheetHotelAdapter(val context: Context,val mList:List<GetHotelDataClass>):RecyclerView.Adapter<BottomSheetHotelAdapter.HotelViewHolder> (){

    class HotelViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.jobsname)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.getjoblist,parent,false)
        return BottomSheetHotelAdapter.HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {

        val data = mList[position]
        holder.name.text = data.hotelName

    }

    override fun getItemCount(): Int {
        return mList.size
    }

}