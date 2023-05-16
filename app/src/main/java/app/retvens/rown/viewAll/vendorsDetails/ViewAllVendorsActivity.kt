package app.retvens.rown.viewAll.vendorsDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetFilterVendors
import app.retvens.rown.databinding.ActivityViewAllVendorsBinding

class ViewAllVendorsActivity : AppCompatActivity(), BottomSheetFilterVendors.OnBottomFilterClickListener {
    lateinit var binding : ActivityViewAllVendorsBinding

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

        viewAllVendorsAdapter = ViewAllVendorsAdapter(blogs, applicationContext)
        binding.viewAllVendorsRecycler.adapter = viewAllVendorsAdapter
        viewAllVendorsAdapter.notifyDataSetChanged()
    }

    override fun bottomFilterVendorsClick(FilterVendorsFrBo: String) {

    }
}