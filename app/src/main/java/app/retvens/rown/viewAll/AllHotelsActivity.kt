package app.retvens.rown.viewAll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllHotelsActivity : AppCompatActivity() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreHotelAdapter : ExploreHotelsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hotels)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener { onBackPressed() }

        exploreBlogsRecyclerView = findViewById(R.id.explore_hotels_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(this,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)


        empty = findViewById(R.id.empty)

        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout)

        getBlogs()
    }

    private fun getBlogs() {

        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id,"1")
        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {

                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {
                                exploreHotelAdapter = ExploreHotelsAdapter(it.posts, this@AllHotelsActivity)
                                exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                                exploreHotelAdapter.notifyDataSetChanged()
                            }
                        } else {
                            empty.visibility = View.VISIBLE
                        }
                    } else {
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                empty.visibility = View.VISIBLE
            }
        })
    }
}