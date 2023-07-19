package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import retrofit2.Callback

class ExperienceAdapter(val context: Context, val list:UserProfileRequestItem, val isOwner : Boolean) : RecyclerView.Adapter<ExperienceAdapter.MediaViewHolder>() {

    var mListener: OnBottomSheetFilterCommunityClickListener? = null

    fun setOnFilterClickListener(listener: OnBottomSheetFilterCommunityClickListener) {
        mListener = listener
    }

    interface OnBottomSheetFilterCommunityClickListener {
        fun onBottomSheetFilterCommunityClick(jonDetails:NormalUserInfoo,position: Int)
    }

    class MediaViewHolder(itemView: View) : ViewHolder(itemView){

        val company = itemView.findViewById<TextView>(R.id.companyName)
        val experience = itemView.findViewById<TextView>(R.id.designation)
        val timeLine = itemView.findViewById<TextView>(R.id.timeLine)
        val edit = itemView.findViewById<LinearLayout>(R.id.edit)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_card_experience, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.normalUserInfo.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

       val data = list.normalUserInfo[position]

        if (!isOwner){
            holder.edit.visibility = View.GONE
        }

        holder.company.text = data.jobCompany
        holder.timeLine.text = "${data.jobStartYear}-${data.jobEndYear}"
        holder.experience.text = data.jobTitle

        holder.edit.setOnClickListener {
            mListener?.onBottomSheetFilterCommunityClick(data,position)
        }
    }


}