package app.retvens.rown.viewAll.viewAllCommunities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetFilterCommunity
import app.retvens.rown.bottomsheet.BottomSheetFilterVendors
import app.retvens.rown.databinding.ActivityViewAllAvailableCommunitiesBinding
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsAdapter
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllAvailableCommunitiesActivity : AppCompatActivity(),
    BottomSheetFilterCommunity.OnBottomSheetFilterCommunityClickListener {

    lateinit var binding : ActivityViewAllAvailableCommunitiesBinding

    lateinit var viewAllCommunityAdapter: ViewAllCommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllAvailableCommunitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewAllCommRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.viewAllCommRecycler.setHasFixedSize(true)

        binding.filterSearch.setOnClickListener {
            val bottomSheet = BottomSheetFilterCommunity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetFilterCommunity.FilterCommunity_TAG)}
            bottomSheet.setOnFilterClickListener(this)
        }


        fetchCommunity()
    }

    private fun fetchCommunity() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val fetchCommunity = RetrofitBuilder.feedsApi.fetchOpenCommunity(user_id)

       fetchCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
           override fun onResponse(
               call: Call<List<GetCommunitiesData>?>,
               response: Response<List<GetCommunitiesData>?>
           ) {
               if (response.isSuccessful){
                   val response = response.body()!!
                   viewAllCommunityAdapter = ViewAllCommunityAdapter(response, applicationContext)
                   binding.viewAllCommRecycler.adapter = viewAllCommunityAdapter
                   viewAllCommunityAdapter.notifyDataSetChanged()
               }
           }

           override fun onFailure(call: Call<List<GetCommunitiesData>?>, t: Throwable) {
               TODO("Not yet implemented")
           }
       })
    }

    override fun bottomSheetFilterCommunityClick(FilterCommunityFrBo: String) {

    }

}