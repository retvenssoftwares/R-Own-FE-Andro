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

        binding.jobNameDetails.text = intent.getStringExtra("title")

        val locat = intent.getStringExtra("location")
        val company = intent.getStringExtra("company")

        binding.locationJobDetails.setText("$company.$locat")
        binding.appliedType.text = intent?.getStringExtra("type")



        binding.backJobsDetails.setOnClickListener {
            onBackPressed()
        }

        val desc = intent.getStringExtra("description")
        val skill = intent.getStringExtra("skill")

        val bundle = Bundle()
        bundle.putString("desc", desc)
        bundle.putString("skill",skill)
        val fragment: Fragment = DescriptionFragment()
        fragment.arguments = bundle
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
            val desc = intent.getStringExtra("description")
            val skill = intent.getStringExtra("skill")

            val bundle = Bundle()
            bundle.putString("desc", desc)
            bundle.putString("skill",skill)

            val fragment = DescriptionFragment()
            fragment.arguments = bundle
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

            val ApplicationId = intent.getStringExtra("AppId")

            val bundle = Bundle()
            bundle.putString("AppID", ApplicationId)
            val fragment: Fragment = ActivitiesJobAppliedFragment()
            fragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_applied_fragments_container,fragment)
            transaction.commit()
        }
    }
}