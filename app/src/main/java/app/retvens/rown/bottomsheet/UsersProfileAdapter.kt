package app.retvens.rown.bottomsheet

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

class UsersProfileAdapter(val context: Context, var interestList : List<UserProfileRequestItem>) : RecyclerView.Adapter<UsersProfileAdapter.InterestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cardofusers,parent,false)
        return InterestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return interestList.size
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val currentItem = interestList[position]
//        holder.name.text = currentItem.Mesibo_account.get(0).address.toString()
        holder.name.text = currentItem.Full_name
        holder.position.text = currentItem.User_id

            Glide.with(context).load(currentItem.Profile_pic).into(holder.profile)

        holder.profile.setOnClickListener {
            showBottomDialog()
        }


    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.connection_name)
        var position = itemView.findViewById<TextView>(R.id.connection_position)
        var profile = itemView.findViewById<ImageView>(R.id.connection_profile)
    }

//    fun searchInterest(searchText : List<GetInterests>){
//        interestList = searchText
//        notifyDataSetChanged()
//    }

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