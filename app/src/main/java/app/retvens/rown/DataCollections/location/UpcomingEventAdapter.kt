package app.retvens.rown.DataCollections.location

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.createPosts.CreatePostEventActivityChild
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class UpcomingEventAdapter(val context: Context, var eventList:List<UpcomingEventDataclass>): RecyclerView.Adapter<UpcomingEventAdapter.MyViewHolderClass>() {
    private var eventClickListener: OnLocationClickListener? = null

    interface OnLocationClickListener {
        fun onStateDataClick(name: String, image:String,eventId:String,date:String,location:String)
    }

    fun setOnLocationClickListener(listener: OnLocationClickListener) {
        eventClickListener = listener
    }

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val name = itemview.findViewById<TextView>(R.id.event_name)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.event_thubmnail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        val data = eventList[position]

        holder.name.text = data.event_title
        Glide.with(context).load(data.event_thumbnail).into(holder.profile)
//
//        val intent = Intent(context,CreatePostEventActivityChild::class.java)
//        intent.putExtra("name",data.event_title)
//        intent.putExtra("image",data.event_start_date)
//        intent.putExtra("id",data.event_id)
//        intent.putExtra("date",data.event_start_date)
//        context.startActivity(intent)

        holder.itemView.setOnClickListener {
            eventClickListener?.onStateDataClick(data.event_title,data.event_thumbnail,data.event_id,data.event_start_date,data.location)
        }


    }
    fun searchLocation(searchText : List<UpcomingEventDataclass>){
        eventList = searchText
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}