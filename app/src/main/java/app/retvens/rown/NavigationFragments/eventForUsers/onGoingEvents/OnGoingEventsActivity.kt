package app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityOnGoingEventsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnGoingEventsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOnGoingEventsBinding
    lateinit var onGoingEventsAdapter: OnGoingEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        binding.onGoingRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.onGoingRecyclerView.setHasFixedSize(true)
        onGoingEvents()

    }
    private fun onGoingEvents() {
        val getOnGoingEvents = RetrofitBuilder.EventsApi.getOnGoingEvents()
        getOnGoingEvents.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                    if (response.isSuccessful){
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {

                        onGoingEventsAdapter = OnGoingEventsAdapter(response.body()!!, this@OnGoingEventsActivity)
                        binding.onGoingRecyclerView.adapter = onGoingEventsAdapter
                        onGoingEventsAdapter.notifyDataSetChanged()

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
                    Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}