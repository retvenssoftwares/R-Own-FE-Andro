package app.retvens.rown.NavigationFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.eventsForHoteliers.EventCategoriesAdapter
import app.retvens.rown.R


class EventFragment : Fragment() {

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var eventCategoriesAdapter: EventCategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)

    }
}