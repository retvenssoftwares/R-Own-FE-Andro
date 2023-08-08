package app.retvens.rown.NavigationFragments.profile.services

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.VendorServicesAdapter
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.ProfileCompletion.VendorServicesData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetEventCategory
import app.retvens.rown.bottomsheet.BottomSheetServiceName
import app.retvens.rown.utils.serverCode
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServicesFragment(val userId: String, val isOwner: Boolean, val username: String) : Fragment(),
    BottomSheetServiceName.OnBottomSNClickListener {

    lateinit var servicesRecycler: RecyclerView
    lateinit var profileServicesAdapter: ProfileServicesAdapter
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty: TextView
    lateinit var notPosted: ImageView

    lateinit var addService: CardView

    private var list: ArrayList<ProfileServicesDataItem> = ArrayList()

    private var isLoading: Boolean = false
    private lateinit var progress: ProgressBar
    private var currentPage = 1
    var pageSize = 0
    private var lastPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesRecycler = view.findViewById(R.id.servicesRecycler)
        servicesRecycler.layoutManager = LinearLayoutManager(context)
        //servicesRecycler. //recyclerView.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)
        progress = view.findViewById(R.id.progress)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        profileServicesAdapter = ProfileServicesAdapter(list, requireContext(), isOwner)
        servicesRecycler.adapter = profileServicesAdapter

        addService = view.findViewById(R.id.addService)
        if (!isOwner) {
            addService.visibility = View.GONE
        }

        servicesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (isAdded && isLoading && (lastVisibleItemPosition == totalItem - 1)) {
//                        if (currentPage > lastPage) {
                        isLoading = false
                        lastPage++
//                        getData()
//                        }
                    }
                }
            }
        })


        getServices()

        addService.setOnClickListener {
            val bottomSheet = BottomSheetServiceName()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let { bottomSheet.show(it, BottomSheetServiceName.SN_TAG) }
            bottomSheet.setOnSNclickListener(this)
        }
    }

    private fun getData() {
        val handler = Handler()

        progress.visibility = View.VISIBLE;

        handler.postDelayed(
            {
                if (isAdded) {
                    getServices()
                    progress.visibility = View.GONE;
                }
            },
            3000
        )
    }

    private fun getServices() {
        val sharedPreferences =
            requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val serviceG = RetrofitBuilder.ProfileApis.getProfileService(userId)
        serviceG.enqueue(object : Callback<List<ProfileServicesDataItem>?> {
            override fun onResponse(
                call: Call<List<ProfileServicesDataItem>?>,
                response: Response<List<ProfileServicesDataItem>?>
            ) {
                if (isAdded) {
                    serverCode = response.code()
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE

                    if (response.isSuccessful) {

                        Log.d("res", response.body().toString())
                        val response = response.body()!!
                        if (response.isNotEmpty()) {
                            try {
                                list.addAll(response)

//                                if (response.size >= 10){

//                                }

                                isLoading = false

                                profileServicesAdapter.notifyDataSetChanged()
                                Log.d("res", response.toString())
                            } catch (e: NullPointerException) {
                                if (currentPage == 1) {
                                    Log.e("error", e.message.toString())
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
                            addService.visibility = View.GONE
                            empty.visibility = View.VISIBLE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ProfileServicesDataItem>?>, t: Throwable) {
                if (isAdded) {
                    addService.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                    empty.text = "${t.localizedMessage}"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                }
            }
        })
    }

    override fun bottomSNClick(serviceName: String) {

    }
}