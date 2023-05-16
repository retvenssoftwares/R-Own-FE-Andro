package app.retvens.rown.viewAll.viewAllCommunities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOpenCommunityDetailsBinding

class OpenCommunityDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityOpenCommunityDetailsBinding

    var isBusinessVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
    }
}