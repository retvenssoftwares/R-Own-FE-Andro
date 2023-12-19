package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.JobsForHoteliers
import app.retvens.rown.NavigationFragments.job.GetJobData
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
    private lateinit var jobs_search_posted: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_posted_by_user)

        val back = findViewById<ImageView>(R.id.jobs_back)


        recyclerView = findViewById(R.id.jobPosted_recycler)
        jobs_search_posted = findViewById(R.id.jobs_search_posted)
        recyclerView.layoutManager = LinearLayoutManager(this)
         //recyclerView. //recyclerView.setHasFixedSize(true)

        back.setOnClickListener {
            onBackPressed()

        }

        getJobs()
    }

    private fun getJobs() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getJob = RetrofitBuilder.jobsApis.getIndividualJobs(user_id)

        getJob.enqueue(object : Callback<GetJobData?> {
            override fun onResponse(call: Call<GetJobData?>, response: Response<GetJobData?>) {
                try {
                    if (response.isSuccessful) {
                        val responseData = response.body()!!
                        jobsPostedAdapater = JobsPostedAdapater(applicationContext, responseData.userJobs)
                        recyclerView.adapter = jobsPostedAdapater
                        jobsPostedAdapater.notifyDataSetChanged()

                        jobs_search_posted.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                val filterData = response.body()!!.userJobs.filter { item ->
                                    item.jobTitle.contains(p0.toString(),ignoreCase = true)
                                }

                                jobsPostedAdapater.updateData(filterData)


                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }

                        })
                    } else {
                        Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Handle exceptions that may occur during response processing
                    Toast.makeText(applicationContext, "Error processing response", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<GetJobData?>, t: Throwable) {
                try {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Handle exceptions that may occur during failure handling
                    Toast.makeText(applicationContext, "Error handling failure", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        })

    }
}