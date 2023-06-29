package app.retvens.rown.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val myFragment = app.retvens.rown.MessagingModule.UserListFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_chat, myFragment)
            addToBackStack(null)
            commit()

        }

    }

    override fun onBackPressed() {
            finish()
            startActivity(Intent(this, DashBoardActivity::class.java))
    }
}