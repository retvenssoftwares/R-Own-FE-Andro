package app.retvens.rown.NavigationFragments.exploreForUsers.services

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreServicesFragment : Fragment() {


    lateinit var exploreBlogsRecyclerView: RecyclerView
    lateinit var exploreServicesAdapter: ExploreServicesAdapter
    private var isLoading:Boolean = false
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var currentPage = 1
    private lateinit var progress: ProgressBar
    private var hotelList:ArrayList<ProfileServicesDataItem> = ArrayList()
    lateinit var empty : TextView
    lateinit var errorImage : ImageView

    private var lastPage = 1

    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        exploreBlogsRecyclerView = view.findViewById(R.id.explore_services_recycler)
        exploreBlogsRecyclerView.layoutManager = GridLayoutManager(context,2)
        exploreBlogsRecyclerView.setHasFixedSize(true)

        exploreServicesAdapter = ExploreServicesAdapter(hotelList, requireContext())
        exploreBlogsRecyclerView.adapter = exploreServicesAdapter
        exploreServicesAdapter.removeServicesFromList(hotelList)
        exploreServicesAdapter.notifyDataSetChanged()

        empty = view.findViewById(R.id.empty)
        errorImage = view.findViewById(R.id.errorImage)
        progress = view.findViewById(R.id.progress)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        progressDialog = Dialog(requireContext())
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()


        exploreBlogsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                        if (currentPage > lastPage) {
                            isLoading = false
                            lastPage++
                            getData()
                        }
                    }
                }


            }
        })


        getServices()
    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getServices()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getServices() {
        val serv = RetrofitBuilder.exploreApis.getExploreService(currentPage.toString())
        serv.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {
                progressDialog.dismiss()
                try {
                    if (isAdded){
                        if (response.isSuccessful){
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                               try {
                                   val data = response.body()!!
                                   data.forEach {
                                       if (it.vendors.size >= 10){
                                           currentPage++
                                       }
                                       hotelList.addAll(it.vendors)
                                       exploreServicesAdapter.removeServicesFromList(hotelList)
                                       exploreServicesAdapter.notifyDataSetChanged()
                                   }
                               } catch (e:NullPointerException){
                                   Log.e("error",e.message.toString())
                               }

                            } else {
//                                errorImage.visibility = View.VISIBLE
                            }
                        } else {
//                            errorImage.visibility = View.VISIBLE
//                            exploreBlogsRecyclerView.visibility = View.GONE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    }
                }catch (e:NullPointerException){
//                    errorImage.visibility = View.VISIBLE
//                    exploreBlogsRecyclerView.visibility = View.GONE
                    Toast.makeText(requireContext(),"No Services Yet",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {

                progressDialog.dismiss()

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
//                errorImage.visibility = View.VISIBLE
            }
        })
    }
}