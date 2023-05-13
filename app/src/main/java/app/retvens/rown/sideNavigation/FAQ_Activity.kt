package app.retvens.rown.sideNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityFaqBinding

class FAQ_Activity : AppCompatActivity() {
    lateinit var binding : ActivityFaqBinding

    var isVisibleC = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.faqBackBtn.setOnClickListener { onBackPressed() }

        binding.cardQuestion.setOnClickListener {
            if (!isVisibleC) {
                binding.cardAnswer.visibility = View.VISIBLE
                isVisibleC = true
            } else {
                binding.cardAnswer.visibility = View.GONE
                isVisibleC = false
            }
        }

    }
}