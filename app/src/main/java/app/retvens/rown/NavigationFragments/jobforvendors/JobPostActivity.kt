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
import app.retvens.rown.bottomsheet.*
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
    BottomSheetHotels.OnBottomCompanyClickListener {

    lateinit var binding:ActivityJobPostBinding

    lateinit var jobTypeEt : TextInputEditText
    lateinit var postMinSalaryEt : TextInputEditText
    lateinit var postLocationEt : TextInputEditText
    private lateinit var jobTitle:TextInputEditText
    private lateinit var company:TextInputEditText
    private lateinit var jobDescription:TextInputEditText
    private lateinit var jobsSkill:TextInputEditText
    private lateinit var designation:TextInputEditText
    private  var hotelsId:String? = null

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
        designation = findViewById(R.id.post_Designation_title)

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

        designation.setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        company.setOnClickListener {
            val bottomSheet = BottomSheetHotels()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetHotels.Company_TAG)}
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

            if (designation.length() < 3){
                binding.postDesignationTitle.error = "Enter Proper details"
            }else if (jobTitle.length() < 3){
                binding.jobTitleLayout.error = "Enter Proper details"
            }else if (jobTypeEt.length() < 3){
                binding.typeLayout.error = "Enter Proper details"
            }else if (postMinSalaryEt.length() <3){
                binding.expectedLayout.error = "Enter Proper details"
            }else if(jobDescription.length() < 3){
                binding.descLayout.error = "Enter Proper details"
            }else if (jobsSkill.length() < 3){
                binding.skillLayout.error = "Enter Proper details"
            }else{
                postJob()
            }

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
        val designation = designation.text.toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val jobsData = PostJobDataCLass(user_id,hotelsId!!,"1","",title,company,"",type,designation,
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
                   Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
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

    override fun selectlocation(latitude: String, longitude: String) {
        TODO("Not yet implemented")
    }

    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        designation.setText(jobTitleFrBo)
    }

    override fun bottomLocationClick(hotelName: String, hotelId: String) {
        company.setText(hotelName)
        hotelsId = hotelId
    }


}