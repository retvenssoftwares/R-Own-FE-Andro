package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.eventsForHoteliers.AddEventActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.AllEventsPostedActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.CreateEventActivity
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesAdapter
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import com.google.android.material.textfield.TextInputEditText

class EventFragmentForHoteliers : Fragment() {

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

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

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)

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
    }
    private fun getCategories() {
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.setHasFixedSize(true)

        val blogs = listOf<EventCategoriesData>(
            EventCategoriesData("Title 1"),
            EventCategoriesData("Title 2"),
            EventCategoriesData("Title 3"),
            EventCategoriesData("Title 23"),
        )

        eventCategoriesAdapter = EventCategoriesAdapter(blogs, requireContext())
        categoryRecyclerView.adapter = eventCategoriesAdapter
        eventCategoriesAdapter.notifyDataSetChanged()
    }
}