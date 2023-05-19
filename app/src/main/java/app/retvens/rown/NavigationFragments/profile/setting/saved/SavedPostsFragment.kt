package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R

class SavedPostsFragment : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var savedPostsAdapter: SavedPostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mediaRecyclerView = view.findViewById(R.id.mediaSavedRecyclerView)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)

        val blogs = listOf<MediaData>(
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_awards),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
            MediaData(R.drawable.png_awards),
            MediaData(R.drawable.png_blog),
            MediaData(R.drawable.png_post),
        )

        savedPostsAdapter = SavedPostsAdapter(blogs, requireContext())
        mediaRecyclerView.adapter = savedPostsAdapter
        savedPostsAdapter.notifyDataSetChanged()


    }
}