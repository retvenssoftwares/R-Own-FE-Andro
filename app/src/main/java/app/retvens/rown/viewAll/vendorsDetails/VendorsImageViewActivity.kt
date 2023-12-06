package app.retvens.rown.viewAll.vendorsDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityVendorsImageViewBinding
import app.retvens.rown.databinding.ActivityViewAllVendorsBinding
import com.bumptech.glide.Glide

class VendorsImageViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityVendorsImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorsImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        val img1 = intent.getStringExtra("img1")
        val img2 = intent.getStringExtra("img2")
        val img3 = intent.getStringExtra("img3")

        try {
            if (img1!!.isNotEmpty() && img2!!.isNotEmpty() && img3!!.isNotEmpty()) {
                Glide.with(applicationContext)
                    .load(img1)
                    .into(binding.img1)
                Glide.with(applicationContext)
                    .load(img2)
                    .into(binding.img2)
                Glide.with(applicationContext)
                    .load(img3)
                    .into(binding.img3)
            } else if (img1.isNotEmpty() && img2!!.isNotEmpty()) {
                binding.img3.visibility = View.GONE
                Glide.with(applicationContext)
                    .load(img1)
                    .into(binding.img1)
                Glide.with(applicationContext)
                    .load(img2)
                    .into(binding.img2)
            } else if (img1.isNotEmpty()) {
                binding.img3.visibility = View.GONE
                binding.img2.visibility = View.GONE
                Glide.with(applicationContext)
                    .load(img1)
                    .into(binding.img1)
            } else {
                binding.img3.visibility = View.GONE
                binding.img2.visibility = View.GONE
                binding.img3.visibility = View.GONE
            }
        } catch (e:NullPointerException){

        }
    }
}