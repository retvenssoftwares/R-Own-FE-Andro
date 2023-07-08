package app.retvens.rown.NavigationFragments.profile.viewConnections

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.ConnectionCollection.GetAllRequestDataClass
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.removeConnection
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ConnectionsAdapter(val listS : ArrayList<Connections>, val context: Context) : RecyclerView.Adapter<ConnectionsAdapter.ConnectionsViewHolder>() {

    class ConnectionsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.suggetions_notification_name)
        val interact = itemView.findViewById<TextView>(R.id.suggetions_notification_connect)
        val remove = itemView.findViewById<TextView>(R.id.suggetions_notification_view)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.suggetions_notification_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_suggetions_notification, parent, false)
        return ConnectionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {

        val data = listS[position]

        if (data.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        } else{
            holder.profile.setImageResource(R.drawable.svg_user)
        }

        holder.title.text = data.Full_name
        holder.interact.text = "INTERACT"
        holder.remove.text = "REMOVE"

        if (data.verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }
        val mesibo = data.Mesibo_account[0]

        holder.interact.setOnClickListener {
            val intent = Intent(context,MesiboMessagingActivity::class.java)
            intent.putExtra(MesiboUI.PEER, mesibo.address)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        holder.remove.setOnClickListener {
            val sharedPreferences =  context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()
            removeConnection(data.User_id, user_id, context){
                try {
                    listS.remove(data)
                    notifyDataSetChanged()
                } catch (e : Exception){

                }
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
}