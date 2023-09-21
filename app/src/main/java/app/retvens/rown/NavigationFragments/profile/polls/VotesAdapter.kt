package app.retvens.rown.NavigationFragments.profile.polls

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class VotesAdapter(val listS : List<Vote>, val context: Context) : RecyclerView.Adapter<VotesAdapter.ConnectionsViewHolder>() {

    class ConnectionsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.receiver_name)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.receiver_image)
        val role = itemView.findViewById<TextView>(R.id.active_timeMessage)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.chatprofile, parent, false)
        return ConnectionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {

        val data = listS[position]



        if (data.profile_pic.isNotEmpty()) {
            Glide.with(context).load(data.profile_pic).into(holder.profile)
        } else{
            holder.profile.setImageResource(R.drawable.svg_user)
        }

        holder.title.text = data.Full_name

        Log.e("error",data.Verification_status.toString())

        if (data.Verification_status != "false"){
            holder.verification.visibility = View.VISIBLE
        }

        try {
            holder.role.text = data.job_title
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }


//        holder.interact.setOnClickListener {
//            val intent = Intent(context,MesiboMessagingActivity::class.java)
//            intent.putExtra(MesiboUI.PEER, mesibo.address)
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
//        }



//        holder.itemView.setOnClickListener {
//            if(data.Role == "Business Vendor / Freelancer"){
//                val intent = Intent(context, VendorProfileActivity::class.java)
//                intent.putExtra("userId",data.User_id)
//                context.startActivity(intent)
//            }else if (data.Role == "Hotel Owner"){
//                val intent = Intent(context, OwnerProfileActivity::class.java)
//                intent.putExtra("userId",data.User_id)
//                context.startActivity(intent)
//            } else {
//                val intent = Intent(context, UserProfileActivity::class.java)
//                intent.putExtra("userId",data.User_id)
//                context.startActivity(intent)
//            }
//        }

    }
}