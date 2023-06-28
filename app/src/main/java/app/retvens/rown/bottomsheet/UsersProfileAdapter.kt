package app.retvens.rown.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import com.bumptech.glide.Glide

class UsersProfileAdapter(val context: Context, var profileList : ArrayList<UserProfileRequestItem>) : RecyclerView.Adapter<UsersProfileAdapter.InterestViewHolder>() {

    interface ConnectClickListener {
        fun onJobSavedClick(connect: Post)

        fun onCancelRequest(connect: Post)
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
        holder.name.text = currentItem.Full_name
        holder.bio.text = currentItem.userBio

        val verificationStatus = currentItem.verificationStatus
        if (verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }
        if (currentItem.Role.isNotEmpty()) {
            holder.position.text = currentItem.Role
        } else {
            holder.position.text = "Incomplete Profile"
        }

        if(currentItem.Profile_pic!!.isNotEmpty()) {
            Glide.with(context).load(currentItem.Profile_pic).into(holder.profile)
        }
        holder.profile.setOnClickListener {
            showBottomDialog(currentItem.Profile_pic, currentItem.Full_name!!, currentItem.Role, currentItem.userBio, currentItem.verificationStatus)
        }

    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.connection_name)
        var position = itemView.findViewById<TextView>(R.id.connection_position)
        var bio = itemView.findViewById<TextView>(R.id.bio)
        var connectUser = itemView.findViewById<TextView>(R.id.connectUser)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
    }

    fun searchConnection(searchText : List<UserProfileRequestItem>){
        profileList = searchText as ArrayList<UserProfileRequestItem>
        notifyDataSetChanged()
    }

    private fun showBottomDialog(profilePic: String, fullName: String, role: String, bio: String, verificationStatus : String) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.connections_profile_dialog)

    val image = dialog.findViewById<ImageView>(R.id.connection_profile)
        if (profilePic.isNotEmpty()) {
            Glide.with(context).load(profilePic).into(image)
        }

        dialog.findViewById<TextView>(R.id.c_name).text = fullName

        if (role.isNotEmpty()) {
            dialog.findViewById<TextView>(R.id.title).text = role
        } else {
            dialog.findViewById<TextView>(R.id.title).text = "Incomplete Profile"
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

    fun removeUsersFromList(data: List<UserProfileRequestItem>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    profileList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

    fun removeUser(data: List<UserProfileRequestItem>){
        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        try {
            data.forEach {
                if (user_id == it.User_id){
                    profileList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}