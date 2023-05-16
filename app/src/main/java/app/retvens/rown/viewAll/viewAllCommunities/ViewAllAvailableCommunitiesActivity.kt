package app.retvens.rown.viewAll.viewAllCommunities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetFilterCommunity
import app.retvens.rown.bottomsheet.BottomSheetFilterVendors
import app.retvens.rown.databinding.ActivityViewAllAvailableCommunitiesBinding
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsAdapter
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsData

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

        val blogs = listOf<ViewAllCommunityData>(
            ViewAllCommunityData("Title 22", "Join"),
            ViewAllCommunityData("Title 23", "Request"),
            ViewAllCommunityData("Title 21", "Join"),
            ViewAllCommunityData("Title 22", "Join"),
            ViewAllCommunityData("Title 23", "Request"),
            ViewAllCommunityData("Title 21", "Join"),
            ViewAllCommunityData("Title 22", "Join"),
            ViewAllCommunityData("Title 23", "Request"),
            ViewAllCommunityData("Title 21", "Join"),
        )

        viewAllCommunityAdapter = ViewAllCommunityAdapter(blogs, this)
        binding.viewAllCommRecycler.adapter = viewAllCommunityAdapter
        viewAllCommunityAdapter.notifyDataSetChanged()
    }

    override fun bottomSheetFilterCommunityClick(FilterCommunityFrBo: String) {

    }

}