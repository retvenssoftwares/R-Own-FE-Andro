package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.BlogData
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreHotelsFragment : Fragment() {


    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreHotelAdapter : ExploreHotelsAdapter
    private var isLoading:Boolean = false
    private var currentPage = 1
    private lateinit var searchBar:EditText
    private lateinit var progress: ProgressBar
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var hotelList:ArrayList<HotelData> = ArrayList()
    lateinit var empty : TextView
    lateinit var errorImage : ImageView

    private var lastPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_hotels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_hotels_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        searchBar = view.findViewById(R.id.search_explore_hotels)

        empty = view.findViewById(R.id.empty)
        errorImage = view.findViewById(R.id.errorImage)
        progress = view.findViewById(R.id.progress)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

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

        getBlogs()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getBlogs()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getBlogs() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id,currentPage.toString())
        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {
                try {
                    if (isAdded){
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
                                    exploreHotelAdapter = ExploreHotelsAdapter(hotelList, requireContext())
                                    exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                                    exploreHotelAdapter.removeHotelFromList(it.posts)
                                    exploreHotelAdapter.notifyDataSetChanged()

                                    searchBar.addTextChangedListener(object :TextWatcher{
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
                    }
                }catch (e:NullPointerException){
//                    errorImage.visibility = View.VISIBLE
//                    exploreBlogsRecyclerView.visibility = View.GONE
                    Toast.makeText(requireContext(),"No Hotels Yet",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                errorImage.visibility = View.VISIBLE
            }
        })
    }

    private fun searchHotel(letter: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

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
                            searchHotel.addAll(it.posts)
                            exploreHotelAdapter = ExploreHotelsAdapter(searchHotel, requireContext())
                            exploreBlogsRecyclerView.adapter = exploreHotelAdapter
                            exploreHotelAdapter.removeHotelFromList(it.posts)
                            exploreHotelAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }

                    }
                }else{
                    getBlogs()
                }
            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }
}