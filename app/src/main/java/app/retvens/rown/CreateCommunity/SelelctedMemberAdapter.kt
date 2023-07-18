package app.retvens.rown.CreateCommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import javax.microedition.khronos.opengles.GL

class SelectedMembersAdapter(private val context: Context) :
    RecyclerView.Adapter<SelectedMembersAdapter.SelectedMemberViewHolder>() {

    var selectedMembersList: MutableList<Connections> = mutableListOf()

    class SelectedMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView = itemView.findViewById<ImageView>(R.id.selectedpic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMemberViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_selected_member, parent, false)
        return SelectedMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedMemberViewHolder, position: Int) {
        val member = selectedMembersList[position]

        if (member.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(member.Profile_pic).into(holder.profileImageView)
        } else {
            holder.profileImageView.setImageResource(R.drawable.svg_user)
        }


        // Load profile image here
    }

    override fun getItemCount(): Int {
        return selectedMembersList.size
    }

    fun addSelectedMember(member: Connections) {
        val index = selectedMembersList.indexOf(member)
        if (index == -1) {
            selectedMembersList.add(member)
        } else {
            selectedMembersList.removeAt(index)
        }
    }
}