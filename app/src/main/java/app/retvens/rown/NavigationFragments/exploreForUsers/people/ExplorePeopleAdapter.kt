package app.retvens.rown.NavigationFragments.exploreForUsers.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.CreateCommunity.UploadIconAdapter
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobAdapter
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ExplorePeopleAdapter(val context: Context,val peopleList:List<Post>):RecyclerView.Adapter<ExplorePeopleAdapter.ExplorePeopleViewholder>() {

    interface ConnectClickListener {
        fun onJobSavedClick(connect: Post)
    }

    private var connectClickListener: ConnectClickListener? = null

    class ExplorePeopleViewholder(itemview:View): ViewHolder(itemview) {

        val name = itemview.findViewById<TextView>(R.id.explore_people_name)
        val profile = itemview.findViewById<ShapeableImageView>(R.id.explore_people_profile)
        val connect = itemview.findViewById<TextView>(R.id.suggetions_notification_connect)
        val viewProfile = itemview.findViewById<CardView>(R.id.ca_view_profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorePeopleViewholder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_people, parent, false)
        return ExplorePeopleViewholder(view)
    }

    override fun onBindViewHolder(holder: ExplorePeopleViewholder, position: Int) {

        val data = peopleList[position]

        if (data.Profile_pic.isNullOrEmpty()){
            holder.profile.setImageResource(R.drawable.peoplevector)
        }else{
            Glide.with(context).load(data.Profile_pic).into(holder.profile)
        }
        holder.name.text = data.Full_name

        var status = data.connectionStatus

        if (data.connectionStatus == "Connected"){
            holder.connect.text = "Remove"
        }

        if (data.connectionStatus == "Requested"){
            holder.connect.text = "Requested"
        }

        holder.connect.setOnClickListener {

            if (status == "Not Connected"){
                connectClickListener?.onJobSavedClick(data)

                holder.connect.text = "Requested"
            }


            status = "Requested"

        }


    }

    override fun getItemCount(): Int {
       return peopleList.size
    }

    fun setJobSavedClickListener(listener: ConnectClickListener) {

        connectClickListener = listener
    }
}