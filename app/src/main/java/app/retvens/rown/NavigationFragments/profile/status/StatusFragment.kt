package app.retvens.rown.NavigationFragments.profile.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.profile.polls.PollsAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsData
import app.retvens.rown.R

class StatusFragment : Fragment() {

    lateinit var statusRecycler : RecyclerView
    lateinit var statusAdapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusRecycler = view.findViewById(R.id.statusRecycler)

        statusRecycler.layoutManager = LinearLayoutManager(context)
        statusRecycler.setHasFixedSize(true)

        val blogs = listOf<StatusData>(
            StatusData("title 1"),
            StatusData("title 11 Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "),
            StatusData("title 1"),
            StatusData("title 12 Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "),
            StatusData("title 14"),
        )

        statusAdapter = StatusAdapter(blogs, requireContext())
        statusRecycler.adapter = statusAdapter
        statusAdapter.notifyDataSetChanged()
    }
}