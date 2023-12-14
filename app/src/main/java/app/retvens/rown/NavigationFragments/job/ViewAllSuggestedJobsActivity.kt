package app.retvens.rown.NavigationFragments.job

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.GetAllJobsData
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllSuggestedJobsActivity : AppCompatActivity() {

    lateinit var suggestedRecycler : RecyclerView
    lateinit var shimmerLayout: LinearLayout
    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_suggested_jobs)

        findViewById<ImageButton>(R.id.notifications_backBtn).setOnClickListener { onBackPressed() }

        shimmerLayout = findViewById(R.id.shimmer_layout_tasks)
        suggestedRecycler = findViewById(R.id.suggested_job_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(this)
        //suggestedRecycler. //recyclerView.setHasFixedSize(true)

        if (intent.getStringExtra("recent") == "Recent Jobs"){
            shimmerLayout.visibility = View.VISIBLE
            findViewById<TextView>(R.id.title).text = "Recent Jobs"
        }else{
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
        }
        getJobs()

    }

    private fun getJobs() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getJob = RetrofitBuilder.jobsApis.getJobs(user_id)

        getJob.enqueue(object : Callback<List<GetAllJobsData>?>,
            SuggestedJobAdapter.JobSavedClickListener {
            override fun onResponse(
                call: Call<List<GetAllJobsData>?>,
                response: Response<List<GetAllJobsData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    if (intent.getStringExtra("recent") == "Recent Jobs"){
                        val recentJobAdapter = RecentJobAdapter(this@ViewAllSuggestedJobsActivity, response)
                        shimmerLayout.visibility = View.GONE
                        suggestedRecycler.adapter = recentJobAdapter
                        recentJobAdapter.notifyDataSetChanged()
                    } else {
                        val suggestedJobAdapter =
                            SuggestedAllJobAdapter(this@ViewAllSuggestedJobsActivity, response)
                        progressDialog.dismiss()
                        suggestedRecycler.adapter = suggestedJobAdapter
                        suggestedJobAdapter.notifyDataSetChanged()
                    }
                }else{
                    Toast.makeText(applicationContext,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetAllJobsData>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onJobSavedClick(job: JobsData) {

            }
        })
    }

}