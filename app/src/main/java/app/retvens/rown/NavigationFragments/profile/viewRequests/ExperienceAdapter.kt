package app.retvens.rown.NavigationFragments.profile.viewRequests

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
    private lateinit var data:NormalUserInfoo
    private lateinit var hosData:hospitalityExpertInfo

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
        return if (role == "Hospitality Expert") {
            list.hospitalityExpertInfo.size
        } else {
            list.normalUserInfo.size
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

        if (!isOwner){
            holder.edit.visibility = View.GONE
        }

        Log.e("data",list.toString())




            if (role == "Hospitality Expert") {
                hosData = list.hospitalityExpertInfo[position]
                data = NormalUserInfoo("","","","","")
                holder.company.text = hosData.hotelCompany
                holder.timeLine.text = "${hosData.jobstartYear} - ${hosData.jobendYear}"
                holder.experience.text = hosData.jobtitle

            } else {
                data = list.normalUserInfo[position]
                hosData = hospitalityExpertInfo("","","","","")
                holder.company.text = data.jobCompany
                holder.timeLine.text = "${data.jobStartYear} - ${data.jobEndYear}"
                holder.experience.text = data.jobTitle
            }

            holder.edit.setOnClickListener {
                mListener?.onBottomSheetFilterCommunityClick(data, hosData,position)
            }

    }


}