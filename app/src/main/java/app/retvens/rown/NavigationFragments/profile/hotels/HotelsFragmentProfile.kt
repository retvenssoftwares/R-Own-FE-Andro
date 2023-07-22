package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import app.retvens.rown.utils.serverCode
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HotelsFragmentProfile(val userId:String, val isOwner : Boolean, val username : String) : Fragment() {

    lateinit var recycler : RecyclerView
    lateinit var profileHotelsAdapter: ProfileHotelsAdapter
    private var hotelList:ArrayList<HotelsName> = ArrayList()
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


        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val User_id = sharedPreferences.getString("user_id", "").toString()

        Log.e("user",User_id)
        Log.e("user",userId)

        val hotels = RetrofitBuilder.ProfileApis.getProfileHotels(userId,User_id)
        hotels.enqueue(object : Callback<List<HotelsName>?> {
            override fun onResponse(
                call: Call<List<HotelsName>?>,
                response: Response<List<HotelsName>?>
            ) {
                if (isAdded) {
                    serverCode = response.code()
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                            try {
                            val response = response.body()!!
                            response.forEach {
                                if (it.display_status == "1"){
                                    hotelList.add(it)
                                }

                            }
                            profileHotelsAdapter = ProfileHotelsAdapter(hotelList, requireContext(), isOwner)
                            recycler.adapter = profileHotelsAdapter
                            profileHotelsAdapter.removeHotelFromList(hotelList)
                            profileHotelsAdapter.notifyDataSetChanged()
                               } catch ( e : NullPointerException){
                                notPosted.visibility = View.VISIBLE
                                empty.visibility = View.VISIBLE
                                if (isOwner){
                                    empty.text = "You have not posted anything yet."
                                } else {
                                    empty.text = "$username have not posted anything yet."
                                }
                        }
                        } else {
                            notPosted.visibility = View.VISIBLE
                            empty.visibility = View.VISIBLE
                            if (isOwner){
                                empty.text = "You have not posted anything yet."
                            } else {
                                empty.text = "$username have not posted anything yet."
                            }
                        }
                    } else {
                        Log.e("error",response.code().toString())
                        addHotel.visibility = View.GONE
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<HotelsName>?>, t: Throwable) {
                Log.e("error",t.message.toString())
                addHotel.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })
    }
}