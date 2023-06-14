package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SavedHotelsFragment : Fragment() {

    lateinit var savedHotelsRecyclerView : RecyclerView
    lateinit var savedHotelsAdapter: SavedHotelsAdapter

    lateinit var exploreHotelAdapter : ExploreHotelsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

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
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        savedHotelsRecyclerView = view.findViewById(R.id.savedHotelsRecyclerView)
        savedHotelsRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedHotelsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreHotelsData>(
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
        )

//        savedHotelsAdapter = SavedHotelsAdapter(blogs, requireContext())
//        savedHotelsRecyclerView.adapter = savedHotelsAdapter
//        savedHotelsAdapter.notifyDataSetChanged()
        getHotels()
    }

    private fun getHotels() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getHotel = RetrofitBuilder.ProfileApis.getSaveHotel(user_id,"1")
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
                                    exploreHotelAdapter = ExploreHotelsAdapter(it.posts as ArrayList<HotelData>, requireContext())
                                    savedHotelsRecyclerView.adapter = exploreHotelAdapter
                                    exploreHotelAdapter.removeHotelFromList(it.posts)
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
                }catch (e:NullPointerException){
                    Toast.makeText(requireContext(),"No Hotels Yet", Toast.LENGTH_SHORT).show()
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