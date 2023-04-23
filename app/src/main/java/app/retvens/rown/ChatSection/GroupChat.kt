package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.AppDatabase
import app.retvens.rown.DataCollections.MessageEntity
import app.retvens.rown.R
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboGroupProfile
import com.mesibo.api.MesiboMessage
import com.mesibo.api.MesiboProfile
import com.mesibo.calls.api.MesiboCall
import com.mesibo.calls.api.MesiboCallActivity

class GroupChat : AppCompatActivity(),Mesibo.MessageListener,MesiboCall.InProgressListener,MesiboCall.IncomingListener {
    @SuppressLint("MissingInflatedId")

    private lateinit var adapter: ChatScreenAdapter
    private lateinit var profile: MesiboProfile
    private lateinit var textMessage: EditText
    private val messages: ArrayList<MessageEntity> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var myUserId: Long = 0
    private var group:String = ""
    lateinit var name:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        val api: Mesibo = Mesibo.getInstance()
        api.init(applicationContext)

        val authtoken = SharedPreferenceManagerAdmin.getInstance(applicationContext).user.uid

        Mesibo.addListener(this)
        Mesibo.setAccessToken(authtoken)
        Mesibo.setDatabase("Retvens.db", 0)
        Mesibo.start()

        val receiversName = findViewById<TextView>(R.id.groupName)

        group = intent.getStringExtra("groupId").toString()
        name = intent.getStringExtra("name").toString()
        receiversName.text = name


        val groupProfile: MesiboProfile? = Mesibo.getProfile(group)
        val name = groupProfile?.groupProfile?.firstName
//        receiversName.text = name

        textMessage = findViewById(R.id.group_text_content)

        recyclerView = findViewById(R.id.GroupMessagesRecycler)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager



        myUserId = Mesibo.getUid()

        adapter = ChatScreenAdapter(this, messages,myUserId.toString())

        recyclerView.adapter = adapter


        val send = findViewById<ImageView>(R.id.btn_send)
        send.setOnClickListener {
            val messagetext = textMessage.text.toString()

            if (messagetext.isNotEmpty()) {

                val sender = Mesibo.getSelfProfile()

                val message: MesiboMessage = sender.newMessage()
                message.profile = sender
                message.message = messagetext
                val messageEntity = MessageEntity(
                    message.mid,
                    message.profile.address,
                    group,
                    message.message,
                    message.profile.address,
                    timestamp = System.currentTimeMillis(),
                    MessageEntity.MessageState.SENT
                )
                Thread {
                    AppDatabase.getInstance(applicationContext).chatMessageDao().insertMessage(messageEntity)
                }.start()

                messages.add(messageEntity)

                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)

                textMessage.text.clear()


                val group = intent.getStringExtra("groupId")

                val groupProfile: MesiboGroupProfile? =
                    Mesibo.getProfile(group) as MesiboGroupProfile?

                if (groupProfile != null) {
                    // Create a MesiboMessage object and set its profile and message text
                    val message: MesiboMessage = groupProfile.newMessage()
                    message.message = messagetext
                    message.groupProfile = groupProfile

                    // Send the message
                    message.send()
                }


            } else {

                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun Mesibo_onMessage(message: MesiboMessage) {
        if (message.isIncoming){
            val sender: MesiboProfile = message.profile


            val messageEntity = MessageEntity(
                message.mid,
                group,
                profile.address,
                message.message,
                message.profile.address,
                timestamp = System.currentTimeMillis(),
                MessageEntity.MessageState.DELIVERED
            )

            Thread {
                AppDatabase.getInstance(applicationContext).chatMessageDao().insertMessage(messageEntity)
            }.start()

            messages.add(messageEntity)
            // Notify the adapter about the new data
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)

            if(message.isRealtimeMessage()) {
                Toast.makeText(applicationContext, "You have got a message from " + sender.getNameOrAddress("")
                    .toString() + ": " + message.message,Toast.LENGTH_SHORT).show()
            }
        } else if (message.isOutgoing()) {

                Toast.makeText(applicationContext,"message sent",Toast.LENGTH_SHORT).show()

        }
    }

    override fun Mesibo_onMessageStatus(p0: MesiboMessage) {

    }

    override fun Mesibo_onMessageUpdate(p0: MesiboMessage) {

    }

    override fun MesiboCall_OnSetCall(p0: MesiboCallActivity?, p1: MesiboCall.Call?) {
        p1?.setListener(this)
        p1?.answer(true)
    }

    override fun MesiboCall_OnMute(
        p0: MesiboCall.CallProperties?,
        p1: Boolean,
        p2: Boolean,
        p3: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnPlayInCallSound(
        p0: MesiboCall.CallProperties?,
        p1: Int,
        p2: Boolean
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnHangup(p0: MesiboCall.CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnStatus(p0: MesiboCall.CallProperties?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnAudioDeviceChanged(
        p0: MesiboCall.CallProperties?,
        p1: MesiboCall.AudioDevice?,
        p2: MesiboCall.AudioDevice?
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnVideoSourceChanged(p0: MesiboCall.CallProperties?, p1: Int, p2: Int) {

    }

    override fun MesiboCall_OnVideo(
        p0: MesiboCall.CallProperties?,
        p1: MesiboCall.VideoProperties?,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnUpdateUserInterface(
        p0: MesiboCall.CallProperties?,
        p1: Int,
        p2: Boolean,
        p3: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnOrientationChanged(
        p0: MesiboCall.CallProperties?,
        p1: Boolean,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnBatteryStatus(
        p0: MesiboCall.CallProperties?,
        p1: Boolean,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnDTMF(p0: MesiboCall.CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnIncoming(p0: MesiboProfile?, p1: Boolean): MesiboCall.CallProperties? {
        if(!p1) {
            return null // Accept video calls only
        }

        if(profile.address.isNullOrEmpty()) {
            return null
        }


        // Define call properties
        val cp = MesiboCall.getInstance().createCallProperties(true)

        // Define optional parameters
        cp.video.enabled = true
        cp.video.bitrate = 2000 // bitrate in kbps

        return cp
    }

    override fun MesiboCall_OnShowUserInterface(
        p0: MesiboCall.Call?,
        p1: MesiboCall.CallProperties?
    ): Boolean {
        return true
    }

    override fun MesiboCall_OnError(p0: MesiboCall.CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_onNotify(p0: Int, p1: MesiboProfile?, p2: Boolean): Boolean {
        return true
    }


}