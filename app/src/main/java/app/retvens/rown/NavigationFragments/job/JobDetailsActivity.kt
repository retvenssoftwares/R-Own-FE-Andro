package app.retvens.rown.NavigationFragments.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityJobDetailsBinding

class JobDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityJobDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.descriptionJobCardText.setOnClickListener {
            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDesc.visibility = View.VISIBLE
            binding.llCompany.visibility = View.GONE
        }
        binding.companyJobCardText.setOnClickListener {

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDesc.visibility = View.GONE
            binding.llCompany.visibility = View.VISIBLE
        }
        binding.activityJobCardText.setOnClickListener {

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDesc.visibility = View.GONE
            binding.llCompany.visibility = View.GONE
        }
    }
}