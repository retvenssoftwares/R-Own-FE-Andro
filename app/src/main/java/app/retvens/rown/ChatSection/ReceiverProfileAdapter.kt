package app.retvens.rown.ChatSection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile
import java.util.concurrent.ConcurrentHashMap

class ReceiverProfileAdapter(private var profiles: ConcurrentHashMap<String, MesiboProfile>?) :
    RecyclerView.Adapter<ReceiverProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.receiver_name)
        private val addressTextView: TextView = itemView.findViewById(R.id.receiver_lastMessage)

        fun bind(profile: MesiboProfile) {
            nameTextView.text = profile.address
            addressTextView.text = profile.uid.toString()

            getLastSeenText(profile.lastSeen.toLong())

        }

        private fun getLastSeenText(lastSeen: Long): String {
            val currentTimeMillis = System.currentTimeMillis()
            val lastSeenMillis = lastSeen * 1000
            val diffMillis = currentTimeMillis - lastSeenMillis

            return when {
                diffMillis < 60_000 -> "Online"
                diffMillis < 3_600_000 -> "${diffMillis / 60_000}m ago"
                else -> "${diffMillis / 3_600_000}h ago"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatprofile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles?.get(position.toString())
        profile?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return profiles?.size ?: 0
    }
}