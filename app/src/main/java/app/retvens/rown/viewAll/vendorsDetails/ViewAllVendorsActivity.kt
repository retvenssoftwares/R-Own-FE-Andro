package app.retvens.rown.viewAll.vendorsDetails

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetFilterVendors
import app.retvens.rown.databinding.ActivityViewAllVendorsBinding
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllVendorsActivity : AppCompatActivity(), BottomSheetFilterVendors.OnBottomFilterClickListener {
    lateinit var binding : ActivityViewAllVendorsBinding

    lateinit var exploreServicesAdapter: ExploreServicesAdapter

    private var isLoading:Boolean = false
    private var currentPage = 1
    private var hotelList:ArrayList<ProfileServicesDataItem> = ArrayList()
    lateinit var empty : TextView
    lateinit var errorImage : ImageView

    private lateinit var progressDialog: Dialog

    lateinit var viewAllVendorsAdapter: ViewAllVendorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllVendorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewAllVendorsRecycler.layoutManager = GridLayoutManager(applicationContext, 2)
        //binding.viewAllVendorsRecycler. //recyclerView.setHasFixedSize(true)

        exploreServicesAdapter = ExploreServicesAdapter(hotelList, this)
        binding.viewAllVendorsRecycler.adapter = exploreServicesAdapter
        exploreServicesAdapter.removeServicesFromList(hotelList)
        exploreServicesAdapter.notifyDataSetChanged()

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(this).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        binding.filterSearch.setOnClickListener {
            val bottomSheet = BottomSheetFilterVendors()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetFilterVendors.FilterVendors_TAG)}
            bottomSheet.setOnFilterClickListener(this)
        }

        binding.viewAllVendorsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                            isLoading = false
                            getData()
                    }
                }


            }
        })


        getServices()

        binding.searchVendor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 == ""){
                    getServices()
                }else{
                    val text = p0.toString()
                    searchServices(text)
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun searchServices(text: String) {

        val getServices = RetrofitBuilder.exploreApis.searchServices(text,"1")

        getServices.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {

                val list:ArrayList<ProfileServicesDataItem> = ArrayList()
                if(response.isSuccessful){
                    val response = response.body()!!
                    Log.e("res",response.toString())
                    response.forEach {
                        try {
                            if (it.message == "You have reached the end"){
                                exploreServicesAdapter = ExploreServicesAdapter(ArrayList(), this@ViewAllVendorsActivity)
                                binding.viewAllVendorsRecycler.adapter = exploreServicesAdapter
                                exploreServicesAdapter.notifyDataSetChanged()
                            }else{
                                list.addAll(it.posts)
                                exploreServicesAdapter = ExploreServicesAdapter(list, this@ViewAllVendorsActivity)
                                binding.viewAllVendorsRecycler.adapter = exploreServicesAdapter
                                exploreServicesAdapter.notifyDataSetChanged()
                            }

                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }


                    }

                }else{
                    getServices()
                }
            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {

            }
        })

    }

    private fun getData() {
        val handler = Handler()

        binding.progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getServices()
            binding.progress.setVisibility(View.GONE);
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
                        if (response.isSuccessful){
                            binding.shimmerFrameLayout.stopShimmer()
                            binding.shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                                try {
                                    val data = response.body()!!
                                    data.forEach {
//                                        if (it.vendors.size >= 10){
                                            currentPage++
//                                        }
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
                            binding.shimmerFrameLayout.stopShimmer()
                            binding.shimmerFrameLayout.visibility = View.GONE
                        }

                }catch (e:NullPointerException){
//                    errorImage.visibility = View.VISIBLE
//                    exploreBlogsRecyclerView.visibility = View.GONE
                    Toast.makeText(applicationContext,"No Services Yet", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {
                progressDialog.dismiss()

                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
//                errorImage.visibility = View.VISIBLE
            }
        })
    }
    override fun bottomFilterVendorsClick(FilterVendorsFrBo: String) {

    }
}