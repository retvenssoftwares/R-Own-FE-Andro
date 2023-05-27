package app.retvens.rown.NavigationFragments.eventForUsers.allEvents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivitySeeAllEventsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeeAllEventsActivity : AppCompatActivity() {

    lateinit var binding:ActivitySeeAllEventsBinding

    lateinit var seeAllEventsAdapter: SeeAllEventsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAllEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        val idCategory = intent.getStringExtra("id")
        if (idCategory == null){
            getAllEvents()
        } else {
            val categoryName = intent.getStringExtra("name")
            binding.topTitle.text = categoryName
            getEventsByCategory(idCategory)
        }
    }

    private fun getAllEvents() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getAll = RetrofitBuilder.EventsApi.getAllEvents(user_id)
        getAll.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (response.isSuccessful){
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE

                    if (response.body()!!.isNotEmpty()) {
                    seeAllEventsAdapter = SeeAllEventsAdapter(response.body()!!, this@SeeAllEventsActivity)
                    binding.eventRecyclerView.adapter = seeAllEventsAdapter
                    seeAllEventsAdapter.notifyDataSetChanged()

                    binding.searchCommunity.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val original = response.body()!!.toList()
                            val filter = original.filter { searchUser ->
                                searchUser.event_title.contains(s.toString(), ignoreCase = true)
//                                searchUser.event_description.contains(s.toString(),ignoreCase = true)
                            }
                            seeAllEventsAdapter.searchView(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

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

    private fun getEventsByCategory(idCategory: String) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.eventRecyclerView.setHasFixedSize(true)

        val getEventByCategory = RetrofitBuilder.EventsApi.getEventsByCategory(user_id, idCategory)
        getEventByCategory.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (response.isSuccessful){
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE

                    if (response.body()!!.isNotEmpty()) {
                    seeAllEventsAdapter = SeeAllEventsAdapter(response.body()!!, this@SeeAllEventsActivity)
                    binding.eventRecyclerView.adapter = seeAllEventsAdapter
                    seeAllEventsAdapter.notifyDataSetChanged()

                    binding.searchCommunity.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val original = response.body()!!.toList()
                            val filter = original.filter { searchUser ->
                                searchUser.event_title.contains(s.toString(), ignoreCase = true)
//                                searchUser.price.contains(s.toString(),ignoreCase = true)
                            }
                            seeAllEventsAdapter.searchView(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
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