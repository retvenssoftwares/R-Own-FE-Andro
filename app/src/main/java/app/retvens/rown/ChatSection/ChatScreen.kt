package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboMessage
import com.mesibo.api.MesiboProfile

class ChatScreen : AppCompatActivity() {

    private lateinit var adapter: ChatScreenAdapter
    private val messages: ArrayList<MesiboMessage> = ArrayList()
    private var myUserId: Long = 0   // replace with your Mesibo user ID
    private lateinit var profile: MesiboProfile
    private lateinit var textMessage:EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)


        val api: Mesibo = Mesibo.getInstance()
        api.init(applicationContext)

        Mesibo.addListener(this)
        Mesibo.setAccessToken("1f3e80428678c3ad51eaceec805151bb20cc97ef11d2628a7ebb479660ka2f5c16f067")
        Mesibo.setDatabase("retvensDB", 0)

        Mesibo.start()

        myUserId = Mesibo.getUid()

        val receiversName = findViewById<TextView>(R.id.chatName)
        val name = intent.getStringExtra("name")
        receiversName.text = name


        val recyclerView: RecyclerView = findViewById(R.id.chatMessagesRecycler)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        // Create the adapter with empty message list and current user ID
        adapter = ChatScreenAdapter(this, messages, myUserId)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        textMessage = findViewById(R.id.text_content)

        val uid = intent.getLongExtra("userId",0)

        profile = Mesibo.getProfile(uid)

        val send = findViewById<ImageView>(R.id.btn_send)
        send.setOnClickListener {
            val messagetext = textMessage.text.toString()
            val name = intent.getStringExtra("name")
            if (messagetext.isNotEmpty()) {
                val message: MesiboMessage = profile.newMessage()
                message.message = messagetext
                message.send()

                messages.add(message)

                // Notify the adapter about the new data
                adapter.notifyItemInserted(messages.size - 1)

                textMessage.text.clear()
            }
        }


    }
}