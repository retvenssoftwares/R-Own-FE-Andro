package app.retvens.rown.Dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.R

class PopularUsersAdapter(val context: Context, var userList:List<MesiboUsersData>) :
    RecyclerView.Adapter<PopularUsersAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView = itemView.findViewById<TextView>(R.id.connection_name)
        var lastSeen = itemView.findViewById<TextView>(R.id.active_timeMessage)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)

        var button = itemView.findViewById<Button>(R.id.connectUser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardofusers, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = userList[position]

        holder.nameTextView.text = data.address

        holder.button.setOnClickListener {
            holder.button.setTextColor(Color.BLACK)
            holder.button.setText("Request")
            holder.button.setBackgroundColor(Color.BLUE)
        }

        holder.profile.setOnClickListener {
            showBottomDialog()
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
        private fun showBottomDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.connections_profile_dialog)

            dialog.findViewById<CardView>(R.id.card_message_person).setOnClickListener {
                showMessageDialog()
            }
//            val back =  dialog.findViewById<>()

            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun showMessageDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.connection_message_layout)

//            val back =  dialog.findViewById<>()

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

}