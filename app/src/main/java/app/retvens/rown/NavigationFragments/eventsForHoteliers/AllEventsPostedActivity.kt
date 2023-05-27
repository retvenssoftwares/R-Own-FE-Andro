package app.retvens.rown.NavigationFragments.eventsForHoteliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAllEventsPostedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllEventsPostedActivity : AppCompatActivity() {
    lateinit var binding : ActivityAllEventsPostedBinding

    lateinit var allEventsPostedAdapter: AllEventsPostedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllEventsPostedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBackBtn.setOnClickListener { onBackPressed() }

        getUserEvents()
    }
    private fun getUserEvents() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        val getEvents = RetrofitBuilder.EventsApi.getEventByUserId(user_id)
        getEvents.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (response.isSuccessful) {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE

                    Log.e("respo", " = $user_id")

                    Log.d("res", response.body().toString())

                    if (response.body()!!.isNotEmpty()) {
                        allEventsPostedAdapter =
                            AllEventsPostedAdapter(response.body()!!, this@AllEventsPostedActivity)
                        binding.recyclerView.adapter = allEventsPostedAdapter
                        allEventsPostedAdapter.notifyDataSetChanged()
                        Log.d("respo", " = $user_id")
                        Log.d("res", response.body().toString())
                    } else {
                        binding.empty.text = "No event Posted"
                        binding.empty.visibility = View.VISIBLE
                    }
                } else {
                    binding.empty.text = "${response.code()}  ${response.message()}"
                    binding.empty.visibility = View.VISIBLE
//                    Toast.makeText(applicationContext,"${response.code()}  ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OnGoingEventsData>?>, t: Throwable) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE

                binding.empty.text = "Try Again - Check your Internet"
                binding.empty.visibility = View.VISIBLE
                Toast.makeText(applicationContext,"${t.localizedMessage.toString()}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}