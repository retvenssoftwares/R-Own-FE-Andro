package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile

class ChatActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var receiverProfileAdapter: ReceiverProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val api: Mesibo = Mesibo.getInstance()
        api.init(applicationContext)

        Mesibo.addListener(this)
        Mesibo.setAccessToken("756fe9385102b2aa79acc9e58273b8cbb956a98d0e6001e664784c5ra198715909d")
        Mesibo.setDatabase("retvensDB", 0)

        Mesibo.start()


        val recycler = findViewById<RecyclerView>(R.id.chatRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        val profile = Mesibo.getUserProfiles()

        Toast.makeText(applicationContext,profile.size.toString(),Toast.LENGTH_SHORT).show()
        receiverProfileAdapter = ReceiverProfileAdapter(profile)
        recycler.adapter = receiverProfileAdapter



    }
}