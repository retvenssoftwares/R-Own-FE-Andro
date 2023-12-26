package app.retvens.rown.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.MessagingModule.MesiboUserListFragment
import app.retvens.rown.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        findViewById<ImageView>(R.id.notifications_backBtn).setOnClickListener { onBackPressed() }


        val myFragment = app.retvens.rown.MessagingModule.UserListFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_chat, myFragment)
            addToBackStack(null)
            commit()
        }

//        val intent = intent
//        val selectionMode = intent.getIntExtra(MesiboUserListFragment.MESSAGE_LIST_MODE, -1)
//        val messageIds = intent.getLongArrayExtra(MesiboUI.MESSAGE_IDS)
//
//        Log.d("selectionMode", "onCreate: "+selectionMode)
//        Log.d("messageIds", "onCreate: "+messageIds)
    }



        override fun onBackPressed() {
            finish()
            startActivity(Intent(this, DashBoardActivity::class.java))
        }

}