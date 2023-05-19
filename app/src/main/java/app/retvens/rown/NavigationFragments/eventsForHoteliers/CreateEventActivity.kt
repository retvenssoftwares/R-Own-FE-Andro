package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCreateEventBinding

class CreateEventActivity : AppCompatActivity() {
    lateinit var binding : ActivityCreateEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextEventCreate.setOnClickListener {
            startActivity(Intent(applicationContext, AddEventDeatailsActivity::class.java))
        }

    }
}