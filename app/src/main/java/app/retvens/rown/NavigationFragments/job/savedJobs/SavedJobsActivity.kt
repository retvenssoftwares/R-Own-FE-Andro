package app.retvens.rown.NavigationFragments.job.savedJobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivitySavedJobsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedJobsActivity : AppCompatActivity() {

    lateinit var binding:ActivitySavedJobsBinding
    lateinit var savedJobsAdapter: SavedJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationsBackBtn.setOnClickListener { onBackPressed() }

        binding.savedJobRecycler.layoutManager = LinearLayoutManager(this)
        binding.savedJobRecycler.setHasFixedSize(true)
        savedJobs()
    }

    private fun savedJobs() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val saves = RetrofitBuilder.jobsApis.getSavedJobs("-GSomAJoY")
        saves.enqueue(object : Callback<SavedJobsData?> {
            override fun onResponse(call: Call<SavedJobsData?>, response: Response<SavedJobsData?>) {
                if (response.isSuccessful) {

                    Log.d("job", response.body()!!.toString())
                    savedJobsAdapter = SavedJobsAdapter( response.body()!!.jobs, applicationContext)
                    binding.savedJobRecycler.adapter = savedJobsAdapter
                    savedJobsAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SavedJobsData?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}