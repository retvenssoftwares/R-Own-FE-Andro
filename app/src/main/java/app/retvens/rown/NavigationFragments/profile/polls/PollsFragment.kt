package app.retvens.rown.NavigationFragments.profile.polls

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PollsFragment(val userId: String, val isOwner : Boolean, val username : String) : Fragment() {

    lateinit var pollsRecyclerView: RecyclerView
    lateinit var pollsAdapter: PollsAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView

    private var list:ArrayList<PostItem> = ArrayList()

    private var isLoading:Boolean = false
    private lateinit var progress: ProgressBar
    private var currentPage = 1
    var pageSize = 0
    private var lastPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        progress = view.findViewById(R.id.progress)

        pollsRecyclerView = view.findViewById(R.id.pollsRecycler)
        pollsRecyclerView.layoutManager = LinearLayoutManager(context)
        pollsRecyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        pollsAdapter = PollsAdapter(list, requireContext(), userId)
        pollsRecyclerView.adapter = pollsAdapter


        pollsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
//                        if (currentPage > lastPage) {
                            isLoading = false
                            lastPage++
                            getData()
//                        }
                    }
                }
            }
        })

        getPolls(userId)

    }

    private fun getData() {
        val handler = Handler()

        progress.visibility = View.VISIBLE

        handler.postDelayed({
            if (isAdded) {
                getPolls(userId)
                progress.visibility = View.GONE
            }
        },
            3000)
    }

    private fun getPolls(userId: String) {

        val getPoll = RetrofitBuilder.feedsApi.getNormalUserPoll(userId,userId,"$currentPage")

        getPoll.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        val response = response.body()!!

                        Log.e("polls",response.toString())

                        if (response.isNotEmpty()) {

                    response.forEach { postsDataClass ->

                        try {
//                            if (postsDataClass.posts.size >= 10){
//                            }
                           if( postsDataClass.posts.isNotEmpty() ){
                               isLoading = false
                               currentPage++
                               list.addAll(postsDataClass.posts)

                               pollsAdapter.removePostsFromList(postsDataClass.posts)
                               pollsAdapter.notifyDataSetChanged()
                            } else {
                               if (currentPage == 1) {
                                   notPosted.visibility = View.VISIBLE
                                   empty.visibility = View.VISIBLE
                                   if (isOwner) {
                                       empty.text = "You have not posted anything yet."
                                   } else {
                                       empty.text = "$username have not posted anything yet."
                                   }
                               }
                           }
                        }catch (e:NullPointerException){
                            if (currentPage == 1) {
                                notPosted.visibility = View.VISIBLE
                                empty.visibility = View.VISIBLE
                                if (isOwner) {
                                    empty.text = "You have not posted anything yet."
                                } else {
                                    empty.text = "$username have not posted anything yet."
                                }
                            }
                        }


                    }
                        } else {
                            if (currentPage == 1) {
                                notPosted.visibility = View.VISIBLE
                                empty.visibility = View.VISIBLE
                                if (isOwner) {
                                    empty.text = "You have not posted anything yet."
                                } else {
                                    empty.text = "$username have not posted anything yet."
                                }
                            }
                        }
                    } else {
                        if (currentPage == 1) {
                            empty.visibility = View.VISIBLE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                if (isAdded) {
                    empty.visibility = View.VISIBLE
                    empty.text = "${t.localizedMessage}"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    isLoading = false
                }
            }
        })
    }
}