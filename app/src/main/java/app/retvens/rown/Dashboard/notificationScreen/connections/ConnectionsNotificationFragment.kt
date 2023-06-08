package app.retvens.rown.Dashboard.notificationScreen.connections

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationAdapter
import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationDataItem
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
        recyclerConnection.setHasFixedSize(true)

        nothing = view.findViewById(R.id.nothing)
        empty = view.findViewById(R.id.emptyBlog)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        getNotifications()

    }
    private fun getNotifications() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getN = RetrofitBuilder.Notification.getConnectionNotification(user_id)
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
                            connectionNotificationAdapter = ConnectionNotificationAdapter(data, requireContext())
                            recyclerConnection.adapter = connectionNotificationAdapter
                            connectionNotificationAdapter.notifyDataSetChanged()
                            Log.d("on", data.toString())
                        } else {
                            nothing.visibility = View.VISIBLE
                        }
                    } else {
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<List<PersonalNotificationDataItem>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - Network Error"
                empty.visibility = View.VISIBLE
            }
        })
    }
}