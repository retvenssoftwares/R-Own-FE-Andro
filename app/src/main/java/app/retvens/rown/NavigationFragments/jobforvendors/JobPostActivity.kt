package app.retvens.rown.NavigationFragments.jobforvendors

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobResponseDataClass
import app.retvens.rown.DataCollections.JobsCollection.PostJobDataCLass
import app.retvens.rown.DataCollections.JobsCollection.RequestJobDataClass
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobPostActivity : AppCompatActivity(),
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetCTC.OnBottomCTCClickListener, BottomSheetLocation.OnBottomLocationClickListener {

    lateinit var jobTypeEt : TextInputEditText
    lateinit var postMinSalaryEt : TextInputEditText
    lateinit var postMaxSalaryEt : TextInputEditText
    lateinit var postLocationEt : TextInputEditText
    private lateinit var jobTitle:TextInputEditText
    private lateinit var company:TextInputEditText
    private lateinit var jobDescription:TextInputEditText
    private lateinit var jobsSkill:TextInputEditText

    var etType : Int ? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_post)

        //define variables
        jobTitle = findViewById(R.id.post_job_title)
        company = findViewById(R.id.PostComET)
        jobDescription = findViewById(R.id.post_job_description_et)
        jobsSkill = findViewById(R.id.post_job_skills_et)

        jobTypeEt = findViewById(R.id.et_post_job_type)
        postMinSalaryEt = findViewById(R.id.et_post_min_salary)
        postMaxSalaryEt = findViewById(R.id.et_post_max_salary)
        postLocationEt = findViewById(R.id.et_location_job_post)
        postLocationEt.setOnClickListener {
            val bottomSheet = BottomSheetLocation()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            bottomSheet.setOnLocationClickListener(this)
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
        postMaxSalaryEt.setOnClickListener {
            etType = 2
            val bottomSheet = BottomSheetCTC()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }

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
        val min = postMinSalaryEt.text.toString()
        val max = postMaxSalaryEt.text.toString()
        val location = postLocationEt.text.toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val jobsData = PostJobDataCLass(user_id,"1","",title,company,"",type,"",
        "",min,max,location,description,skill)

        val postJob = RetrofitBuilder.jobsApis.postJob(user_id,jobsData)

        postJob.enqueue(object : Callback<JobResponseDataClass?> {
            override fun onResponse(
                call: Call<JobResponseDataClass?>,
                response: Response<JobResponseDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JobResponseDataClass?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        jobTypeEt.setText(jobTypeFrBo)
    }

    override fun bottomCTCClick(CTCFrBo: String) {
        if (etType == 1){
            postMinSalaryEt.setText(CTCFrBo)
        }else{
            postMaxSalaryEt.setText(CTCFrBo)
        }
    }

    override fun bottomLocationClick(LocationFrBo: String) {
        postLocationEt.setText(LocationFrBo)
    }
}