package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventCategoryActivity
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.allEvents.SeeAllEventsActivity
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.eventsForHoteliers.AddEventActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.AllEventsPostedActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventFragmentForHoteliers : Fragment() {

    lateinit var allRecyclerView: RecyclerView
    lateinit var allEventsAdapter: AllEventsAdapter

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var search : EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_for_hoteliers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        shimmerFrameLayout = view.findViewById(R.id.shimmer_category)

        search = view.findViewById(R.id.search_community)


        allRecyclerView = view.findViewById(R.id.blogsRecyclerView)
        allRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        allRecyclerView.setHasFixedSize(true)
        getAllEvents()

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)

        view.findViewById<TextView>(R.id.viewAllItem).setOnClickListener {
            startActivity(Intent(context, SeeAllEventsActivity::class.java))
        }
        view.findViewById<TextView>(R.id.viewAllCategory).setOnClickListener {
            startActivity(Intent(context, AllEventCategoryActivity::class.java))
        }

        view.findViewById<CardView>(R.id.addEvent).setOnClickListener {

            val dialogLanguage = Dialog(requireContext())
            dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogLanguage.setContentView(R.layout.bottom_sheet_add_event)
            dialogLanguage.setCancelable(true)

            dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogLanguage.window?.setGravity(Gravity.BOTTOM)
            dialogLanguage.show()

            dialogLanguage.findViewById<LinearLayout>(R.id.add).setOnClickListener {
                startActivity(Intent(context, AddEventActivity::class.java))
                dialogLanguage.dismiss()
            }
            dialogLanguage.findViewById<LinearLayout>(R.id.myEvent).setOnClickListener {
                startActivity(Intent(context, AllEventsPostedActivity::class.java))
                dialogLanguage.dismiss()
            }
        }
        getCategories()
        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            getAllEvents()
            getCategories()

            refresh.isRefreshing = false
        }

    }

    private fun getAllEvents() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getAll = RetrofitBuilder.EventsApi.getAllEvents(user_id)
        getAll.enqueue(object : Callback<List<OnGoingEventsData>?> {
            override fun onResponse(
                call: Call<List<OnGoingEventsData>?>,
                response: Response<List<OnGoingEventsData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        allEventsAdapter = AllEventsAdapter(response.body()!!, requireContext())
                        allRecyclerView.adapter = allEventsAdapter
                        allEventsAdapter.notifyDataSetChanged()

                        search.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                val original = response.body()!!.toList()
                                val filter = original.filter { searchUser ->
                                    searchUser.event_title.contains(s.toString(), ignoreCase = true)
//                                searchUser.event_description.contains(s.toString(),ignoreCase = true)
                                }
                                allEventsAdapter.searchView(filter)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })

                    } else {
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            override fun onFailure(call: Call<List<OnGoingEventsData>?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun getCategories() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.setHasFixedSize(true)

        val getEventCategories = RetrofitBuilder.EventsApi.getEventCategory()
        getEventCategories.enqueue(object : Callback<List<ViewAllCategoriesData>?> {
            override fun onResponse(
                call: Call<List<ViewAllCategoriesData>?>,
                response: Response<List<ViewAllCategoriesData>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {

                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        eventCategoriesAdapter =
                            EventCategoriesAdapter(response.body()!!, requireContext())
                        categoryRecyclerView.adapter = eventCategoriesAdapter
                        eventCategoriesAdapter.notifyDataSetChanged()
                    } else{
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<ViewAllCategoriesData>?>, t: Throwable) {
                if (isAdded){
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE

                    Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}