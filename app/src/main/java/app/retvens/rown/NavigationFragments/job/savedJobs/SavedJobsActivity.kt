package app.retvens.rown.NavigationFragments.job.savedJobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.explorejob.AppliedJobAdapter
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivitySavedJobsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedJobsActivity : AppCompatActivity() {

    lateinit var binding:ActivitySavedJobsBinding
    lateinit var savedJobsAdapter: SavedJobsAdapter
    private var savedJobsList:ArrayList<SavedJob> = ArrayList()
    lateinit var savedJobsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationsBackBtn.setOnClickListener { onBackPressed() }

        binding.savedJobRecycler.layoutManager = LinearLayoutManager(this)
        //binding.savedJobRecycler. //recyclerView.setHasFixedSize(true)

        savedJobsAdapter = SavedJobsAdapter(savedJobsList, this)
        binding.savedJobRecycler.adapter = savedJobsAdapter
        savedJobsAdapter.notifyDataSetChanged()
        savedJobs()
    }


    private fun savedJobs() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val saves = RetrofitBuilder.jobsApis.getSavedJobs(user_id,1)
        saves.enqueue(object : Callback<List<SavedJobsData>?> {
            override fun onResponse(call: Call<List<SavedJobsData>?>, response: Response<List<SavedJobsData>?>) {
                try {
                    if (response.isSuccessful) {

                        val response = response.body()!!
                        val original = response.toList()

                        response.forEach { savedJobsData ->
                            savedJobsList.addAll(savedJobsData.jobs)
                            savedJobsAdapter.notifyDataSetChanged()
                        }


                        } else {

                        }
                    }catch (e:NullPointerException){
                    Toast.makeText(applicationContext, "No More Jobs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SavedJobsData>?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })


        }

}