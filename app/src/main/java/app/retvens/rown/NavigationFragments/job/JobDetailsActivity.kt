package app.retvens.rown.NavigationFragments.job

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityJobDetailsBinding

class JobDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityJobDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backJobsDetails.setOnClickListener {
            onBackPressed()
        }

        binding.descriptionJobCardText.setOnClickListener {
            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llDesc.visibility = View.VISIBLE
            binding.llCompany.visibility = View.GONE
            binding.llActivities.visibility = View.GONE
        }
        binding.companyJobCardText.setOnClickListener {

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.llActivities.visibility = View.GONE
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

            binding.llActivities.visibility = View.VISIBLE
            binding.llDesc.visibility = View.GONE
            binding.llCompany.visibility = View.GONE
        }

        binding.cardApplyJob.setOnClickListener {

            val dialogRole = Dialog(this)
            dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogRole.setContentView(R.layout.bottom_sheet_aply_for_job)
            dialogRole.setCancelable(true)

            dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogRole.window?.setGravity(Gravity.BOTTOM)
            dialogRole.show()



        }
    }
}