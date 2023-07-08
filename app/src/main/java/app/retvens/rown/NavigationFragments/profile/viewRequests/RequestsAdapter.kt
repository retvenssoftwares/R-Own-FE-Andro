package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.Connection
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.rejectConnRequest
import app.retvens.rown.utils.removeConnRequest
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
        val verification = itemView.findViewById<ImageView>(R.id.verification)
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

        if (data.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        } else{
            holder.profile.setImageResource(R.drawable.svg_user)
        }
        if (data.verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }

        holder.accept.setOnClickListener {
            connectClickListener?.onJobSavedClick(data)
            try {
                listS.conns.remove(data)
                notifyDataSetChanged()
            }catch (e : Exception){}
        }

        holder.reject.setOnClickListener {
            rejectConnRequest(data.User_id, context){
                try {
                    listS.conns.remove(data)
                    notifyDataSetChanged()
                }catch (e : Exception){}
            }
        }

        holder.itemView.setOnClickListener {
            if(data.Role == "Business Vendor / Freelancer"){
                val intent = Intent(context, VendorProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }else if (data.Role == "Hotel Owner"){
                val intent = Intent(context, OwnerProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra("userId",data.User_id)
                context.startActivity(intent)
            }
        }

    }

    fun setJobSavedClickListener(listener: ConnectClickListener) {

        connectClickListener = listener
    }
}