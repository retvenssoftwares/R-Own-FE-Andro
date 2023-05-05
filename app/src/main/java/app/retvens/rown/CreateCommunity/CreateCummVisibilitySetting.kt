package app.retvens.rown.CreateCommunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCreateCommunityBinding
import app.retvens.rown.databinding.ActivityCreateCummVisibilitySettingBinding

class CreateCummVisibilitySetting : AppCompatActivity() {

    lateinit var binding : ActivityCreateCummVisibilitySettingBinding

    var selectedLayout : Int = 1
    private  var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCummVisibilitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")

        binding.closedCummLayout.setOnClickListener {

            type = "close"

            selectedLayout = 1
            binding.layVisibility.visibility = View.VISIBLE

            binding.closedCummHintText.visibility = View.VISIBLE
            binding.openHintText.visibility = View.GONE
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.openCummLayout.setOnClickListener {

            type = "open"
            selectedLayout = 2
            binding.layVisibility.visibility = View.VISIBLE

            binding.openHintText.visibility = View.VISIBLE
            binding.closedCummHintText.visibility = View.GONE
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.nextCumm.setOnClickListener {
           val intent = Intent(this,SelectMembers::class.java)
            intent.putExtra("type",type)
            intent.putExtra("name",name)
            intent.putExtra("desc",description)
            startActivity(intent)

        }
    }
}