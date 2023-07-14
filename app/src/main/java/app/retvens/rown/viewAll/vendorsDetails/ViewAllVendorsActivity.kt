package app.retvens.rown.viewAll.vendorsDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetFilterVendors
import app.retvens.rown.databinding.ActivityViewAllVendorsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllVendorsActivity : AppCompatActivity(), BottomSheetFilterVendors.OnBottomFilterClickListener {
    lateinit var binding : ActivityViewAllVendorsBinding

    lateinit var exploreServicesAdapter: ExploreServicesAdapter

    lateinit var viewAllVendorsAdapter: ViewAllVendorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllVendorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewAllVendorsRecycler.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.viewAllVendorsRecycler.setHasFixedSize(true)

        binding.filterSearch.setOnClickListener {
            val bottomSheet = BottomSheetFilterVendors()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetFilterVendors.FilterVendors_TAG)}
            bottomSheet.setOnFilterClickListener(this)
        }

        val blogs = listOf<ViewAllVendorsData>(
            ViewAllVendorsData("Paradise Inn"),
            ViewAllVendorsData("Title 2"),
            ViewAllVendorsData("Title 21"),
            ViewAllVendorsData("Title 24"),
            ViewAllVendorsData("Title 23"),
            ViewAllVendorsData("Title 32"),
            ViewAllVendorsData("Title 3"),
            ViewAllVendorsData("Paradise Inn"),
            ViewAllVendorsData("Title 2"),
            ViewAllVendorsData("Title 21"),
            ViewAllVendorsData("Title 24"),
            ViewAllVendorsData("Title 23"),
            ViewAllVendorsData("Title 32"),
            ViewAllVendorsData("Title 3"),
        )

        getServices()


//        viewAllVendorsAdapter = ViewAllVendorsAdapter(blogs, applicationContext)
//        binding.viewAllVendorsRecycler.adapter = viewAllVendorsAdapter
//        viewAllVendorsAdapter.notifyDataSetChanged()
    }

    private fun getServices() {
        val serv = RetrofitBuilder.exploreApis.getExploreService("1")
        serv.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {
                    if (response.isSuccessful){
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE
                        if (response.body()!!.isNotEmpty()) {
                            val data = response.body()!!
                            data.forEach {
                                try {
                                    exploreServicesAdapter = ExploreServicesAdapter(it.events as ArrayList<ProfileServicesDataItem>, this@ViewAllVendorsActivity)
                                    binding.viewAllVendorsRecycler.adapter = exploreServicesAdapter
                                    exploreServicesAdapter.removeServicesFromList(it.events)
                                    exploreServicesAdapter.notifyDataSetChanged()
                                }catch (e:NullPointerException){
                                    Log.e("error",e.message.toString())
                                }

                            }
                        } else {
                            binding.empty.visibility = View.VISIBLE
                        }
                    } else {
                        binding.empty.visibility = View.VISIBLE
                        binding.empty.text = response.code().toString()
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.GONE
                    }
                }
            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.empty.text = "Try Again - ${t.localizedMessage}"
                binding.empty.visibility = View.VISIBLE
            }
        })
    }
    override fun bottomFilterVendorsClick(FilterVendorsFrBo: String) {

    }
}