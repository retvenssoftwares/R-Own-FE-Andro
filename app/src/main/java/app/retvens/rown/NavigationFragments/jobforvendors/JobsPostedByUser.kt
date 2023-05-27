package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.JobsForHoteliers
import app.retvens.rown.NavigationFragments.job.JobsPostedAdapater
import app.retvens.rown.NavigationFragments.job.RecentJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsPostedByUser : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobsPostedAdapater: JobsPostedAdapater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_posted_by_user)

        val back = findViewById<ImageView>(R.id.jobs_back)


        recyclerView = findViewById(R.id.jobPosted_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        back.setOnClickListener {

        }

        getJobs()
    }

    private fun getJobs() {

        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getJob = RetrofitBuilder.jobsApis.getJobs(user_id)

        getJob.enqueue(object : Callback<List<JobsData>?> {
            override fun onResponse(
                call: Call<List<JobsData>?>,
                response: Response<List<JobsData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    jobsPostedAdapater = JobsPostedAdapater(applicationContext, response)
                    recyclerView.adapter = jobsPostedAdapater
                    jobsPostedAdapater.notifyDataSetChanged()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JobsData>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}