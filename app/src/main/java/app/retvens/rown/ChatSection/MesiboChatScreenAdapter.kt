package app.retvens.rown.ChatSection

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.MessageEntity
import app.retvens.rown.R
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboMessage
import com.mesibo.messaging.MessageData
import java.text.SimpleDateFormat
import java.util.*

class MesiboChatScreenAdapter(
    private val context: Context,
    private var messages: List<app.retvens.rown.MessagingModule.MessageData>,
    private val myAddress: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.sender_message_layout, parent, false)
            SentMessageViewHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.receivers_msg_layout, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]



        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                val sentMessageViewHolder = holder as SentMessageViewHolder
                sentMessageViewHolder.bind(message.mesiboMessage)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                val receivedMessageViewHolder = holder as ReceivedMessageViewHolder
                receivedMessageViewHolder.bind(message.mesiboMessage)
            }
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.peer == myAddress) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }


    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.sender_message)
        private val timeTextView: TextView = itemView.findViewById(R.id.sender_time)
        private val seenIcon: ImageView = itemView.findViewById(R.id.read_msg)

        fun bind(message: MesiboMessage) {
            messageTextView.text = message.message
            val currentTime = Calendar.getInstance().time
            val formattedTime = SimpleDateFormat("hh:mm").format(currentTime)
            timeTextView.text = formattedTime

        }

    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.receiver_message)
        private val timeTextView: TextView = itemView.findViewById(R.id.receiver_time)

        fun bind(message: MesiboMessage) {
            messageTextView.text = message.message
            val currentTime = Calendar.getInstance().time
            val formattedTime = SimpleDateFormat("hh:mm").format(currentTime)
            timeTextView.text = formattedTime
        }
    }
}