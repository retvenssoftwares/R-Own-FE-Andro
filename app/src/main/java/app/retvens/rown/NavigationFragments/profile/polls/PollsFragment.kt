package app.retvens.rown.NavigationFragments.profile.polls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R


class PollsFragment : Fragment() {

    lateinit var pollsRecyclerView: RecyclerView
    lateinit var pollsAdapter: PollsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pollsRecyclerView = view.findViewById(R.id.pollsRecycler)
        pollsRecyclerView.layoutManager = LinearLayoutManager(context)
        pollsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<PollsData>(
            PollsData("title 1"),
            PollsData("title 11"),
            PollsData("title 12"),
            PollsData("title 14"),
        )

        pollsAdapter = PollsAdapter(blogs, requireContext())
        pollsRecyclerView.adapter = pollsAdapter
        pollsAdapter.notifyDataSetChanged()

    }
}