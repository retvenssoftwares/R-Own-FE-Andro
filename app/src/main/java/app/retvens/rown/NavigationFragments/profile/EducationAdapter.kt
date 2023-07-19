package app.retvens.rown.NavigationFragments.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.MediaDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide

class EducationAdapter(val context: Context, val list:UserProfileRequestItem, val isOwner : Boolean) : RecyclerView.Adapter<EducationAdapter.MediaViewHolder>() {


    var mListener: OnBottomSheetFilterCommunityClickListener? = null

    fun setOnFilterClickListener(listener: OnBottomSheetFilterCommunityClickListener) {
        mListener = listener
    }

    interface OnBottomSheetFilterCommunityClickListener {
        fun onBottomSheetFilterCommunityClick(jonDetails: UserProfileRequestItem.StudentEducation, position: Int)
    }

    class MediaViewHolder(itemView: View) : ViewHolder(itemView){

        val collage = itemView.findViewById<TextView>(R.id.collageName)
        val experience = itemView.findViewById<TextView>(R.id.experience)
        val edit = itemView.findViewById<LinearLayout>(R.id.editEducation)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_education_card, parent, false)
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.studentEducation.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

        val data = list.studentEducation[position]
            holder.collage.text = data.educationPlace
            holder.experience.text = "${data.education_session_start}-${data.education_session_end}"

        if (!isOwner){
            holder.edit.visibility = View.GONE
        }

        holder.edit.setOnClickListener {
            mListener?.onBottomSheetFilterCommunityClick(data,position)
        }


    }


}