package app.retvens.rown.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.MessagingModule.MesiboUserListFragment
import app.retvens.rown.R
import java.util.ArrayList


class ForwardActivity : AppCompatActivity(), OnUserSelectedListener {

    private lateinit var forward_recycle: RecyclerView
    private lateinit var caption_view: RelativeLayout
    private lateinit var caption_send: ImageButton
    private lateinit var forward_backBtn: ImageView
    private lateinit var caption_edit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forward)

        forward_recycle = findViewById(R.id.forward_recycle)
        caption_view = findViewById(R.id.caption_view)
        caption_send = findViewById(R.id.caption_send)
        caption_edit = findViewById(R.id.caption_edit)
        forward_backBtn = findViewById(R.id.forward_backBtn)

        val intent = intent
        val selectionMode = intent.getIntExtra(MesiboUserListFragment.MESSAGE_LIST_MODE, -1)
        val messageIds = intent.getLongArrayExtra(MesiboUI.MESSAGE_IDS)

        Log.d("selectionMode", "onCreate: " + selectionMode)
        Log.d("messageIds", "onCreate: " + messageIds)

        val userList = listOf(
            UserListData("pooja", "https://via.placeholder.com/150", false),
            UserListData("ankit", "https://via.placeholder.com/150", false),
            UserListData("payal","https://via.placeholder.com/150", false),
            UserListData("suiti", "https://via.placeholder.com/150", false),
            UserListData("shubham","https://via.placeholder.com/150", false),
            UserListData("bhavesh","https://via.placeholder.com/150", false),
            UserListData("aman", "https://via.placeholder.com/150", false),
            UserListData("minu", "https://via.placeholder.com/150", false),
            UserListData("moiz", "https://via.placeholder.com/150", false)
        )

        forward_recycle.layoutManager = LinearLayoutManager(this)
        val forwardAdapter = ForwardAdapter(this, userList, this)
        forward_recycle.adapter = forwardAdapter

        caption_send.setOnClickListener {

            val intent = Intent(this, MesiboMessagingActivity::class.java)
            intent.putExtra(MesiboUI.MESSAGE_ID, messageIds)
            startActivity(intent)
        }

        forward_backBtn.setOnClickListener {

            onBackPressed()
        }

        val messageList = generateMessageList()
    }

    private fun generateMessageList(): List<UserListData> {
        val list = ArrayList<UserListData>()
        for (i in 0 until 20) {
            list.add(UserListData("UserListData $i","" ,false))

        }
        return list
    }
    override fun onItemSelected(isSelected: Boolean, selectedPersonNames: List<String>) {
        Log.d("dfghjklcvbnm'", "onItemSelected: " + selectedPersonNames)

        val namesString = selectedPersonNames
        
        if (isSelected) {

            caption_view.visibility = VISIBLE

            caption_edit.text = selectedPersonNames.toString().replace("[", "").replace("]", "");

        } else {
            caption_view.visibility = GONE
        }
    }
}
