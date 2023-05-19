package app.retvens.rown.NavigationFragments.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.NavigationFragments.job.jobDetailsAppliedFrags.ActivitiesJobAppliedFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.CompanyDetailsFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.DescriptionFragment
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityJobDetailsAppliedBinding

class JobDetailsAppliedActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobDetailsAppliedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsAppliedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backJobsDetails.setOnClickListener {
            onBackPressed()
        }
        val fragment: Fragment = DescriptionFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_applied_fragments_container,fragment)
        transaction.commit()

        binding.descriptionJobCardTextApplied.setOnClickListener {
            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            val fragment: Fragment = DescriptionFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_applied_fragments_container,fragment)
            transaction.commit()
        }
        binding.companyJobCardTextApplied.setOnClickListener {

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            val fragment: Fragment = CompanyDetailsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_applied_fragments_container,fragment)
            transaction.commit()
        }
        binding.activitiesJobCardTextApplied.setOnClickListener {

            binding.activitiesJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.activitiesJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.descriptionJobCardTextApplied.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardTextApplied.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            val fragment: Fragment = ActivitiesJobAppliedFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_applied_fragments_container,fragment)
            transaction.commit()
        }
    }
}