package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.ConnectionCollection.hospitalityExpertInfo
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import retrofit2.Callback
import java.lang.Exception

class ExperienceAdapter(val context: Context, val list:UserProfileRequestItem, val isOwner : Boolean, val role : String) : RecyclerView.Adapter<ExperienceAdapter.MediaViewHolder>() {

    var mListener: OnBottomSheetFilterCommunityClickListener? = null

    fun setOnFilterClickListener(listener: OnBottomSheetFilterCommunityClickListener) {
        mListener = listener
    }

    interface OnBottomSheetFilterCommunityClickListener {
        fun onBottomSheetFilterCommunityClick(jonDetails:NormalUserInfoo, hospitalityExpert: hospitalityExpertInfo,position: Int)
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

        if (!isOwner){
            holder.edit.visibility = View.GONE
        }

        try {
            val hosData = list.hospitalityExpertInfo[position]
            val data = list.normalUserInfo[position]

            if (role == "Hospitality Expert") {

                holder.company.text = hosData.hotelCompany
                holder.timeLine.text = "${hosData.jobstartYear} - ${hosData.jobendYear}"
                holder.experience.text = hosData.jobtitle

                Toast.makeText(context, "hosData - ${hosData.jobstartYear}", Toast.LENGTH_SHORT).show()
            } else {

                holder.company.text = data.jobCompany
                holder.timeLine.text = "${data.jobStartYear} - ${data.jobEndYear}"
                holder.experience.text = data.jobTitle
                Toast.makeText(context, "data - ${data.jobCompany}", Toast.LENGTH_SHORT).show()
            }

            holder.edit.setOnClickListener {
                mListener?.onBottomSheetFilterCommunityClick(data, hosData,position)
            }
        } catch (e:Exception){  }

    }


}