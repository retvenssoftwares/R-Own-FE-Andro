package app.retvens.rown.NavigationFragments.eventForUsers

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAllEventCategoryBinding
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllEventCategoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityAllEventCategoryBinding

    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllEventCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        getCategories()





     }
    private fun getCategories() {
        binding.eventCategoryRecyclerView.layoutManager = LinearLayoutManager(this)
        //binding.eventCategory //recyclerView. //recyclerView.setHasFixedSize(true)

        val getEventCategories = RetrofitBuilder.EventsApi.getEventCategory()
        getEventCategories.enqueue(object : Callback<List<ViewAllCategoriesData>?> {
            override fun onResponse(
                call: Call<List<ViewAllCategoriesData>?>,
                response: Response<List<ViewAllCategoriesData>?>
            ) {
                    if (response.isSuccessful) {

                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                        eventCategoriesAdapter =
                            EventCategoriesAdapter(response.body()!!, this@AllEventCategoryActivity)
                        binding.eventCategoryRecyclerView.adapter = eventCategoriesAdapter
                        eventCategoriesAdapter.notifyDataSetChanged()
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

            @SuppressLint("SuspiciousIndentation")
            override fun onFailure(call: Call<List<ViewAllCategoriesData>?>, t: Throwable) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE

                binding.empty.text = "Try Again - Check your Internet"
                binding.empty.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}