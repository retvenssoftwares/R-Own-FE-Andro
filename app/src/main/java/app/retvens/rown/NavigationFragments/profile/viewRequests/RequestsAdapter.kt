package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.Connection
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class RequestsAdapter(val listS : GetAllRequestDataClass, val context: Context) : RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {


    interface ConnectClickListener {
        fun onJobSavedClick(connect: Connection)
    }

    private var connectClickListener: ConnectClickListener? = null

    class RequestsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        val accept = itemView.findViewById<TextView>(R.id.suggetions_notification_connect)
        val reject = itemView.findViewById<TextView>(R.id.suggetions_notification_view)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.suggetions_notification_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggetions_notification, parent, false)
        return RequestsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.conns.size
    }

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {

        val data = listS.conns[position]

        holder.title.text = data.Full_name
        holder.accept.text = "Accept"
        holder.reject.text = "Reject"

        Glide.with(context).load(data.Profile_pic).into(holder.profile)

        holder.accept.setOnClickListener {
            connectClickListener?.onJobSavedClick(data)
            try {
                listS.conns.remove(data)
                notifyDataSetChanged()
            }catch (e : Exception){}
        }

    }

    fun setJobSavedClickListener(listener: ConnectClickListener) {

        connectClickListener = listener
    }
}