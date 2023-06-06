package app.retvens.rown.viewAll.communityDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCommunityDetailsBinding
import com.bumptech.glide.Glide

class CommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailsBinding

    private var isSwitchToCloseCommunity = true
    var isBusinessVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener { onBackPressed() }

        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")

        Glide.with(applicationContext).load(image).into(binding.communityProfile)
        binding.communityDetailName.text = title

        binding.communityDetailEditBtn.setOnClickListener {
            val intent = Intent(this, CommunityEditActivity::class.java)
            intent.putExtra("image", image)
            intent.putExtra("title", title)
            startActivity(intent)
        }

        replaceFragment(CommunityUsersFragment())

        binding.usersText.setOnClickListener {
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            replaceFragment(CommunityUsersFragment())
        }
        binding.mediaText.setOnClickListener {
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            replaceFragment(CommunityMediaFragment())
        }
        binding.switchToCommunity.setOnClickListener {

            val dialogL = Dialog(this)
            dialogL.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogL.setContentView(R.layout.switch_to_community_dialog)
            dialogL.setCancelable(true)

            dialogL.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialogL.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogL.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogL.window?.setGravity(Gravity.BOTTOM)
            dialogL.show()

            if (isSwitchToCloseCommunity){
                val yes = dialogL.findViewById<TextView>(R.id.text_yes)
                yes.setOnClickListener {
                    binding.switchToCommunity.text = "Switch to Open Community"
                    isSwitchToCloseCommunity = false
                    dialogL.dismiss()
                }
                val no = dialogL.findViewById<TextView>(R.id.text_no)
                no.setOnClickListener {
                    dialogL.dismiss()
                }
            } else {
                dialogL.findViewById<TextView>(R.id.ttt).text = "Anyone can request to join your community, do you really want to proceed ?"
                val yes = dialogL.findViewById<TextView>(R.id.text_yes)
                yes.setOnClickListener {
                    binding.switchToCommunity.text = "Switch to Close Community"
                    isSwitchToCloseCommunity = true
                    dialogL.dismiss()
                }
                val no = dialogL.findViewById<TextView>(R.id.text_no)
                no.text = "No, Keep it Closed"
                no.setOnClickListener {
                    dialogL.dismiss()
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.community_fragment_container,fragment)
            transaction.commit()
        }
    }
}