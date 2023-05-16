package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R


class ExploreBlogsFragment : Fragment() {

    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreBlogsAdapter : ExploreBlogsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_blogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreBlogsRecyclerView = view.findViewById(R.id.explore_blogs_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreBlogsData>(
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
            ExploreBlogsData("Title 1"),
            ExploreBlogsData("Title 2"),
            ExploreBlogsData("Title 3"),
            ExploreBlogsData("Title 23"),
        )

        exploreBlogsAdapter = ExploreBlogsAdapter(blogs, requireContext())
        exploreBlogsRecyclerView.adapter = exploreBlogsAdapter
        exploreBlogsAdapter.notifyDataSetChanged()

    }
}