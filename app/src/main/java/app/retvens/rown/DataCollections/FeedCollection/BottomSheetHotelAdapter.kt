package app.retvens.rown.DataCollections.FeedCollection

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.createPosts.CheckInChildActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class BottomSheetHotelAdapter(val context: Context,val mList:List<GetHotelDataClass>,val location:String):RecyclerView.Adapter<BottomSheetHotelAdapter.HotelViewHolder> (){

    class HotelViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.hotel_name)
        val location = itemview.findViewById<TextView>(R.id.location)
        val hotelPic = itemview.findViewById<ShapeableImageView>(R.id.hotel_thubmnail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel,parent,false)
        return BottomSheetHotelAdapter.HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {

        val data = mList[position]
        Log.e("data",data.toString())
        holder.name.text = data.hotelName
        holder.location.text = location

        try {
            Glide.with(context).load(data.hotelLogoUrl).into(holder.hotelPic)
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,CheckInChildActivity::class.java)
            intent.putExtra("hotelPic",data.hotelLogoUrl)
            intent.putExtra("hotelName",data.hotelName)
            intent.putExtra("location",location)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}