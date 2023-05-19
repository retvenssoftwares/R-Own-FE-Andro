package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityEvenDetailsBinding

class EvenDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityEvenDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvenDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextEventCreate.setOnClickListener {
            startActivity(Intent(this, AllEventsPostedActivity::class.java))
        }

    }
}