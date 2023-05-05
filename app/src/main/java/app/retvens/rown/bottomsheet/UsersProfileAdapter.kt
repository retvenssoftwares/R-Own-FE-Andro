package app.retvens.rown.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import com.bumptech.glide.Glide

class UsersProfileAdapter(val context: Context, var profileList : List<UserProfileRequestItem>) : RecyclerView.Adapter<UsersProfileAdapter.InterestViewHolder>() {

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
        holder.position.text = currentItem.User_id

            Glide.with(context).load(currentItem.Profile_pic).into(holder.profile)

        holder.profile.setOnClickListener {
            showBottomDialog(currentItem.Profile_pic!!, currentItem.Full_name!!)
        }


    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.connection_name)
        var position = itemView.findViewById<TextView>(R.id.connection_position)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)
    }

    fun searchConnection(searchText : List<UserProfileRequestItem>){
        profileList = searchText
        notifyDataSetChanged()
    }

    private fun showBottomDialog(profilePic: String, fullName: String) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.connections_profile_dialog)

    val image = dialog.findViewById<ImageView>(R.id.connection_profile)
        Glide.with(context).load(profilePic).into(image)

        dialog.findViewById<TextView>(R.id.c_name).text = fullName

    dialog.findViewById<CardView>(R.id.card_message_person).setOnClickListener {
        showMessageDialog(profilePic, fullName)
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

}