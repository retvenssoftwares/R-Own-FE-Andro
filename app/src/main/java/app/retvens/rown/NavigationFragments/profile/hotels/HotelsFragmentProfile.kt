package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HotelsFragmentProfile(val userId:String, val isOwner : Boolean) : Fragment() {

    lateinit var recycler : RecyclerView

    lateinit var profileHotelsAdapter: ProfileHotelsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView
    lateinit var addHotel : CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotels_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        addHotel = view.findViewById<CardView>(R.id.addHotel)
        if (!isOwner){
            addHotel.visibility = View.GONE
        }
        addHotel.setOnClickListener{
            startActivity(Intent(context, AddHotelActivity::class.java))
        }

        getHotels()
    }
    private fun getHotels() {

//        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
//        val user_id = sharedPreferences.getString("user_id", "").toString()

        val hotels = RetrofitBuilder.ProfileApis.getProfileHotels(userId)
        hotels.enqueue(object : Callback<List<HotelsName>?> {
            override fun onResponse(
                call: Call<List<HotelsName>?>,
                response: Response<List<HotelsName>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                            try {
                            profileHotelsAdapter = ProfileHotelsAdapter(response.body()!! as ArrayList<HotelsName>, requireContext(), isOwner)
                            recycler.adapter = profileHotelsAdapter
                            profileHotelsAdapter.removeHotelFromList(response.body()!!)
                            profileHotelsAdapter.notifyDataSetChanged()
                               } catch ( e : NullPointerException){
                                notPosted.visibility = View.VISIBLE
                        }
                        } else {
                            notPosted.visibility = View.VISIBLE
                        }
                    } else {
                        addHotel.visibility = View.GONE
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<HotelsName>?>, t: Throwable) {
                addHotel.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })
    }
}