package app.retvens.rown.NavigationFragments.profile.setting.saved.hotels

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SavedHotelsFragment : Fragment() {

    lateinit var savedHotelsRecyclerView : RecyclerView
    lateinit var savedHotelsAdapter: SavedHotelsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var empty : TextView
    lateinit var emptyImage : ImageView

    private var hotelList:ArrayList<Hotel> = ArrayList()
    private lateinit var progress: ProgressBar

    private var currentPage = 1
    private var isLoading:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_hotels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty = view.findViewById(R.id.empty)
        emptyImage = view.findViewById(R.id.emptyImage)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        progress = view.findViewById(R.id.progress)

        savedHotelsRecyclerView = view.findViewById(R.id.savedHotelsRecyclerView)
        savedHotelsRecyclerView.layoutManager = GridLayoutManager(context,2)
        //savedHotels //recyclerView. //recyclerView.setHasFixedSize(true)

        savedHotelsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
//                            lastPage++
                            getData()
//                        }
                    }
                }


            }
        })

        getSavedHotels()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getSavedHotels()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getSavedHotels() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.ProfileApis.getSaveHotel(user_id,"1")
        getHotel.enqueue(object : Callback<List<SavedHotelsDataItem>?> {
            override fun onResponse(
                call: Call<List<SavedHotelsDataItem>?>,
                response: Response<List<SavedHotelsDataItem>?>
            ) {
                try {
                    if (isAdded) {
                        if (response.isSuccessful) {
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                                val data = response.body()
                                data!!.forEach{

                                    hotelList.addAll(it.hotels)
//                                    if (it.posts.size >= 10){
                                    currentPage++
//                                    }
                                    savedHotelsAdapter = SavedHotelsAdapter(hotelList , requireContext())
                                    savedHotelsRecyclerView.adapter = savedHotelsAdapter
                                    savedHotelsAdapter.removeHotelFromList(it.hotels)
                                    savedHotelsAdapter.notifyDataSetChanged()
                                }
                            } else {
//                                empty.visibility = View.VISIBLE
                                emptyImage.visibility = View.VISIBLE
                            }
                        } else {
//                            Toast.makeText(requireContext(),"${response.message()} ${response.code()} ${response.body().toString()}", Toast.LENGTH_SHORT).show()
                            Log.d("tag", response.body().toString())
                        }
                    }
                }catch (e:NullPointerException){
                        emptyImage.visibility = View.VISIBLE
                    Log.d("tag", e.toString())
                }
            }

            override fun onFailure(call: Call<List<SavedHotelsDataItem>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
//                empty.visibility = View.VISIBLE
                Log.d("tag", t.localizedMessage.toString())
//                Toast.makeText(requireContext(),t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}