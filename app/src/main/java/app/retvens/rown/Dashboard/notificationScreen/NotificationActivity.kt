package app.retvens.rown.Dashboard.notificationScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.Dashboard.notificationScreen.community.CommunityNotificationFragment
import app.retvens.rown.Dashboard.notificationScreen.connections.ConnectionsNotificationFragment
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationFragment
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationsBackBtn.setOnClickListener{ onBackPressed() }

        binding.personalNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            replaceFragment(PersonalNotificationFragment())
            binding.layoutSuggetion.visibility = View.GONE
        }
        binding.communityNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            replaceFragment(CommunityNotificationFragment())
            binding.layoutSuggetion.visibility = View.GONE
        }
        binding.connectionNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            replaceFragment(ConnectionsNotificationFragment())
            binding.layoutSuggetion.visibility = View.GONE
        }
        binding.suggetionNotification.setOnClickListener {
            binding.personalNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.communityNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.connectionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.suggetionNotification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))

            binding.layoutSuggetion.visibility = View.VISIBLE
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.notification_fragment_container,fragment)
            transaction.commit()
        }
    }

}