package app.retvens.rown.viewAll.vendorsDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllVendorsBinding

class VendorsImageViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewAllVendorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllVendorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }
    }
}