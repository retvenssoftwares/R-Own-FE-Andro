package app.retvens.rown.NavigationFragments.profile.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusFragment(val userId: String, val isOwner : Boolean, val username : String) : Fragment() {

    lateinit var statusRecycler : RecyclerView
    lateinit var statusAdapter: StatusAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView
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

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)



        getMedia(userId)
    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getNormalUserStatus(userId,userId, "1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?>,
            StatusAdapter.OnItemClickListener {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        val response = response.body()!!
                        if (response.isNotEmpty()) {

                            response.forEach { postsDataClass ->

                        try {
                            statusAdapter = StatusAdapter(postsDataClass.posts as ArrayList<PostItem>, requireContext())
                            statusRecycler.adapter = statusAdapter
                            statusAdapter.removePostsFromList(postsDataClass.posts)
                            statusAdapter.notifyDataSetChanged()

                            statusAdapter.setOnItemClickListener(this)
                        }catch (e:NullPointerException){
                            notPosted.visibility = View.VISIBLE
                            empty.visibility = View.VISIBLE
                            if (isOwner){
                                empty.text = "You have not posted anything yet."
                            } else {
                                empty.text = "$username have not posted anything yet."
                            }
                        }



                    }
                        } else {
                            notPosted.visibility = View.VISIBLE
                            empty.visibility = View.VISIBLE
                            if (isOwner){
                                empty.text = "You have not posted anything yet."
                            } else {
                                empty.text = "$username have not posted anything yet."
                            }
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
                if (isAdded) {
                    empty.visibility = View.VISIBLE
                    empty.text = "${t.localizedMessage}"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
//                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
                }
            }

            override fun onItemClick(dataItem: PostItem) {
                postLike(dataItem.post_id)
            }

            override fun onItemClickForComment(banner: PostItem, position: Int) {
                val bottomSheet = BottomSheetComment(banner.post_id,banner.Profile_pic)
                val fragManager = (activity as FragmentActivity).supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            }
        })
    }

    private fun postLike(postId:String) {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = LikesCollection(user_id)

        val like = RetrofitBuilder.feedsApi.postLike(postId,data)

        like.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }
}