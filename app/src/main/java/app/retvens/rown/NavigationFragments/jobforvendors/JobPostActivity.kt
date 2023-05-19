package app.retvens.rown.NavigationFragments.jobforvendors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobResponseDataClass
import app.retvens.rown.DataCollections.JobsCollection.PostJobDataCLass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomSheetCompany
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetJobTitle
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.databinding.ActivityJobPostBinding
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobPostActivity : AppCompatActivity(),
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetCTC.OnBottomCTCClickListener,
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener,
    BottomSheetJobTitle.OnBottomJobTitleClickListener,
    BottomSheetCompany.OnBottomCompanyClickListener {

    lateinit var binding:ActivityJobPostBinding

    lateinit var jobTypeEt : TextInputEditText
    lateinit var postMinSalaryEt : TextInputEditText
    lateinit var postLocationEt : TextInputEditText
    private lateinit var jobTitle:TextInputEditText
    private lateinit var company:TextInputEditText
    private lateinit var jobDescription:TextInputEditText
    private lateinit var jobsSkill:TextInputEditText

    var etType : Int ? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityJobPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //define variables
        jobTitle = findViewById(R.id.post_job_title)
        company = findViewById(R.id.postCompaET)
        jobDescription = findViewById(R.id.post_job_description_et)
        jobsSkill = findViewById(R.id.post_job_skills_et)

        jobTypeEt = findViewById(R.id.et_post_job_type)
        postMinSalaryEt = findViewById(R.id.et_post_min_salary)
        postLocationEt = findViewById(R.id.et_location_job_post)
        postLocationEt.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        jobTypeEt.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }
        postMinSalaryEt.setOnClickListener {
            etType = 1
            val bottomSheet = BottomSheetCTC()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }

        jobTitle.setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        company.setOnClickListener {
            val bottomSheet = BottomSheetCompany()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCompany.Company_TAG)}
            bottomSheet.setOnCompanyClickListener(this)
        }

       /* postMaxSalaryEt.setOnClickListener {
            etType = 2
            val bottomSheet = BottomSheetCTC()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }*/

        val post = findViewById<CardView>(R.id.card_post_job)

        post.setOnClickListener {
            postJob()
        }

    }

    private fun postJob() {

        val title = jobTitle.text.toString()
        val company = company.text.toString()
        val description = jobDescription.text.toString()
        val skill = jobsSkill.text.toString()
        val type = jobTypeEt.text.toString()
        val expected = postMinSalaryEt.text.toString()
        val location = postLocationEt.text.toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val jobsData = PostJobDataCLass(user_id,"1","",title,company,"",type,"",
        "",expected,location,description,skill)

        val postJob = RetrofitBuilder.jobsApis.postJob(user_id,jobsData)

       postJob.enqueue(object : Callback<UpdateResponse?> {
           override fun onResponse(
               call: Call<UpdateResponse?>,
               response: Response<UpdateResponse?>
           ) {
               if (response.isSuccessful){
                   val response = response.body()!!
                   Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
               }else{
                   Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
               }
           }

           override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
               Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
           }
       })

    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        jobTypeEt.setText(jobTypeFrBo)
    }

    override fun bottomCTCClick(CTCFrBo: String) {
            postMinSalaryEt.setText(CTCFrBo)
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        postLocationEt.setText(CountryStateCityFrBo)
    }
    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        jobTitle.setText(jobTitleFrBo)
    }
    override fun bottomLocationClick(CompanyFrBo: String) {
        company.setText(CompanyFrBo)
    }
}