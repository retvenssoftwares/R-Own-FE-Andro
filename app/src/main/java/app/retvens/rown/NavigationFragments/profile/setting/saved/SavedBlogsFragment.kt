package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogsData
import app.retvens.rown.R


class SavedBlogsFragment : Fragment() {

lateinit var savedBlogsRecyclerView : RecyclerView
lateinit var savedBlogsAdapter: SavedBlogsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_blogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedBlogsRecyclerView = view.findViewById(R.id.savedBlogsRecyclerView)
        savedBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedBlogsRecyclerView.setHasFixedSize(true)

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

        savedBlogsAdapter = SavedBlogsAdapter(blogs, requireContext())
        savedBlogsRecyclerView.adapter = savedBlogsAdapter
        savedBlogsAdapter.notifyDataSetChanged()

    }
}