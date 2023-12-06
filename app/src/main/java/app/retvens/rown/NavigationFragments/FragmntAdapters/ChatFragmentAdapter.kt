package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.MessageEntity
import app.retvens.rown.R
import com.mesibo.emojiview.EmojiconTextView
import java.util.*

class ChatFragmentAdapter(val context: Context, private var receiver: List<MessageEntity>):RecyclerView.Adapter<ChatFragmentAdapter.ProfileViewHolder>(){


    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.mes_rv_name)
        val lastMessage = itemView.findViewById<EmojiconTextView>(R.id.mes_cont_post_or_details)
        val alert = itemView.findViewById<TextView>(R.id.mes_alert)
        val time = itemView.findViewById<TextView>(R.id.mes_rv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.userchatlist, parent, false)
        return ChatFragmentAdapter.ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val data = receiver[position]
        holder.userName.text = data.receiverName
        holder.lastMessage.text = data.message

    }

    override fun getItemCount(): Int {
        return receiver.size
    }


}