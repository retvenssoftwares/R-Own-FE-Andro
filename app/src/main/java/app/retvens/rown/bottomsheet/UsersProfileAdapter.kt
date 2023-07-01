package app.retvens.rown.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.bottomSheetPeople.MatchedContact
import app.retvens.rown.utils.removeConnRequest
import app.retvens.rown.utils.sendConnectionRequest
import com.bumptech.glide.Glide

class UsersProfileAdapter(val context: Context, var profileList : ArrayList<MatchedContact>) : RecyclerView.Adapter<UsersProfileAdapter.InterestViewHolder>() {

    interface ConnectClickListener {
        fun onJobSavedClick(connect: MatchedContact)

        fun onCancelRequest(connect: MatchedContact)
    }

    private var connectClickListener: ConnectClickListener? = null

    fun setJobSavedClickListener(listener: ConnectClickListener) {
        connectClickListener = listener
    }

    fun cancelConnRequest(listener: ConnectClickListener){
        connectClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cardofusers,parent,false)
        return InterestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val currentItem = profileList[position]
//        holder.name.text = currentItem.Mesibo_account.get(0).address.toString()
        holder.name.text = currentItem.matchedNumber.Full_name
        holder.bio.text = currentItem.matchedNumber.userBio

        val verificationStatus = currentItem.matchedNumber.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }
        if (currentItem.matchedNumber.Role.isNotEmpty()) {
            holder.position.text = currentItem.matchedNumber.Role
        } else {
            holder.position.text = "Incomplete Profile"
        }

        var status = currentItem.connectionStatus

        if (currentItem.connectionStatus == "Connected") {
            holder.connect.text = "Interact"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        if (currentItem.connectionStatus == "Requested") {
            holder.connect.text = "Requested"
            holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        if(currentItem.matchedNumber!!.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(currentItem.matchedNumber.Profile_pic).into(holder.profile)
        }

        holder.connect.setOnClickListener {

            if (status == "Not Connected") {
                connectClickListener?.onJobSavedClick(currentItem)

                holder.connect.text = "Requested"
                currentItem.connectionStatus = "Requested"
                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
            }

            if (status == "Requested" || currentItem.connectionStatus == "Requested") {
                connectClickListener?.onCancelRequest(currentItem)

                holder.connect.setBackgroundColor(ContextCompat.getColor(context, R.color.green_own))
                holder.connect.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.connect.text = "CONNECT"
                currentItem.connectionStatus = "Not Connected"
            }

            if (holder.connect.text == "Interact") {
                val intent = Intent(context, MesiboMessagingActivity::class.java)
                intent.putExtra(MesiboUI.PEER, currentItem.matchedNumber.Mesibo_account[0].address)
                context.startActivity(intent)

            }

            status = "Requested"

        }

        holder.profile.setOnClickListener {
            showBottomDialog(currentItem.matchedNumber.Profile_pic, currentItem.matchedNumber.User_id, currentItem.matchedNumber.Full_name, currentItem.matchedNumber.Role, currentItem.matchedNumber.userBio, currentItem.matchedNumber.verificationStatus, currentItem.connectionStatus)
        }

    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.connection_name)
        var position = itemView.findViewById<TextView>(R.id.connection_position)
        var bio = itemView.findViewById<TextView>(R.id.bio)
        var connect = itemView.findViewById<TextView>(R.id.connectUser)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
    }

    fun searchConnection(searchText : List<MatchedContact>){
        profileList = searchText as ArrayList<MatchedContact>
        notifyDataSetChanged()
    }

    private fun showBottomDialog(profilePic: String, userId : String, fullName: String, role: String, bio: String, verificationStatus : String, connectionStatus : String) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.connections_profile_dialog)

    val image = dialog.findViewById<ImageView>(R.id.connection_profile)
        if (profilePic.isNotEmpty()) {
            Glide.with(context).load(profilePic).into(image)
        }

        dialog.findViewById<TextView>(R.id.c_name).text = fullName
        val connect = dialog.findViewById<TextView>(R.id.connect)

        if (role.isNotEmpty()) {
            dialog.findViewById<TextView>(R.id.title).text = role
        } else {
            dialog.findViewById<TextView>(R.id.title).text = "Incomplete Profile"
        }

        if (connectionStatus == "Connected") {
            connect.text = "Interact"
            connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        if (connectionStatus == "Requested") {
            connect.text = "Requested"
            connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
        }

        connect.setOnClickListener {

            if (connectionStatus == "Not Connected") {
                sendConnectionRequest(userId, context, connect)

                connect.text = "Requested"
                connect.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                connect.setTextColor(ContextCompat.getColor(context, R.color.green_own))
            }

            if (connectionStatus == "Requested") {
                removeConnRequest(userId, context, connect)

                connect.setBackgroundColor(ContextCompat.getColor(context, R.color.green_own))
                connect.setTextColor(ContextCompat.getColor(context, R.color.white))
                connect.text = "CONNECT"
            }
        }

        dialog.findViewById<TextView>(R.id.description).text = bio

    val verification = dialog.findViewById<ImageView>(R.id.verification)
    dialog.findViewById<CardView>(R.id.card_message_person).setOnClickListener {
        showMessageDialog(profilePic, fullName)
    }
        if (verificationStatus != "false"){
            verification.visibility = View.VISIBLE
        }
//            val back =  dialog.findViewById<>()

    dialog.show()
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
    dialog.window?.setGravity(Gravity.BOTTOM)
}

    private fun showMessageDialog(profilePic: String, fullName: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.connection_message_layout)

        val messageImg = dialog.findViewById<ImageView>(R.id.message_profile)
        val messageName = dialog.findViewById<TextView>(R.id.message_name)

        Glide.with(context).load(profilePic).into(messageImg)
        messageName.text = fullName
//            val back =  dialog.findViewById<>()

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    fun removeUsersFromList(data: List<MatchedContact>){
        try {
            data.forEach {
                if (it.matchedNumber.display_status == "0"){
                    profileList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

    fun removeUser(data: List<MatchedContact>){
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        try {
            data.forEach {
                if (user_id == it.matchedNumber.User_id){
                    profileList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}