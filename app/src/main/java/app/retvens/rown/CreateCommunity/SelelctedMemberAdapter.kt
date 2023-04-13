package app.retvens.rown.CreateCommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.R

class SelectedMembersAdapter(private val context: Context) :
    RecyclerView.Adapter<SelectedMembersAdapter.SelectedMemberViewHolder>() {

    var selectedMembersList: MutableList<MesiboUsersData> = mutableListOf()

    class SelectedMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView = itemView.findViewById<ImageView>(R.id.selectedpic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMemberViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_selected_member, parent, false)
        return SelectedMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedMemberViewHolder, position: Int) {
        val member = selectedMembersList[position]


        // Load profile image here
    }

    override fun getItemCount(): Int {
        return selectedMembersList.size
    }

    fun addSelectedMember(member: MesiboUsersData) {
        selectedMembersList.add(member)
        notifyDataSetChanged()
    }
}