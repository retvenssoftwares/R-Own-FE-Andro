package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.ApplicantDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobDetailsData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsDetailsVendor : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var recyclerView: RecyclerView
    private lateinit var descriptiontext: TextView
    private lateinit var skillreq: TextView
    private lateinit var appliedCandidateAdapter: AppliedCandidateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_details_vendor)

        val cardDescrip = findViewById<RelativeLayout>(R.id.descriptionCard)
        val cardSkill = findViewById<RelativeLayout>(R.id.skillCard)

        val description = findViewById<TextView>(R.id.textdes)
        val skill = findViewById<TextView>(R.id.textskill)

        val view1 = findViewById<RelativeLayout>(R.id.descriptionView)
        val view2 = findViewById<RelativeLayout>(R.id.skillView)


        cardDescrip.setOnClickListener {
           cardDescrip.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color2))
            description.setTextColor(ContextCompat.getColor(this,R.color.white))

            cardSkill.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color))
            skill.setTextColor(ContextCompat.getColor(this,R.color.textcolor))

            view1.visibility = View.VISIBLE
            view2.visibility = View.GONE
        }

        cardSkill.setOnClickListener {
            cardDescrip.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color))
            description.setTextColor(ContextCompat.getColor(this,R.color.textcolor))

            cardSkill.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color2))
            skill.setTextColor(ContextCompat.getColor(this,R.color.white))

            view2.visibility = View.VISIBLE
            view1.visibility = View.GONE
        }

        val title = findViewById<TextView>(R.id.detail_job_designation)
        val location = findViewById<TextView>(R.id.detail_job_location)
        val type = findViewById<TextView>(R.id.details_jobs_type)
//        val descriptiontext = findViewById<TextView>(R.id.JobsDescription)
//        val skillreq = findViewById<TextView>(R.id.JobsSkills)
        val salary = findViewById<TextView>(R.id.details_salary)

        descriptiontext = findViewById(R.id.JobsDescription)
        skillreq = findViewById(R.id.JobsSkills)

        title.text = intent.getStringExtra("title")
        type.text = intent.getStringExtra("type")
        descriptiontext.text = intent.getStringExtra("description")
        skillreq.text = intent.getStringExtra("skills")
        salary.text = intent.getStringExtra("salary")
        val company = intent.getStringExtra("company")
        val locat = intent.getStringExtra("location")

        val jid = intent.getStringExtra("jid")

        Log.d("sdcfvgbhnjmk", "onCreate: "+jid)

        location.setText("$company.$locat")




        recyclerView = findViewById(R.id.applied_candidate_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
         //recyclerView. //recyclerView.setHasFixedSize(true)

        getApplicant(jid)


    }

    private fun getApplicant(jid: String?) {

        val getApplicant = RetrofitBuilder.jobsApis.getApplicant(jid!!)

        getApplicant.enqueue(object : Callback<JobDetailsData?> { override fun onResponse(call: Call<JobDetailsData?>, response: Response<JobDetailsData?>
            ) {
                if (response.isSuccessful){

                    descriptiontext.text=response.body()?.jobDescription
                    skillreq.text=response.body()?.skillsRecq

                    Log.d("sucessssss", "onResponse: "+response.body())


                    appliedCandidateAdapter = AppliedCandidateAdapter(applicationContext,response.body()!!.jobApplicants)
                    recyclerView.adapter = appliedCandidateAdapter
                    appliedCandidateAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<JobDetailsData?>, t: Throwable) {

                Log.d("failure", "onFailure: "+t.message)
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}