package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.MyFirebaseMessagingService
import app.retvens.rown.R
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboGroupProfile
import com.mesibo.api.MesiboMessage
import com.mesibo.api.MesiboProfile
import com.mesibo.api.MesiboReadSession
import com.mesibo.calls.api.CallStateReceiver.init
import com.mesibo.calls.api.MesiboCall
import com.mesibo.calls.api.MesiboCall.CallProperties
import com.mesibo.calls.api.MesiboCallActivity
import com.mesibo.messaging.MesiboImages.init
import org.webrtc.NetworkMonitor.init

class ChatScreen : AppCompatActivity(), Mesibo.MessageListener,MesiboCall.InProgressListener,MesiboCall.IncomingListener{

    private lateinit var adapter: ChatScreenAdapter
    private val messages: ArrayList<MesiboMessage> = ArrayList()
    private var myUserId: Long = 0   // replace with your Mesibo user ID
    private lateinit var profile: MesiboProfile
    private lateinit var textMessage:EditText
    private lateinit var recyclerView:RecyclerView
    private lateinit var token:String
    private var mNotifyUser: MyFirebaseMessagingService? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)


        val api: Mesibo = Mesibo.getInstance()
        api.init(applicationContext)

        val authtoken = SharedPreferenceManagerAdmin.getInstance(applicationContext).user.uid

        Toast.makeText(applicationContext,authtoken,Toast.LENGTH_SHORT).show()

        Mesibo.addListener(this)
        Mesibo.setAccessToken(authtoken)
        Mesibo.setDatabase("retvensDB", 0)

        Mesibo.start()

        val receiversName = findViewById<TextView>(R.id.chatName)
        val name = intent.getStringExtra("address")
        receiversName.text = name
        val uid = intent.getLongExtra("userId",0)



        recyclerView = findViewById(R.id.chatMessagesRecycler)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        myUserId = Mesibo.getUid()

        profile = Mesibo.getSelfProfile()

        Mesibo.setAppInForeground(this, 0, true)
        val mReadSession:MesiboReadSession = profile.createReadSession(this)
        mReadSession?.enableReadReceipt(true)
        mReadSession?.read(100)


        mReadSession?.enableMissedCalls(true)

        // Create the adapter with empty message list and current user ID
        adapter = ChatScreenAdapter(this, messages,myUserId)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        textMessage = findViewById(R.id.text_content)



        val send = findViewById<ImageView>(R.id.btn_send)
        send.setOnClickListener {
            val messagetext = textMessage.text.toString()

            if (messagetext.isNotEmpty()) {
                val name = intent.getStringExtra("address")

                val message: MesiboMessage = profile.newMessage()
                message.profile = profile
                message.message = messagetext
                messages.add(message)


                // Notify the adapter about the new data
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)

                textMessage.text.clear()




                val recipientProfile:MesiboProfile
                recipientProfile = Mesibo.getProfile(name)
//                recipientProfile.name = "Recipient Name"


                // Create a MesiboMessage object and set its profile and message text
                val sendmessage:MesiboMessage = recipientProfile.newMessage()
                sendmessage.message = messagetext
                sendmessage.send()

                val title = message.profile.address

//                notify(title,sendmessage.message)



            }else{

                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()

            }
        }


        val video = findViewById<ImageView>(R.id.video_Call)

        video.setOnClickListener {

            val name = intent.getStringExtra("address")

            val recipientProfile:MesiboProfile
            recipientProfile = Mesibo.getProfile(name)


            MesiboCall.getInstance().init(applicationContext)
            MesiboCall.getInstance().callUi(this, recipientProfile, true)

        }

//        val groupProfile: MesiboProfile? = Mesibo.getProfile(2740197)
//
//
//        Toast.makeText(applicationContext,groupProfile?.name,Toast.LENGTH_SHORT).show()

        val voice = findViewById<ImageView>(R.id.voice_Call)
        voice.setOnClickListener {
            val name = intent.getStringExtra("address")
            val recipientProfile:MesiboProfile
            recipientProfile = Mesibo.getProfile(name)


            MesiboCall.getInstance().init(applicationContext)
            MesiboCall.getInstance().callUi(this, recipientProfile, false)
        }

        val call:MesiboCall  = MesiboCall.getInstance()
        call.init(applicationContext)


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result

            // Log and toast
            Log.e("token", token)

            Mesibo.setPushToken(token)
        })

        mNotifyUser = MyFirebaseMessagingService()

 }

//    fun notify(title: String, message: String) {
//        mNotifyUser!!.generateNotification(title,message)
//    }

    override fun Mesibo_onMessage(message: MesiboMessage) {

        if (message.isIncoming){
            val sender: MesiboProfile = message.profile

            message.save()
            messages.add(message)

            // Notify the adapter about the new data
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)

            if(message.isRealtimeMessage()) {
                Toast.makeText(applicationContext, "You have got a message from " + sender.getNameOrAddress("")
                    .toString() + ": " + message.message,Toast.LENGTH_SHORT).show()
            }
        } else if (message.isOutgoing()) {



        }

    }

    override fun Mesibo_onMessageStatus(p0: MesiboMessage) {

//        val seenIcon:ImageView = findViewById(R.id.read_msg)
//
//        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.doublecheck)
//        seenIcon.setImageDrawable(drawable)



    }

    override fun Mesibo_onMessageUpdate(p0: MesiboMessage) {

    }

    override fun MesiboCall_OnSetCall(p0: MesiboCallActivity?, p1: MesiboCall.Call?) {


        p1?.setListener(this)
        p1?.answer(true)

    }

    override fun MesiboCall_OnMute(p0: CallProperties?, p1: Boolean, p2: Boolean, p3: Boolean) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnPlayInCallSound(p0: CallProperties?, p1: Int, p2: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnHangup(p0: CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnStatus(p0: CallProperties?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnAudioDeviceChanged(
        p0: CallProperties?,
        p1: MesiboCall.AudioDevice?,
        p2: MesiboCall.AudioDevice?
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnVideoSourceChanged(p0: CallProperties?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnVideo(
        p0: CallProperties?,
        p1: MesiboCall.VideoProperties?,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnUpdateUserInterface(
        p0: CallProperties?,
        p1: Int,
        p2: Boolean,
        p3: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnOrientationChanged(p0: CallProperties?, p1: Boolean, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnBatteryStatus(p0: CallProperties?, p1: Boolean, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnDTMF(p0: CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnIncoming(p0: MesiboProfile?, p1: Boolean): CallProperties? {
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
        p1: CallProperties?
    ): Boolean {
       return true
    }

    override fun MesiboCall_OnError(p0: CallProperties?, p1: Int) {

    }

    override fun MesiboCall_onNotify(p0: Int, p1: MesiboProfile?, p2: Boolean): Boolean {
       return true
    }


}