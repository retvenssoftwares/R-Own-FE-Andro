package app.retvens.rown.NavigationFragments.FragmntAdapters

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ChatSection.ChatScreen
import app.retvens.rown.Dashboard.PopularUsersAdapter
import app.retvens.rown.DataCollections.MessageEntity
import app.retvens.rown.R
import com.mesibo.emojiview.EmojiconTextView
import java.text.SimpleDateFormat
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
        holder.userName.text = data.receiver
        holder.lastMessage.text = data.message

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatScreen::class.java)
            intent.putExtra("address","${data.receiver}")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return receiver.size
    }


}