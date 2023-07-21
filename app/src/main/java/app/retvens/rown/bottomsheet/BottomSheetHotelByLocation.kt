package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.BottomSheetHotelAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetHotelDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetHotelByLocation(val location:String) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetHotelAdapter: BottomSheetHotelAdapter
    private var currentPage = 1
    private var isLoading:Boolean = false
    private lateinit var progress: ProgressBar
    private var hotelList:ArrayList<HotelData> = ArrayList()
    private lateinit var adapter:BottomSheetHotelAdapter
    private lateinit var searchBar:EditText
    companion object {
        const val CountryStateCity_TAG = "BottomSheetDailog"
    }

    var mListener: OnBottomCountryStateCityClickListener ? = null
    fun setOnCountryStateCityClickListener(listener: OnBottomCountryStateCityClickListener?){
        mListener = listener
    }

    interface OnBottomCountryStateCityClickListener{
        fun bottomCountryStateCityClick(CountryStateCityFrBo : String)
    }

    fun newInstance(): BottomSheetWhatToPost? {
        return BottomSheetWhatToPost()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_hotel_by_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            getHotel()

        recyclerView = view.findViewById(R.id.hotel_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        progress = view.findViewById(R.id.progress)

        adapter = BottomSheetHotelAdapter(requireContext(),hotelList)
        recyclerView.adapter = adapter

        searchBar = view.findViewById(R.id.search_explore_hotels)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                        isLoading = false
                        getData()

                    }
                }


            }
        })

    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getHotel()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getHotel() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.exploreApis.getExploreHotels(user_id,currentPage.toString())

        getHotel.enqueue(object : Callback<List<ExploreHotelData>?> {
            override fun onResponse(
                call: Call<List<ExploreHotelData>?>,
                response: Response<List<ExploreHotelData>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    currentPage++
                    try {
                        response.forEach {
                            hotelList.addAll(it.posts)
                            adapter.notifyDataSetChanged()
                        }
                    }catch (e:NullPointerException){
                        Toast.makeText(requireContext(),"No More Hotels",Toast.LENGTH_SHORT).show()
                    }

                    searchBar.addTextChangedListener(object :TextWatcher{
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        }

                        override fun afterTextChanged(p0: Editable?) {
                            val letter = p0.toString()
                            searchHotel(letter)
                        }
                    })

                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                Log.e("error",t.message.toString())
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
                            if (it.message == "You have reached the end"){
                                adapter = BottomSheetHotelAdapter(requireContext(),ArrayList())
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }else{
                                searchHotel.addAll(it.posts)
                                adapter = BottomSheetHotelAdapter(requireContext(),searchHotel)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }

                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }

                    }
                }else{
                    getHotel()
                }
            }

            override fun onFailure(call: Call<List<ExploreHotelData>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }
}