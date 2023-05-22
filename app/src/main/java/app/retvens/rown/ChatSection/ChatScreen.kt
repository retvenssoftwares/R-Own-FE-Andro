package app.retvens.rown.ChatSection

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.AppDatabase
import app.retvens.rown.DataCollections.MessageEntity
import app.retvens.rown.MessagingModule.MessageData
import app.retvens.rown.MyFirebaseMessagingService
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.mesibo.api.*
import com.mesibo.calls.api.MesiboCall
import com.mesibo.calls.api.MesiboCall.CallProperties
import com.mesibo.calls.api.MesiboCallActivity


class ChatScreen : AppCompatActivity(), Mesibo.MessageListener,MesiboCall.InProgressListener,MesiboCall.IncomingListener,
    Mesibo.SyncListener,Mesibo.FileTransferListener,Mesibo.FileTransferHandler{

    private lateinit var adapter: MesiboChatScreenAdapter
//    private val messages: ArrayList<MessageEntity> = ArrayList()
    private var myUserId: Long = 0   // replace with your Mesibo user ID
    private lateinit var profile: MesiboProfile
    private lateinit var textMessage:EditText
    private lateinit var recyclerView:RecyclerView
    private lateinit var token:String
    private  var mMessages:ArrayList<MessageData> = ArrayList()
    private var mNotifyUser: MyFirebaseMessagingService? = null
    private lateinit var appDatabase: AppDatabase
    private  var getMessages:List<MessageEntity> = emptyList()
    private  var address:String = ""
    private var mReadSession:MesiboReadSession? = null
    private  var mData:List<MessageData> = emptyList()
    private var mLastReadCount = 0
    private var mLastMessageCount = 0
    private val mLastMessageStatus = -1
    private var showLoadMore = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val api: Mesibo = Mesibo.getInstance()
        api.init(applicationContext)
//
//        val authtoken = SharedPreferenceManagerAdmin.getInstance(applicationContext).user.uid
//
//        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my-database").build()



        Mesibo.addListener(this)
//        Mesibo.setAccessToken(authtoken)
//        Mesibo.setDatabase("Mesibo.db",0)
//
        Mesibo.start()





        val receiversName = findViewById<TextView>(R.id.chatName)
        var address = intent.getStringExtra("address")
        var name = intent.getStringExtra("name")
        receiversName.text = name

        val pic = findViewById<ImageView>(R.id.profileOfUser)
        val profile_pic = intent.getStringExtra("profile")
        Glide.with(applicationContext).load(profile_pic).into(pic)


        recyclerView = findViewById(R.id.chatMessagesRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        myUserId = Mesibo.getUid()

        profile = Mesibo.getSelfProfile()

//        Mesibo.setAppInForeground(this, 0, true)
//        mReadSession= MesiboReadSession(this)
//        mReadSession?.enableReadReceipt(true)
//        mReadSession?.enableMissedCalls(true)


        // Create the adapter with empty message list and current user ID
        adapter = MesiboChatScreenAdapter(this, mMessages,profile.address)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        textMessage = findViewById(R.id.text_content)

//        Thread {
//            val getMessages = AppDatabase.getInstance(applicationContext).chatMessageDao().getMessages(profile.address,address!!)
//
//            runOnUiThread {
//                messages.addAll(getMessages)
//                adapter.notifyDataSetChanged()
//                recyclerView.scrollToPosition(messages.size - 1)
//            }
//        }.start()


        val send = findViewById<ImageView>(R.id.btn_send)



        send.setOnClickListener {
            val messagetext = textMessage.text.toString()

            if (messagetext.isNotEmpty()) {

                address = intent.getStringExtra("address")
                var name = intent.getStringExtra("name")

                val message: MesiboMessage = profile.newMessage()
                message.profile = profile
                message.message = messagetext



//                val messageEntity = MessageEntity(
//                    message.mid,
//                    message.profile.address,
//                    address!!,
//                    message.message,
//                    name!!,
//                    timestamp = System.currentTimeMillis()
//                )
//                Thread {
//                    AppDatabase.getInstance(applicationContext).chatMessageDao().insertMessage(messageEntity)
//                }.start()

                val Data = MessageData(baseContext,message)

                mMessages.add(Data)

                adapter.notifyItemInserted(mMessages.size - 1)
                recyclerView.scrollToPosition(mMessages.size - 1)

                textMessage.text.clear()




                val recipientProfile:MesiboProfile
                recipientProfile = Mesibo.getProfile(address)
//                recipientProfile.name = "Recipient Name"


                // Create a MesiboMessage object and set its profile and message text
                val sendmessage:MesiboMessage = recipientProfile.newMessage()
                sendmessage.message = messagetext
                sendmessage.send()


            }else{

                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()

            }
        }



        val video = findViewById<ImageView>(R.id.video_Call)

        video.setOnClickListener {

            address = intent.getStringExtra("address")
            Toast.makeText(applicationContext,address,Toast.LENGTH_SHORT).show()
            val recipientProfile:MesiboProfile
            recipientProfile = Mesibo.getProfile(address)


            MesiboCall.getInstance().init(applicationContext)
            MesiboCall.getInstance().callUi(this, recipientProfile, true)

        }

        val voice = findViewById<ImageView>(R.id.voice_Call)
        voice.setOnClickListener {
            address = intent.getStringExtra("address")
            Toast.makeText(applicationContext,address,Toast.LENGTH_SHORT).show()
            val recipientProfile:MesiboProfile
            recipientProfile = Mesibo.getProfile(address)



            MesiboCall.getInstance().callUi(this, recipientProfile, false)
        }

        val call:MesiboCall  = MesiboCall.getInstance()
        call.init(applicationContext)

        mNotifyUser = MyFirebaseMessagingService()

    }

//    fun notify(title: String, message: String) {
//        mNotifyUser!!.generateNotification(title,message)
//    }

    override fun Mesibo_onMessage(message: MesiboMessage) {
        address = intent.getStringExtra("address")!!

        val incomingAddress = message.profile.address
        if (message.isIncoming) {
            val sender: MesiboProfile = message.profile
//
//            if (incomingAddress == address){
//                val messageEntity = MessageEntity(
//                    message.mid,
//                    address,
//                    profile.address,
//                    message.message,
//                    profile.address,
//                    timestamp = System.currentTimeMillis()
//                )
//
//                messages.add(messageEntity)
//
//                Thread {
//                    AppDatabase.getInstance(applicationContext).chatMessageDao().insertMessage(messageEntity)
//                }.start()

            val data = MessageData(baseContext, message)

            mMessages.add(data)



            // Notify the adapter about the new data
            adapter.notifyItemInserted(mMessages.size - 1)
            recyclerView.scrollToPosition(mMessages.size - 1)




            if (message.isRealtimeMessage()) {


                Toast.makeText(
                    applicationContext, "You have got a message from " + sender.getNameOrAddress("")
                        .toString() + ": " + message.message, Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext, "you have a message", Toast.LENGTH_SHORT).show()
            }
        }else if (message.isOutgoing){
            Toast.makeText(applicationContext, "message sent ", Toast.LENGTH_SHORT).show()
        }

        }

    private fun loadFromDB(count: Int) {
        this.mLastMessageCount = this.mMessages.size
        this.showLoadMore = false
        this.mLastReadCount = mReadSession!!.read(count)
        if (this.mLastReadCount == count) {
            this.showLoadMore = true
        } else {
            if (0 == this.mLastReadCount && this.mMessages.size == 0) {
                this.updateUiIfLastMessage(null as MesiboMessage?)
            }
            mReadSession!!.sync(count, this)
        }
    }

     private fun updateUiIfLastMessage(msg : MesiboMessage?) {
         if (null != msg) {
             if (msg.isRealtimeMessage()) {
                 return;
             }

             if (!msg.isLastMessage()) {
                 return;
             }
         }
     }

    override fun Mesibo_onMessageStatus(p0: MesiboMessage) {

    }

    override fun Mesibo_onMessageUpdate(p0: MesiboMessage) {

    }

    override fun MesiboCall_OnSetCall(p0: MesiboCallActivity?, p1: MesiboCall.Call?) {
        TODO("Not yet implemented")
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

    override fun MesiboCall_OnIncoming(p0: MesiboProfile?, p1: Boolean): CallProperties {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnShowUserInterface(
        p0: MesiboCall.Call?,
        p1: CallProperties?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnError(p0: CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_onNotify(p0: Int, p1: MesiboProfile?, p2: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun Mesibo_onSync(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun Mesibo_onFileTransferProgress(p0: MesiboFileTransfer?): Boolean {
        TODO("Not yet implemented")
    }

    override fun Mesibo_onStartFileTransfer(p0: MesiboFileTransfer?): Boolean {
        TODO("Not yet implemented")
    }

    override fun Mesibo_onStopFileTransfer(p0: MesiboFileTransfer?): Boolean {
        TODO("Not yet implemented")
    }


}



//    override fun MesiboCall_OnSetCall(p0: MesiboCallActivity?, p1: MesiboCall.Call?) {
//
//
//        p1?.setListener(this)
//        p1?.answer(true)
//
//    }



//    override fun MesiboCall_OnIncoming(p0: MesiboProfile?, p1: Boolean): CallProperties? {
//        if(!p1) {
//            return null // Accept video calls only
//        }
//
//        if(profile.address.isNullOrEmpty()) {
//            return null
//        }
//
//
//        // Define call properties
//        val cp = MesiboCall.getInstance().createCallProperties(true)
//
//        // Define optional parameters
//        cp.video.enabled = true
//        cp.video.bitrate = 2000 // bitrate in kbps
//
//        return cp
//    }



