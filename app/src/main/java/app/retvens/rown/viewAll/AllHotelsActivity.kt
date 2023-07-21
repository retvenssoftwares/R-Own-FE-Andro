package app.retvens.rown.viewAll

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllHotelsActivity : AppCompatActivity() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreHotelAdapter : ExploreHotelsAdapter

    private var isLoading:Boolean = false
    private var currentPage = 1
    private lateinit var progress: ProgressBar
    private var hotelList:ArrayList<HotelData> = ArrayList()
    lateinit var errorImage : ImageView
    private lateinit var searchBar: EditText
    private var lastPage = 1

    private lateinit var progressDialog: Dialog
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hotels)

        findViewById<ImageView>(R.id.profile_backBtn).setOnClickListener { onBackPressed() }

        exploreBlogsRecyclerView = findViewById(R.id.explore_hotels_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(this,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        progress = findViewById(R.id.progress)

        empty = findViewById(R.id.empty)
        searchBar = findViewById(R.id.search_explore_hotels)

        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        exploreBlogsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
//                        if (currentPage > lastPage) {
                        isLoading = false
                        lastPage++
                        getData()
//                        }
                    }
                }


            }
        })

        getHotels()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getHotels()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getHotels() {

        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id,currentPage.toString())
        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {
                progressDialog.dismiss()
                try {
                        if (response.isSuccessful){
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                                val data = response.body()!!
                                data.forEach {

                                    hotelList.addAll(it.posts)
//                                    if (it.posts.size >= 10){
                                    currentPage++
//                                    }
                                    exploreHotelAdapter = ExploreHotelsAdapter(hotelList, this@AllHotelsActivity)
                                    exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                                    exploreHotelAdapter.removeHotelFromList(it.posts)
                                    exploreHotelAdapter.notifyDataSetChanged()

                                    searchBar.addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {

                                        }

                                        override fun onTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {
                                            val letter = p0.toString()
                                            searchHotel(letter)
                                        }

                                        override fun afterTextChanged(p0: Editable?) {

                                        }

                                    })

                                }
                            } else {
//                                errorImage.visibility = View.VISIBLE
                            }
                        } else {
//                            errorImage.visibility = View.VISIBLE
//                            exploreBlogsRecyclerView.visibility = View.GONE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                }catch (e:NullPointerException){
//                    errorImage.visibility = View.VISIBLE
//                    exploreBlogsRecyclerView.visibility = View.GONE
                    Toast.makeText(applicationContext,"No Hotels Yet", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                progressDialog.dismiss()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                if (currentPage ==1) {
                    errorImage.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun searchHotel(letter: String) {

        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val searchHotel = RetrofitBuilder.exploreApis.searchHotel(letter,user_id,"1")

        searchHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.e("response",response.toString())
                    val searchHotel:ArrayList<HotelData> = ArrayList()
                    response.forEach {
                        try {
                            if (it.message == "You have reached the end"){
                                exploreHotelAdapter = ExploreHotelsAdapter(ArrayList(), this@AllHotelsActivity)
                                exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                                exploreHotelAdapter.notifyDataSetChanged()
                            }else{
                                searchHotel.addAll(it.posts)
                                exploreHotelAdapter = ExploreHotelsAdapter(searchHotel, this@AllHotelsActivity)
                                exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                                exploreHotelAdapter.notifyDataSetChanged()
                            }

                            exploreHotelAdapter.removeHotelFromList(it.posts)
                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }

                    }
                }else{
                    getHotels()
                }
            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }
}