package app.retvens.rown.NavigationFragments.profile.media

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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.status.StatusAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetHotelierProfileSetting
import app.retvens.rown.bottomsheet.BottomSheetPostEdit
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaFragment(val userId: String, val isOwner : Boolean) : Fragment(), MediaAdapter.OnItemClickListener {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var mediaAdapter: MediaAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaRecyclerView = view.findViewById(R.id.mediaRecyclerView)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)


        getMedia(userId)

    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getUserProfileMedia(userId,userId,"1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?>{
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        if (response.body()!!.isNotEmpty()) {
                    val response = response.body()!!

                    response.forEach { postsDataClass ->
                        try {
                            val postsToDisplay = postsDataClass.posts.filter { it.display_status == "1" }
                            if (postsToDisplay.isEmpty()) {
                                // No posts to display
                                notPosted.visibility = View.VISIBLE
                            } else {
                                // Display posts using the MediaAdapter
                                mediaAdapter = MediaAdapter(requireContext(), postsToDisplay)
                                mediaRecyclerView.adapter = mediaAdapter
                                mediaAdapter.notifyDataSetChanged()
                                mediaAdapter.setOnItemClickListener(this@MediaFragment)
                            }
                        } catch (e: NullPointerException) {
                            // Handle NullPointerException
                            notPosted.visibility = View.VISIBLE
                        }



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

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }


        })



    }

    override fun onItemClick(dataItem: PostItem) {
        if (isOwner){
        val bottomSheet = BottomSheetPostEdit(dataItem.post_id,dataItem.caption,dataItem.location)
        val fragManager = (activity as FragmentActivity).supportFragmentManager
        fragManager.let{bottomSheet.show(it, BottomSheetPostEdit.Hotelier_TAG)}
    }
}
}