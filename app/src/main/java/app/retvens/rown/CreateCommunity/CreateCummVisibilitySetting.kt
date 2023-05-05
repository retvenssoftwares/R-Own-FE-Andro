package app.retvens.rown.CreateCommunity

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCummVisibilitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closedCummLayout.setOnClickListener {
            selectedLayout = 1
            binding.layVisibility.visibility = View.VISIBLE

            binding.closedCummHintText.visibility = View.VISIBLE
            binding.openHintText.visibility = View.GONE
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.openCummLayout.setOnClickListener {
            selectedLayout = 2
            binding.layVisibility.visibility = View.VISIBLE

            binding.openHintText.visibility = View.VISIBLE
            binding.closedCummHintText.visibility = View.GONE
            binding.openCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            binding.closedCummLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_5))
        }

        binding.nextCumm.setOnClickListener {

        }
    }
}