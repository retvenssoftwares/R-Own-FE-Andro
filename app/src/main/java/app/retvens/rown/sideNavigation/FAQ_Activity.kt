package app.retvens.rown.sideNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityFaqBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQ_Activity : AppCompatActivity() {
    lateinit var binding : ActivityFaqBinding

    lateinit var faqAdapter: FaqAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.faqBackBtn.setOnClickListener { onBackPressed() }

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.setHasFixedSize(true)

        fetFaq()
    }

    private fun fetFaq() {
        val faq = RetrofitBuilder.ProfileApis.getFAQ()
        faq.enqueue(object : Callback<List<faqData>?> {
            override fun onResponse(
                call: Call<List<faqData>?>,
                response: Response<List<faqData>?>
            ) {
                if (response.isSuccessful){
                    faqAdapter = FaqAdapter(response.body()!!, this@FAQ_Activity)
                    binding.recycler.adapter = faqAdapter
                    faqAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<faqData>?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}