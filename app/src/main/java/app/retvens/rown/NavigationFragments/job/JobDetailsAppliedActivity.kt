package app.retvens.rown.NavigationFragments.job

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobDetailsDataClass
import app.retvens.rown.NavigationFragments.job.jobDetailsAppliedFrags.ActivitiesJobAppliedFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.ActivitiesFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.CompanyDetailsFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.DescriptionFragment
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityJobDetailsAppliedBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailsAppliedActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobDetailsAppliedBinding

    private lateinit var progressDialog:Dialog
    private var skills:String = ""
    private var description:String= ""
    private var companyDertails:String=""
    private var companyWeb:String=""
    private var fragmentPositon="1"
    private  var peopleName:String=""
    private  var peopleRole:String=""
    private  var peopleProfile:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsAppliedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setCancelable(true)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image = progressDialog.findViewById<ImageView>(R.id.imageview)

        Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
//        progressDialog.show()

        binding.backJobsDetails.setOnClickListener {
            onBackPressed()
        }

        getJobDetail()

//        val desc = intent.getStringExtra("description")
//        val skill = intent.getStringExtra("skill")

//        val bundle = Bundle()
//        bundle.putString("desc", desc)
//        bundle.putString("skill",skill)
//        val fragment: Fragment = DescriptionFragment(desc!!,skill!!)
//        fragment.arguments = bundle
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.details_applied_fragments_container,fragment)
//        transaction.commit()

        binding.descriptionJobCardTextApplied.setOnClickListener {

            removeStyleAll()
            setStyle(binding.descriptionJobCardTextApplied)

            fragmentPositon="1"

            val fragment: Fragment = DescriptionFragment(description,skills)
            fragmentReplace(fragment)
//                     val desc = intent.getStringExtra("description")
//            val skill = intent.getStringExtra("skill")
//
//            val bundle = Bundle()
//            bundle.putString("desc", desc)
//            bundle.putString("skill",skill)

//            val fragment = DescriptionFragment(desc!!,skill!!)
//            fragment.arguments = bundle
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.details_applied_fragments_container,fragment)
//            transaction.commit()
        }
        binding.companyJobCardTextApplied.setOnClickListener {

            removeStyleAll()
            setStyle(binding.companyJobCardTextApplied)

            fragmentPositon="2"

            val fragment:Fragment=CompanyDetailsFragment(companyDertails,skills)
            fragmentReplace(fragment)

//            val fragment: Fragment = CompanyDetailsFragment("","")
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.details_applied_fragments_container,fragment)
//            transaction.commit()
        }
        binding.activitiesJobCardTextApplied.setOnClickListener {

            removeStyleAll()
            setStyle(binding.activitiesJobCardTextApplied)

            fragmentPositon="3"

            val fragment:Fragment= ActivitiesJobAppliedFragment()
            fragmentReplace(fragment)


//            val bundle = Bundle()
//            bundle.putString("AppID", ApplicationId)
//            val fragment: Fragment = ActivitiesJobAppliedFragment()
//            fragment.arguments = bundle
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.details_applied_fragments_container,fragment)
//            transaction.commit()
        }
    }
    private fun getJobDetail(){

        val jobId=intent.getStringExtra("jobId").toString()
        val userId=intent.getStringExtra("ownerUserId").toString()

        Log.d("oooooOwnerUserId", "getJobDetail: "+userId)
        Log.d("oooooJobId", "getJobDetail: "+jobId)

        val getJobDetail = RetrofitBuilder.jobsApis.getJobDetail(jobId,userId)

        getJobDetail.enqueue(object : Callback<JobDetailsDataClass?> {
            override fun onResponse(
                call: Call<JobDetailsDataClass?>,
                response: Response<JobDetailsDataClass?>
            ) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()

                    val response = response.body()

                    Log.d("oooogetjobdetail", "onResponse: "+response)

                    if (response != null) {
                        binding.jobNameDetails.setText(response.jobTitle)
                        binding.locationJobDetails.setText(response.jobLocation)
                        binding.appliedType.setText(response.jobType)

                        Glide.with(applicationContext).load(response.companyImage).placeholder(R.drawable.png_blog)
                            .into(binding.profileJobsDetails)

                        val jobStatus=response.status

                        description = response.jobDescription
                        skills = response.skillsRecq
//                        companyDertails = response.companyDetails
                        companyWeb = response.websiteLink
                        peopleName=response.people.Full_name
                        peopleRole=response.people.Role
                        peopleProfile=response.people.Profile_pic


                        if (fragmentPositon=="1"){
                            Log.d("oooogetjobdetail", "FragDescrip: "+response+fragmentPositon)


                            val fragment: Fragment = DescriptionFragment(description,skills)
                            fragmentReplace(fragment)


                        }
                        else if (fragmentPositon=="2")
                        {
                            val fragment:Fragment=CompanyDetailsFragment(companyDertails,skills)
                            fragmentReplace(fragment)

                        }
                        else if (fragmentPositon=="3"){

                            val fragment:Fragment=ActivitiesFragment(peopleName,peopleRole,peopleProfile)
                            fragmentReplace(fragment)
                        }
//                        if(jobStatus!="Not Applied")
//                        {
//                            Toast.makeText(applicationContext,"omomom",Toast.LENGTH_SHORT).show()
//                            binding.appliedText.setTextColor(ContextCompat.getColor(applicationContext, R.color.light_grey))
//                            binding.appliedText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_grey))
//                            binding.appliedText.isClickable=true
//
//                        }
                    }
                }
                else{
                    progressDialog.dismiss()
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<JobDetailsDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun setStyle(textViewId: TextView)
    {
        textViewId.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        textViewId.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
    }

    private fun removeStyle(textViewId: TextView){
        textViewId.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        textViewId.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
    }
    private fun removeStyleAll(){
        removeStyle(binding.descriptionJobCardTextApplied)
        removeStyle(binding.companyJobCardTextApplied)
        removeStyle(binding.activitiesJobCardTextApplied)
    }
    private fun fragmentReplace(fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragments_container,fragment)
        transaction.commit()
    }
}