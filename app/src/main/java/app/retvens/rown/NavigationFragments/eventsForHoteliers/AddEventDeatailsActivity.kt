package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityAddEventBinding
import app.retvens.rown.databinding.ActivityAddEventDeatailsBinding

class AddEventDeatailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddEventDeatailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventDeatailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location = intent.getStringExtra("location")
        val venue = intent.getStringExtra("venue")
        val title = intent.getStringExtra("title")
        val category_id = intent.getStringExtra("category_id")
        val description = intent.getStringExtra("description")
        val eventCategory = intent.getStringExtra("eventCategory")

        binding.nextEvent.setOnClickListener {
            if (binding.etEmailEvent.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show()
            } else if (binding.etPhoneVenue.text.toString()
                    .isEmpty() || binding.etWebsite.text.toString()
                    .isEmpty() || binding.etBooking.text.toString()
                    .isEmpty() || binding.price.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, EvenDetailsActivity::class.java)
                intent.putExtra("location", location)
                intent.putExtra("venue", venue)
                intent.putExtra("category_id", category_id)
                intent.putExtra("title", title)
                intent.putExtra("description", description)
                intent.putExtra("eventCategory", eventCategory)
                intent.putExtra("email", binding.etEmailEvent.text.toString())
                intent.putExtra("phone", binding.etPhoneVenue.text.toString())
                intent.putExtra("website", binding.etWebsite.text.toString())
                intent.putExtra("booking", binding.etBooking.text.toString())
                intent.putExtra("price", binding.price.text.toString())
                startActivity(intent)
            }
        }
    }
}