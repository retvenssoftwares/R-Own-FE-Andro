package app.retvens.rown.NavigationFragments.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityJobDetailsAppliedBinding

class JobDetailsAppliedActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobDetailsAppliedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsAppliedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.descriptionJobCardTextApplied.setOnClickListener {
            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDescApplied.visibility = View.VISIBLE
            binding.llCompanyApplied.visibility = View.GONE
        }
        binding.companyJobCardTextApplied.setOnClickListener {

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDescApplied.visibility = View.GONE
            binding.llCompanyApplied.visibility = View.VISIBLE
        }
        binding.activitiesJobCardTextApplied.setOnClickListener {

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDescApplied.visibility = View.GONE
            binding.llCompanyApplied.visibility = View.GONE
        }
    }
}