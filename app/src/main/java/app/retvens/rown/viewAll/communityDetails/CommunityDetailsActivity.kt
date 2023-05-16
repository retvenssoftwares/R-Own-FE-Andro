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

        binding.usersText.setOnClickListener {
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            binding.llu.visibility = View.VISIBLE
        }
        binding.mediaText.setOnClickListener {
            binding.mediaText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.usersText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_5))
            binding.llu.visibility = View.GONE
        }

        binding.business.setOnClickListener {
            if (isBusinessVisible){
                binding.staticCard.visibility = View.VISIBLE
                isBusinessVisible = false
            } else {
                binding.staticCard.visibility = View.GONE
                isBusinessVisible = true
            }
        }
        binding.staticCard.setOnClickListener {
            openBottomSelectionCommunity()
        }

    }

    private fun openBottomSelectionCommunity() {
        val dialogLanguage = Dialog(this)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_remove_from_community)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()



        dialogLanguage.findViewById<TextView>(R.id.remove).setOnClickListener {
            openBottomSelectionCommunityRemove(dialogLanguage)
        }
        dialogLanguage.findViewById<TextView>(R.id.message).setOnClickListener {
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.view_profile).setOnClickListener {
            dialogLanguage.dismiss()
        }
    }

    private fun openBottomSelectionCommunityRemove(dialogL: Dialog) {
        val dialogLanguage = Dialog(this)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_remove_from_community_confermation)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        dialogLanguage.findViewById<TextView>(R.id.yes).setOnClickListener {
            dialogL.dismiss()
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.not).setOnClickListener {
            dialogLanguage.dismiss()
        }
    }
}