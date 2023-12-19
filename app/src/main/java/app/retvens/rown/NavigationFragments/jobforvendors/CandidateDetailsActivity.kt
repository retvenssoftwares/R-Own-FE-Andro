package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.ChatActivity
import app.retvens.rown.DataCollections.JobsCollection.CandidateDataClass
import app.retvens.rown.DataCollections.JobsCollection.StatusDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.JobCantidateDetailsData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomshitStatus
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidateDetailsActivity : AppCompatActivity(),
    BottomshitStatus.OnBottomCTCClickListener {
    @SuppressLint("MissingInflatedId")

    private lateinit  var username:TextView
    private lateinit var type:TextView
    private lateinit var intro:TextView
    private lateinit var exp:TextView
    private lateinit var update_status:TextView
    private lateinit var message:CardView
    private lateinit var jobs_back:ImageView

    lateinit var appId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)

        val name = findViewById<TextView>(R.id.name_Candidate)
        username = findViewById<TextView>(R.id.userName_Candidate)
        update_status = findViewById<TextView>(R.id.update_status)
        val role = findViewById<TextView>(R.id.candidate_role)
        val location = findViewById<TextView>(R.id.location_candidate)
        type = findViewById<TextView>(R.id.candidate_jobType)
        intro = findViewById<TextView>(R.id.candidate_intro)
        exp = findViewById<TextView>(R.id.candidate_experience)
        message = findViewById<CardView>(R.id.message)
        jobs_back = findViewById<ImageView>(R.id.jobs_back)
        val profile = findViewById<ShapeableImageView>(R.id.candidate_profile)


        name.text = intent.getStringExtra("name")
        role.text = intent.getStringExtra("role")
        location.text = "Kanpur"

        val image = intent.getStringExtra("profile")

        Glide.with(applicationContext)
            .load(image)
            .placeholder(R.drawable.svg_user)
            .into(profile)

        val id = intent.getStringExtra("applicationId")
        val userid = intent.getStringExtra("UserId")

        Log.d("xcvbnm", "onCreate: "+id)


        message.setOnClickListener{
            val intent = Intent(this,ChatActivity::class.java)
             startActivity(intent)
        }

        jobs_back.setOnClickListener{
            onBackPressed()
        }

       getCandidate(userid!!,id!!)

        val checkResume = findViewById<CardView>(R.id.checkResume)

        checkResume.setOnClickListener {
           chechResume(userid!!,id!!)
        }

        val updateStatus = findViewById<CardView>(R.id.updateStatus)

        updateStatus.setOnClickListener {
            val bottomSheet = BottomshitStatus()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }

    }

    private fun chechResume(user_id:String,jobId:String) {
        val getResume = RetrofitBuilder.jobsApis.getCandidate(user_id,jobId,)

        getResume.enqueue(object : Callback<JobCantidateDetailsData?> {
            override fun onResponse(call: Call<JobCantidateDetailsData?>, response: Response<JobCantidateDetailsData?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(response.resume), "application/pdf")
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {

                    }
                }
            }

            override fun onFailure(call: Call<JobCantidateDetailsData?>, t: Throwable) {

            }
        })
    }


    private fun getCandidate(user_id:String,jobId:String) {

        val getCandidate = RetrofitBuilder.jobsApis.getCandidate(user_id!!,jobId!!)

        try {
            getCandidate.enqueue(object : Callback<JobCantidateDetailsData?> {
                override fun onResponse(call: Call<JobCantidateDetailsData?>,
                    response: Response<JobCantidateDetailsData?>) {

                    if (response.isSuccessful){ val response = response.body()!!
                        username.text = response.User_name
                        type.text = response.jobType
                        intro.text = response.self_introduction
                        exp.text = response.experience
                        appId=response.applicationId
                        update_status.text=response.status.toString()



                        Log.d("appppId", "onResponse: "+appId)


                    }else{
                        Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JobCantidateDetailsData?>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {

        }

    }

    override fun bottomCTCClick(CTCFrBo: String) {
      updateStatus(CTCFrBo)
        Log.d("bvbbrgjigigigr", "bottomCTCClick: "+CTCFrBo)
    }

    private fun updateStatus(ctcFrBo: String) {

        val id = intent.getStringExtra("applicationId")
        val update = StatusDataClass(ctcFrBo)
        val value=ctcFrBo.toString()
        update_status.text = ctcFrBo.toString()
        val updateStatus = RetrofitBuilder.jobsApis.updateStatus(appId!!,update)

        Log.d("xcvbnmcvbn", "updateStatus: "+update)

        updateStatus.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(call: Call<UpdateResponse?>, response: Response<UpdateResponse?>
            ) {

                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()

                    Log.d("ertyuiop", "onResponse: "+response.message)
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                 }
              }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.d("ertyuiop", "onFailure: "+t.message)
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

}