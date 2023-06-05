package app.retvens.rown.Dashboard.notificationScreen.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalNotificationFragment : Fragment() {

    lateinit var recyclerPersonal : RecyclerView
    lateinit var personalNotificationAdapter: PersonalNotificationAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerPersonal = view.findViewById(R.id.recyclerPersonal)
        recyclerPersonal.layoutManager = LinearLayoutManager(context)
        recyclerPersonal.setHasFixedSize(true)

        empty = view.findViewById(R.id.emptyBlog)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutBlog)

        getNotifications()

    }

    private fun getNotifications() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val getN = RetrofitBuilder.Notification.getPersonalNotification(user_id)
        getN.enqueue(object : Callback<List<PersonalNotificationDataItem>?> {
            override fun onResponse(
                call: Call<List<PersonalNotificationDataItem>?>,
                response: Response<List<PersonalNotificationDataItem>?>
            ) {
                if (isAdded){
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                                personalNotificationAdapter = PersonalNotificationAdapter(data, requireContext())
                                recyclerPersonal.adapter = personalNotificationAdapter
                                personalNotificationAdapter.notifyDataSetChanged()
                                Log.d("on", data.toString())
                        } else {
                            empty.visibility = View.VISIBLE
                            empty.text = "No Notifications"
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