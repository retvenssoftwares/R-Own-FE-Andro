package app.retvens.rown.NavigationFragments.profile.events

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventsProfileFragment(val userId:String, val isOwner : Boolean) : Fragment() {

    lateinit var eventRecyclerView: RecyclerView

    lateinit var eventsProfileAdapter: EventsProfileAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView

    private lateinit var progressDialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        progressDialog = Dialog(requireContext())
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        getEvents()

    }
    private fun getEvents() {

//        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
//        val user_id = sharedPreferences.getString("user_id", "").toString()
// "-GSomAJoY"
        val events = RetrofitBuilder.ProfileApis.getProfileEvents(user_id = userId)
        events.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                progressDialog.dismiss()
                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            try{

                            eventsProfileAdapter =
                                EventsProfileAdapter(response.body()!!, requireContext())
                            eventRecyclerView.adapter = eventsProfileAdapter
                            eventsProfileAdapter.notifyDataSetChanged()
                        } catch (e : NullPointerException){
                                notPosted.visibility = View.VISIBLE
                        }
                        } else {
                            notPosted.visibility = View.VISIBLE
                        }
                    } else {
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<OnGoingEventsData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
                progressDialog.dismiss()
            }
        })
    }
}