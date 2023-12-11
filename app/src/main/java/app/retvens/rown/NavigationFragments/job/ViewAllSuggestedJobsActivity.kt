package app.retvens.rown.NavigationFragments.job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllSuggestedJobsActivity : AppCompatActivity() {

    lateinit var suggestedRecycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_suggested_jobs)

        findViewById<ImageButton>(R.id.notifications_backBtn).setOnClickListener { onBackPressed() }

        suggestedRecycler = findViewById(R.id.suggested_job_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(this)
        //suggestedRecycler. //recyclerView.setHasFixedSize(true)

        if (intent.getStringExtra("recent") == "Recent Jobs"){
            findViewById<TextView>(R.id.title).text = "Recent Jobs"
        }
//        getJobs()

    }

//    private fun getJobs() {
//        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
//        val user_id = sharedPreferences.getString("user_id", "").toString()
//
//        val getJob = RetrofitBuilder.jobsApis.getJobs(user_id)
//
//        getJob.enqueue(object : Callback<List<JobsData>?>,
//            SuggestedJobAdapter.JobSavedClickListener {
//            override fun onResponse(
//                call: Call<List<JobsData>?>,
//                response: Response<List<JobsData>?>
//            ) {
//                if (response.isSuccessful){
//                    val response = response.body()!!
//
//                    if (intent.getStringExtra("recent") == "Recent Jobs"){
//                        val recentJobAdapter = RecentJobAdapter(this@ViewAllSuggestedJobsActivity, response)
//                        suggestedRecycler.adapter = recentJobAdapter
//                        recentJobAdapter.notifyDataSetChanged()
//                    } else {
//                        val suggestedJobAdapter =
//                            SuggestedAllJobAdapter(this@ViewAllSuggestedJobsActivity, response)
//                        suggestedRecycler.adapter = suggestedJobAdapter
//                        suggestedJobAdapter.notifyDataSetChanged()
//                    }
//                }else{
//                    Toast.makeText(applicationContext,response.code().toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<JobsData>?>, t: Throwable) {
//                Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onJobSavedClick(job: JobsData) {
//
//            }
//        })
//    }

}