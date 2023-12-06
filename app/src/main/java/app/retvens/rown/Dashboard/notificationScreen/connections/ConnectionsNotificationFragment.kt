package app.retvens.rown.Dashboard.notificationScreen.connections

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationAdapter
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationDataItem
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnectionsNotificationFragment : Fragment() {

    lateinit var recyclerConnection : RecyclerView
    lateinit var connectionNotificationAdapter: ConnectionNotificationAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var empty : TextView
    lateinit var nothing : ImageView

    private var currentPage = 1
    private var isLoading = false
    private var notificationList:ArrayList<PersonalNotificationDataItem> = ArrayList()
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connections_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerConnection = view.findViewById(R.id.recyclerConnection)
        recyclerConnection.layoutManager = LinearLayoutManager(context)
        //recyclerConnection. //recyclerView.setHasFixedSize(true)

        progress = view.findViewById(R.id.progress)
        nothing = view.findViewById(R.id.nothing)
        empty = view.findViewById(R.id.emptyBlog)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        connectionNotificationAdapter = ConnectionNotificationAdapter(notificationList, requireContext())
        recyclerConnection.adapter = connectionNotificationAdapter

        recyclerConnection.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("working","okk")
                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    Log.e("working","okk")
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
                        isLoading = false
                        currentPage++
                        getData()
                    }
                }
            }
        })

        getNotifications()

    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getNotifications()
            progress.setVisibility(View.GONE);
        },
            1500)
    }

    private fun getNotifications() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getN = RetrofitBuilder.Notification.getConnectionNotification(user_id, "$currentPage")
        getN.enqueue(object : Callback<List<PersonalNotificationDataItem>?> {
            override fun onResponse(
                call: Call<List<PersonalNotificationDataItem>?>,
                response: Response<List<PersonalNotificationDataItem>?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            notificationList.addAll(data)
                            connectionNotificationAdapter.notifyDataSetChanged()
                            Log.d("on", data.toString())
                        } else {
                            if (currentPage == 1) {
                                nothing.visibility = View.VISIBLE
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
            override fun onFailure(call: Call<List<PersonalNotificationDataItem>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - Network Error"
                if (currentPage == 1) {
                    empty.visibility = View.VISIBLE
                }
            }
        })
    }
}